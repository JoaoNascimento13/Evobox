package application.dynamic.creatures;

import java.awt.Point;
import java.util.ArrayList;

import application.core.RandomizerSingleton;
import application.dynamic.factories.SexualReproductionCreatureFactory;
import application.core.Direction;
import application.core.MapStateSingleton;

public class SexualReproduction extends ReproductionStrategy  {
	private static final long serialVersionUID = 1L;

	private Creature creature;
	
	private int reproductionCooldown;
	
	public SexualReproduction (Creature creature) {
		this.creature = creature;
		startReproductionCooldown();
	}

	
	
	@Override
	public void exposeToReproduction(long tick) {
		
		updateFertility();
		
		if (creature.isFertile) {

			RandomizerSingleton randomizer = RandomizerSingleton.getInstance();
			
			Direction partnerDir = getReproductionDirection(randomizer);
			
			if (partnerDir != null) {

				ArrayList<Point> spawnPoints = getSpawnPoints(partnerDir, randomizer);
				
				if (spawnPoints.size() > 0) {

					Creature partner = MapStateSingleton.getInstance().getCreature(creature.x+partnerDir.x, creature.y+partnerDir.y);
					
					reproduce(tick, partner, spawnPoints);
				}
			}
		}
	}
	

	public void updateFertility () {
		if (reproductionCooldown > 0) {
			reproductionCooldown -= creature.ticksPerTurn();
		}
		if (!creature.isFertile) {
			if (reproductionCooldown <= 0 &&
				creature.food > (2*creature.feedingStrategy.getMaximumFoodStorage())/3) {
				creature.isFertile = true;
			}
		}
	}
	
	public void reproduce (long tick, Creature partner, ArrayList<Point> spawnPoints) {
		
		SexualReproductionCreatureFactory factory = new SexualReproductionCreatureFactory();
		
		factory.setParents(creature, partner);
		
		for (Point p : spawnPoints) {

			factory.setSpawnPoint(p);
			
			Creature spawn = factory.createCreature(tick);
			
			MapStateSingleton.getInstance().queueCreatureRegister(spawn);
			
			MapStateSingleton.getInstance().setCreatureInPoint(spawn);
			
		}
		
		creature.isFertile = false;
		creature.food -= creature.feedingStrategy.getMaximumFoodStorage()/3;
		startReproductionCooldown();

		partner.isFertile = false;
		partner.food -= partner.feedingStrategy.getMaximumFoodStorage()/3;
		partner.reproductionStrategy.startReproductionCooldown();
	}
	
	
	
	public Direction getReproductionDirection(RandomizerSingleton randomizer) {
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		ArrayList<Direction> RandomDirs = Direction.randomArrayList(randomizer);
		Direction reproductionDir = null;
		Creature partner = null;
		for (Direction d : RandomDirs) {
			
			 partner = mapState.getCreature(creature.x+d.x, creature.y+d.y);
			
			if (partner != null &&
				partner.isFertile && 
				creature.species.isDirectRelative(partner.species)) {
				
				reproductionDir = d;
				break;
			}
		}
		return reproductionDir;
	}
	

	public ArrayList<Point> getSpawnPoints(Direction partnerDir, RandomizerSingleton randomizer) {
		
		int childrenToSpawn = Math.max(1, creature.genome.getChildrenPerBirth() + randomizer.nextInt(3) - 1);
		

		ArrayList<Direction> RandomDirs = Direction.randomArrayList(randomizer);
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		ArrayList<Point> spawnPoints = new ArrayList<Point>();
		
		for (Direction d : RandomDirs) {
			if (mapState.isAvailable(creature.x+d.x, creature.y+d.y)) {
				
				spawnPoints.add(new Point(creature.x+d.x, creature.y+d.y));
				childrenToSpawn--;
				if (childrenToSpawn == 0) {
					break;
				}
			}
		}
		if (childrenToSpawn > 0) {
			 secondLoop:
			for (Direction d : RandomDirs) {
				if (mapState.isAvailable(creature.x+partnerDir.x+d.x, creature.y+partnerDir.y+d.y)) {
					
					//This check prevents this loop from adding a point already added by the previous loop.
					for (Point p : spawnPoints) {
						if (p.x == creature.x+partnerDir.x+d.x && p.y == creature.y+partnerDir.y+d.y) {
							continue secondLoop;
						}
					}
					
					spawnPoints.add(new Point(creature.x+partnerDir.x+d.x, creature.y+partnerDir.y+d.y));
					childrenToSpawn--;
					if (childrenToSpawn == 0) {
						break;
					}
				}
			}
		}
		
		return spawnPoints;
	}
	
	
	
	
	
	public void startReproductionCooldown() {
		
		int expectedReproductionEvents = creature.genome.getTotalChildren() / creature.genome.getChildrenPerBirth();
		
		reproductionCooldown = creature.genome.getMaxAge() / expectedReproductionEvents;
	}
	
	
	
	
	
}