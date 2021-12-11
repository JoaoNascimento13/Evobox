package application.core;

import java.awt.Point;
import java.util.ArrayList;

import application.dynamic.creatures.Creature;
import application.dynamic.creatures.Diet;
import application.dynamic.creatures.Species;
import application.dynamic.flow.FlowGenerator;

public class MapStateSingleton {

	
	transient private Point[][] flowMap;
	transient private Creature[][] creatureMap;

	
	
	public ArrayList<Creature> activeCreatures;
	public ArrayList<Species> activeSpecies;
	
	public ArrayList<Long> deadCreaturesToRemoveIds;
	public ArrayList<Creature> bornCreaturesToAdd;
	
	public ArrayList<FlowGenerator> flowGenerators;
	

	public long nextCreatureId;
	public int nextSpeciesId;

	
	private Creature focusedCreature;
	private Species focusedSpecies;
	
	transient private boolean highlightSpecies;
	transient private boolean trackFocusedCreature;
	
	transient public boolean refreshFocusedCreature;

	public long tick;
	
	
	private static final MapStateSingleton mapState = new MapStateSingleton();

	
	private MapStateSingleton() {}
	
	
    static public MapStateSingleton getInstance(){
    	return mapState;
    }



    public Point[][] getFlowMap() {
    	return flowMap;
    }
    
    
    public void initialize() {
    	initializeFlowMap();
    	initializeCreatureMap();

		tick = 0;
    	nextCreatureId = 1;
    	nextSpeciesId = 1;
		activeCreatures = new ArrayList<Creature>();
		activeSpecies = new ArrayList<Species>();
		bornCreaturesToAdd = new ArrayList<Creature>();
		deadCreaturesToRemoveIds = new ArrayList<Long>();
		flowGenerators = new ArrayList<FlowGenerator>();
    }
    
    public void initializeFlowMap() {
		SettingsSingleton settings = SettingsSingleton.getInstance();
		flowMap = new Point[settings.mapCellsX][settings.mapCellsY];
		for (int i = 0; i < settings.mapCellsX; i++) {
			for (int j = 0; j < settings.mapCellsX; j++) {
				flowMap[i][j] = new Point(0, 0);
			}
		}
    }
    public void initializeCreatureMap() {
		SettingsSingleton settings = SettingsSingleton.getInstance();
		creatureMap = new Creature[settings.mapCellsX][settings.mapCellsY];
    }
   
   

    public void registerCreature(Creature creature) {
    	
    	creature.id = nextCreatureId;
    	nextCreatureId++;
    	activeCreatures.add(creature);
    	
    	creature.species.currentMembers++;
    	creature.species.totalMembers++;
    	creature.species.members.add(creature);
    	
    	creature.numberInSpecies = creature.species.totalMembers;
    	
    	if (creature.mutated) {
//    		mapState.focusedCreature = creature;
//    		mapState.refreshFocusedCreature = true;
    		
        	creature.species.currentMutatedMembers++;
    	}
		
    }
    

    public void registerSpecies(Species species) {
    	species.id = nextSpeciesId;
    	nextSpeciesId++;
    	activeSpecies.add(species);
    }
    public void unregisterSpecies(Species species) {
    	activeSpecies.remove(species);
    }

	public int getMaxNumberOfCreaturesOfSameSpecies() {
		int maxnumber = 0;
		for (Species s : activeSpecies) {
			maxnumber = Math.max(maxnumber, s.currentMembers);
		}
		return maxnumber;
	}
    
    

    public void queueCreatureRegister(Creature creature) {
    	bornCreaturesToAdd.add(creature);
    }
    public void registerBornCreatures() {
    	for (Creature c : bornCreaturesToAdd) {
    		registerCreature(c);
    	}
    	bornCreaturesToAdd.clear();
    }
    public void queueCreatureUnregister(Creature creature) {
    	deadCreaturesToRemoveIds.add(creature.id);
    }
    public void unregisterDeadCreatures() {
		OutdatedPositionsSingleton outdatedPositions = OutdatedPositionsSingleton.getInstance();
		ArrayList<Integer> oldCreaturePositionsX = outdatedPositions.getOutdatedCreaturesX();
		ArrayList<Integer> oldCreaturePositionsY = outdatedPositions.getOutdatedCreaturesY();
		Creature deadCreature;
    	for (long deadCreatureId : deadCreaturesToRemoveIds) {
    		for (int i = 0; i < activeCreatures.size(); i++) {
        		if (activeCreatures.get(i).id == deadCreatureId) {
        			deadCreature = activeCreatures.get(i);
        			oldCreaturePositionsX.add(deadCreature.x);
        			oldCreaturePositionsY.add(deadCreature.y);

            		for (int j = 0; j < deadCreature.species.members.size(); j++) {
            			if (deadCreature.species.members.get(j).id == deadCreatureId) {
            				deadCreature.species.members.remove(j);
            				break;
            			}
            		}
        			activeCreatures.remove(i);
        			break;
        		}
    		}
    	}
    	deadCreaturesToRemoveIds.clear();
    }
//    public void unregisterCreature(Creature creature) {
//    	for (int i = 0; i < creatures.size(); i++) {
//    		if (creatures.get(i).id == creature.id) {
//    			creatures.remove(i);
//    			break;
//    		}
//    	}
//    }
    
