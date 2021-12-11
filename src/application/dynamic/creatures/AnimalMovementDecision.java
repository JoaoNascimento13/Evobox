package application.dynamic.creatures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import application.core.RandomizerSingleton;
import javafx.util.Pair;
import application.core.Direction;
import application.core.MapStateSingleton;

public class AnimalMovementDecision extends MovementDecisionStrategy  {
	private static final long serialVersionUID = 1L;
	
	private Creature creature;
	
	public AnimalMovementDecision (Creature creature) {
		this.creature = creature;
	}
	
	

	@Override
	public Direction decideMovementDirection() {

//		OutdatedPositionsSingleton outdatedPositions = OutdatedPositionsSingleton.getInstance();


//		int lastPixelX = SettingsSingleton.getInstance().mapCellsX-1;
//		int lastPixelY = SettingsSingleton.getInstance().mapCellsY-1;;

		if (creature.targetCreature != null && creature.targetCreature.health == 0) {
			creature.targetCreature = null;
		}

		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		if (creature.targetCreature == null || mapState.tick >= creature.nextGoalChange) {
			creature.goal = null;
			creature.targetCreature = null;
		}
		
		RandomizerSingleton randomizer = RandomizerSingleton.getInstance();
		
		boolean canFeed = false;
		if (creature.food < creature.getMaximumFoodStorage()) {
			canFeed = true;
		}
		boolean canReproduce = false;
		if (creature.isFertile && creature.food > creature.feedingStrategy.getMaximumFoodStorage()/3) {
			canReproduce = true;
		}
		
		Creature immediateFood = null;
		Creature immediateThreat = null;
		Creature immediateMate = null;
		
		Creature adjacentCreature;
		for (Direction dir : Direction.randomArrayList(randomizer)) {
			adjacentCreature = mapState.getCreature(creature.x + dir.x, creature.y + dir.y);
			if (adjacentCreature != null) {

				if (canFeed && 
					mapState.isFood(creature, adjacentCreature) && 
					mapState.getCombatBasedTargetPriority(creature, adjacentCreature) > 0) {
					immediateFood = adjacentCreature;
					
				} else if (canReproduce && 
						mapState.isFood(adjacentCreature, creature) && 
						mapState.getCombatBasedTargetPriority(adjacentCreature, creature) > 0) {
						immediateThreat = adjacentCreature;
					
				} else if (mapState.isMate(creature, adjacentCreature, mapState.willAcceptOffSpeciesMating(creature))) {
						immediateMate = adjacentCreature;
				}
			}
		}
		
		if (immediateThreat != null) {
			creature.goal = CreatureGoal.FLEE;
			creature.targetCreature = immediateThreat;
		} else if (immediateMate != null) {
			creature.goal = CreatureGoal.MATE;
			creature.targetCreature = immediateMate;
		} else if (immediateFood != null) {
			creature.goal = CreatureGoal.FEED;
			creature.targetCreature = immediateFood;
		}
		
		
		
		if (creature.targetCreature == null) {
			
			ArrayList<Pair<Creature, Integer>> threatTargets = locateThreats();
			Collections.shuffle(threatTargets, randomizer);
			int maxThreatRate = 0;
			int maxThreatRateInd = 0;
			for (int i = 0; i < threatTargets.size(); i++) {
				if (threatTargets.get(i).getValue() > maxThreatRate) {
					maxThreatRate = threatTargets.get(i).getValue();
					maxThreatRateInd = i;
				}
			}
			
			int maxFoodRate = 0;
			int maxFoodRateInd = 0;
			ArrayList<Pair<Creature, Integer>> foodTargets = null;
			if (canFeed) {
				foodTargets = locateAvailableFood();
				Collections.shuffle(foodTargets, randomizer);
				for (int i = 0; i < foodTargets.size(); i++) {
					if (foodTargets.get(i).getValue() > maxFoodRate) {
						maxFoodRate = foodTargets.get(i).getValue();
						maxFoodRateInd = i;
					}
				}
			}

			int maxMateRate = 0;
			int maxMateRateInd = 0;
			ArrayList<Pair<Creature, Integer>> mateTargets = null;
			if (canReproduce) {
				mateTargets = locateMates();
				Collections.shuffle(mateTargets, randomizer);
				for (int i = 0; i < mateTargets.size(); i++) {
					if (mateTargets.get(i).getValue() > maxMateRate) {
						maxMateRate = mateTargets.get(i).getValue();
						maxMateRateInd = i;
					}
				}
			}
			
			if (maxMateRate + maxFoodRate + maxThreatRate > 0) {
				

				if (2*maxMateRate > maxFoodRate && 2*maxMateRate > maxThreatRate) {
					
					creature.goal = CreatureGoal.MATE;
					creature.targetCreature = mateTargets.get(maxMateRateInd).getKey();
					creature.nextGoalChange = mapState.tick + (20 * creature.genome.getActivationSpeed());
					
					
				} else if (maxFoodRate == 0 || 
						maxThreatRate 
						+ 10*creature.genome.getReactiveness() 
						-  10*creature.genome.getAggression() 
						> maxFoodRate ) {

					creature.goal = CreatureGoal.FLEE;
					creature.targetCreature = threatTargets.get(maxThreatRateInd).getKey();
					creature.nextGoalChange = mapState.tick + (10 * creature.genome.getActivationSpeed());
					
				} else if (maxFoodRate > 0) {

					creature.goal = CreatureGoal.FEED;
					creature.targetCreature = foodTargets.get(maxFoodRateInd).getKey();
					creature.nextGoalChange = mapState.tick + (10 * creature.genome.getActivationSpeed());
				}
				
			}
			

			if (creature.targetCreature == null) {

				//No targets of any kind - move at random

				creature.goal = CreatureGoal.WANDER;
				creature.nextGoalChange = mapState.tick + (5 * creature.genome.getActivationSpeed());
			}
		}
		
		
		int dirX = 0;
		int dirY = 0;

		
		if (creature.targetCreature == null) {
			
			dirX = randomizer.nextInt(3)-1;
			dirY = randomizer.nextInt(3)-1;
			
		} else if (creature.goal == CreatureGoal.FLEE) {
			
			if (creature.targetCreature.x > creature.x) {
				dirX = -1;
			} else if (creature.targetCreature.x < creature.x) {
				dirX = 1;
			} else {
				dirX = 0;
			}
			if (creature.targetCreature.y > creature.y) {
				dirY = -1;
			} else if (creature.targetCreature.y < creature.y) {
				dirY = 1;
			} else {
				dirY = 0;
			}
			
		} else {
			
			if (creature.targetCreature.x > creature.x) {
				dirX = 1;
			} else if (creature.targetCreature.x < creature.x) {
				dirX = -1;
			} else {
				dirX = 0;
			}
			if (creature.targetCreature.y > creature.y) {
				dirY = 1;
			} else if (creature.targetCreature.y < creature.y) {
				dirY = -1;
			} else {
				dirY = 0;
			}
		}
		
		return Direction.getDirection(dirX, dirY);
	}

	
	
