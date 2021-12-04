package application.core;

import java.awt.Point;
import java.util.ArrayList;

import application.dynamic.Creature;
import application.dynamic.Diet;
import application.dynamic.FlowGenerator;
import application.dynamic.Species;
import application.gui.SimulatorController;

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

	
	private static final MapStateSingleton mapState = new MapStateSingleton();

	public Creature focusedCreature;
	
	
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
    	creature.numberInSpecies = creature.species.totalMembers;
    }
    

    public void registerSpecies(Species species) {
    	species.id = nextSpeciesId;
    	nextSpeciesId++;
    	activeSpecies.add(species);
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
    	for (long id : deadCreaturesToRemoveIds) {
    		for (int i = 0; i < activeCreatures.size(); i++) {
        		if (activeCreatures.get(i).id == id) {
        			oldCreaturePositionsX.add(activeCreatures.get(i).x);
        			oldCreaturePositionsY.add(activeCreatures.get(i).y);
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




}