    public boolean isAvailable(int x, int y) {
    	if (isWithinBounds(x, y) && isEmpty(x, y)) {
    		
//        	System.out.println("isAvailable: " + x + ", " + y);
    		
    		return true;
    	} else {
    		return false;
    	}
    }
    public boolean isWithinBounds(int x, int y) {
		int maxX = SettingsSingleton.getInstance().mapCellsX;
		int maxY = SettingsSingleton.getInstance().mapCellsY;
    	if (x < maxX && x  > -1 && 
    		y < maxY && y > -1) {
    		return true;
    	}
    	return false;
    }
    public boolean isEmpty(int x, int y) {
    	
//    	System.out.println("checking isEmpty: " + x + ", " + y);
		
    	return (creatureMap[x][y] == null);
    }
    public boolean hasCreature(int x, int y) {
    	return (!isEmpty(x, y));
    }
    public boolean hasPlant(int x, int y) {
    	Creature c = getCreature(x, y);
    	if (c == null) {
    		return false;
    	} else {
    		if (c.genome.diet == Diet.PHOTOSYNTHESIS) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    }
    public boolean hasAccessibleFood(int x, int y, Diet diet, Creature attacker) {
    	try {
			Creature target = getCreature(x, y);
			return isFood(attacker, target);
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
    }
    public boolean isFood(Creature attacker, Creature target) {
    	if (target == null || 
			target.species.id == attacker.species.id || 
			isTargetSpeciesChildOfAttacker(attacker, target)) {
			return false;
		} else {
			if (target.genome.diet == Diet.PHOTOSYNTHESIS) {
				if (attacker.genome.diet == Diet.HERBIVOROUS && 
					getCombatBasedTargetPriority(attacker, target) > 0) {
					return true;
				}
			} else {
				if (attacker.genome.diet == Diet.CARNIVOROUS && 
					getCombatBasedTargetPriority(attacker, target) > 0) {
					return true;
				}
			}
		}
		return false;
    }
    public boolean isThreat(Creature observer, Creature PossibleThreat) {
    	if (PossibleThreat == null) {
    		return false;
    	}
    	return isFood(PossibleThreat, observer);
    }

    public boolean isMate(Creature initiator, Creature target, boolean initiatorAcceptsOffspeciesMating) {
    	
    	if (target != null &&
    			
			 (target.species.id == initiator.species.id || 
			
			 (initiatorAcceptsOffspeciesMating && 
			  willAcceptOffSpeciesMating(target) && 
			 (target.species.parent.id == initiator.species.id ||
			  target.species.id == initiator.species.parent.id)))) {
    		
			return true;
		}
		return false;
    }
	public boolean willAcceptOffSpeciesMating(Creature creature) {
		if (creature.age > 2*creature.genome.getAgeExpectancy()/3 && 
			creature.numberOfOffspring < creature.genome.getTotalChildren()/3) {
			return true;
		} else {
			return false;
		}
	}
    
    public boolean isTargetSpeciesChildOfAttacker(Creature attacker, Creature target) {
    	for (Species s : attacker.species.children) {
    		if (s.id == target.species.id) {
    			return true;
    		}
    	}
    	return false;
    }
    public int getCombatBasedTargetPriority(Creature attacker, Creature target) {
		int maxHitsTakenByAttacker = (target.health / attacker.genome.getAttack()) + 1;
		int maxDamageExpected = maxHitsTakenByAttacker * target.genome.getDefense();
		if (attacker.health > maxDamageExpected) {
			return 100 - maxDamageExpected;
		} else {
			return -99;
		}
    }
    public void clearCreature(Creature creature) {
    	creatureMap[creature.x][creature.y] = null;
    }
    public void clearCreatureFrom(int x, int y) {
    	creatureMap[x][y] = null;
    }
    public void setCreatureInPoint(Creature creature) {
//    	if (hasCreature(creature.x, creature.y)) {
//    		System.out.println("already another creature in point: " + getCreature(creature.x, creature.y));
//    		System.out.println("OVERLAPPING CREATURES");
//    		int crash = 1/0;
//    	}
    	 creatureMap[creature.x][creature.y] = creature;
    }
    public Creature getCreature(int x, int y) {
    	Creature c = null;
    	try {
			c = creatureMap[x][y];
		} catch (Exception e) {
		}
    	return c;
    }

    
	public void setFocusedCreature(Creature focusedCreature) {
		this.focusedCreature = focusedCreature;
	}

	public void setFocusedSpecies(Species focusedSpecies) {
		this.focusedSpecies = focusedSpecies;
	}
	public Creature getFocusedCreature() {
		return this.focusedCreature;
	}
	public long getFocusedCreatureId() {
		if (focusedCreature == null) {
			return -1;
		}
		return this.focusedCreature.id;
	}

	public Species getFocusedSpecies() {
		return this.focusedSpecies;
	}

	public void clearFocusedCreature() {
//		OutdatedPositionsSingleton.getInstance().addCreaturePosition(focusedCreature.x, focusedCreature.y);
		this.focusedCreature = null;
	}

	public void clearFocusedSpecies() {
		this.focusedSpecies = null;
	}

	public boolean toggleSpeciesHighlight() {
		highlightSpecies = !highlightSpecies;
		return highlightSpecies;
	}
	public boolean toggleSpeciesHighlight(Species species) {
		highlightSpecies = !highlightSpecies;
		if (highlightSpecies && focusedSpecies != null && species.id != focusedSpecies.id) {
			setFocusedSpecies(species); 
			if (focusedCreature != null && focusedCreature.species.id != species.id) {
				clearFocusedCreature();
			}
		}
		return highlightSpecies;
	}
	public boolean getSpeciesHighlight() {
		return highlightSpecies;
	}
	

	public boolean toggleCreatureTracking() {
		trackFocusedCreature = !trackFocusedCreature;
		return trackFocusedCreature;
	}
	public boolean getCreatureTracking() {
		return trackFocusedCreature;
	}


	public void increaseTurnCounter() {
		tick++;
	}
}