	private ArrayList <Pair <Creature, Integer>> locateAvailableFood() {
		
		int maxRange = 2*Math.max(creature.genome.getPerception(), creature.genome.getAggression()) + 2;
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		Diet diet = creature.genome.diet;
		
		if (creature.genome.specificDiet == null) {
			
			//For non-specific diets, we look around for the closest compatible food source.
			
	        ArrayList <Pair <Creature, Integer>> targets = new ArrayList <Pair <Creature, Integer>> ();
	        
	        //Note: We look in concentric squares around the creature's location,
	        //		skipping the first square (dist == 1) because we already checked adjacent tiles.
	        
			for (int dist = 2; dist <= maxRange; dist++) {
				
				for (int x = creature.x - dist; x <= creature.x + dist; x++) {
					if (mapState.hasAccessibleFood(x, dist, diet, creature)) {
						targets.add(new Pair<Creature, Integer>(
								mapState.getCreature(x, dist), 
								getDistanceBasedTargetPriority(dist) + 
								mapState.getCombatBasedTargetPriority(creature, mapState.getCreature(x, dist))
									));
					}
					if (mapState.hasAccessibleFood(x, -dist, diet, creature)) {
						targets.add(new Pair<Creature, Integer>(
								mapState.getCreature(x, -dist), 
								getDistanceBasedTargetPriority(dist) + 
								mapState.getCombatBasedTargetPriority(creature, mapState.getCreature(x, -dist))
									));
					}
				}
				for (int y = creature.y - dist + 1; y <= creature.y + dist - 1; y++) {
					if (mapState.hasAccessibleFood(dist, y, diet, creature)) {
						targets.add(new Pair<Creature, Integer>(
								mapState.getCreature(dist, y), 
								getDistanceBasedTargetPriority(dist) + 
								mapState.getCombatBasedTargetPriority(creature, mapState.getCreature(dist, y))
									));
					}
					if (mapState.hasAccessibleFood(-dist, y, diet, creature)) {
						targets.add(new Pair<Creature, Integer>(
								mapState.getCreature(-dist, y), 
								getDistanceBasedTargetPriority(dist) + 
								mapState.getCombatBasedTargetPriority(creature, mapState.getCreature(-dist, y))
								));
					}
				}
				//TODO: Performance allowing, maybe an intelligence stat would allow creatures to also 
				//		analyze food farther away than the first source found, and choose the best target. 
				if (targets.size() > 0) {
					break;
				}
			}
			return targets;
			
		} else {
			//TODO: For specific diets, we should iterate through the target species' members.
		}
		return null;
	}
	
	

	private ArrayList <Pair <Creature, Integer>> locateThreats() {
		
		int maxRange = 2*Math.max(creature.genome.getPerception(), creature.genome.getReactiveness()) + 2;
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
        ArrayList <Pair <Creature, Integer>> threats = new ArrayList <Pair <Creature, Integer>> ();
        
		for (int dist = 2; dist <= maxRange; dist++) {
			
			for (int x = creature.x - dist; x <= creature.x + dist; x++) {
				
				if (
						
					mapState.isThreat(creature, mapState.getCreature(x, dist))) {
					threats.add(new Pair<Creature, Integer>(
							mapState.getCreature(x, dist), 
							getDistanceBasedTargetPriority(dist) + 
							mapState.getCombatBasedTargetPriority(mapState.getCreature(x, dist), creature)
								));
				}
				if (mapState.isThreat(creature, mapState.getCreature(x, -dist))) {
					threats.add(new Pair<Creature, Integer>(
							mapState.getCreature(x, -dist), 
							getDistanceBasedTargetPriority(dist) + 
							mapState.getCombatBasedTargetPriority(mapState.getCreature(x, -dist), creature)
								));
				}
			}
			for (int y = creature.y - dist + 1; y <= creature.y + dist - 1; y++) {
				if (mapState.isThreat(creature, mapState.getCreature(dist, y))) {
					threats.add(new Pair<Creature, Integer>(
							mapState.getCreature(dist, y), 
							getDistanceBasedTargetPriority(dist) + 
							mapState.getCombatBasedTargetPriority(mapState.getCreature(dist, y), creature)
								));
				}
				if (mapState.isThreat(creature, mapState.getCreature(-dist, y))) {
					threats.add(new Pair<Creature, Integer>(
							mapState.getCreature(-dist, y), 
							getDistanceBasedTargetPriority(dist) + 
							mapState.getCombatBasedTargetPriority(mapState.getCreature(-dist, y), creature)
							));
				}
			}
			//TODO: Performance allowing, maybe an intelligence stat would allow creatures to also 
			//		analyze threats farther away than the first source found, and choose the biggest threat. 
			if (threats.size() > 0) {
				break;
			}
		}
		return threats;
		
	}
	
	

	private ArrayList <Pair <Creature, Integer>> locateMates() {

		int maxRange = 2*creature.genome.getPerception() + 4;
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		boolean willAcceptOffSpeciesMating = mapState.willAcceptOffSpeciesMating(creature);
		
		
        ArrayList <Pair <Creature, Integer>> mates = new ArrayList <Pair <Creature, Integer>> ();
        
		for (int dist = 2; dist <= maxRange; dist++) {
			
			for (int x = creature.x - dist; x <= creature.x + dist; x++) {
				
				if (
					mapState.isMate(creature, mapState.getCreature(x, dist), willAcceptOffSpeciesMating)) {
					mates.add(new Pair<Creature, Integer>(
							mapState.getCreature(x, dist), 
							getDistanceBasedTargetPriority(dist)
								));
				}
				if (mapState.isMate(creature, mapState.getCreature(x, -dist), willAcceptOffSpeciesMating)) {
					mates.add(new Pair<Creature, Integer>(
							mapState.getCreature(x, -dist), 
							getDistanceBasedTargetPriority(dist)
								));
				}
			}
			for (int y = creature.y - dist + 1; y <= creature.y + dist - 1; y++) {
				if (mapState.isMate(creature, mapState.getCreature(dist, y), willAcceptOffSpeciesMating)) {
					mates.add(new Pair<Creature, Integer>(
							mapState.getCreature(dist, y), 
							getDistanceBasedTargetPriority(dist)
								));
				}
				if (mapState.isMate(creature, mapState.getCreature(-dist, y), willAcceptOffSpeciesMating)) {
					mates.add(new Pair<Creature, Integer>(
							mapState.getCreature(-dist, y), 
							getDistanceBasedTargetPriority(dist)
							));
				}
			}
			//TODO: Performance allowing, maybe an intelligence stat would allow creatures to also 
			//		analyze threats farther away than the first source found, and choose the biggest threat. 
			if (mates.size() > 0) {
				break;
			}
		}
		return mates;
	}
	
	
	
	
	public int getDistanceBasedTargetPriority(int distance) {
		return (100-10*distance);
	}
	
}
