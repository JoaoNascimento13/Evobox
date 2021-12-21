package application.dynamic.creatures;

import java.util.ArrayList;
import java.util.Collections;

import application.core.RandomizerSingleton;
import application.core.SettingsSingleton;
import javafx.util.Pair;
import application.core.Direction;
import application.core.MapStateSingleton;

public class AnimalMovementDecision extends MovementDecisionStrategy  {
	private static final long serialVersionUID = 1L;
	
	private Creature creature;
	
	public AnimalMovementDecision (Creature creature) {
		this.creature = creature;
	}
	
	
	private static int maxRangeBase = 10;
	

	@Override
	public Direction decideMovementDirection() {

		
		if (creature.targetCreature != null && creature.targetCreature.health == 0) {
			creature.unregisterAttackerAndTarget();
		}

		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		if (creature.targetCreature == null || mapState.turn >= creature.nextGoalChange) {
			creature.goal = null;
			creature.unregisterAttackerAndTarget();
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
//		Creature immediateThreat = null;
		Creature immediateMate = null;
		
		Creature adjacentCreature;
		for (Direction dir : Direction.randomArrayList(randomizer)) {
			adjacentCreature = mapState.getCreature(creature.x + dir.x, creature.y + dir.y);
			if (adjacentCreature != null) {

				if (canFeed && 
					mapState.isFood(creature, adjacentCreature) && 
					mapState.getCombatBasedTargetPriority(creature, adjacentCreature) > 0) {
					immediateFood = adjacentCreature;
					
//				} else if (
//						mapState.isFood(adjacentCreature, creature) && 
//						mapState.getCombatBasedTargetPriority(adjacentCreature, creature) > 0) {
//						immediateThreat = adjacentCreature;
					
				} else if (canReproduce && 
						mapState.acceptsMating(creature, adjacentCreature, mapState.willAcceptOffSpeciesMating(creature))) {
						immediateMate = adjacentCreature;
				}
			}
		}
		
//		if (immediateThreat != null) {
//			creature.goal = CreatureGoal.FLEE;
//			creature.targetCreature = immediateThreat;
//		} else 
			
		if (immediateMate != null) {
			creature.goal = CreatureGoal.MATE;
			creature.targetCreature = immediateMate;
		} else if (immediateFood != null) {
			creature.goal = CreatureGoal.FEED;
			creature.registerAttackerAndTarget(immediateFood);
		}
		
		
		
		if (creature.targetCreature == null) {

			int maxThreatRate = 0;
			int maxThreatRateInd = 0;
			ArrayList<Pair<Creature, Integer>> threatTargets = locateThreats();
			if (threatTargets != null) {
				Collections.shuffle(threatTargets, randomizer);
				for (int i = 0; i < threatTargets.size(); i++) {
					if (threatTargets.get(i).getValue() > maxThreatRate) {
						maxThreatRate = threatTargets.get(i).getValue();
						maxThreatRateInd = i;
					}
				}
			}
			
			int maxFoodRate = 0;
			int maxFoodRateInd = 0;
			ArrayList<Pair<Creature, Integer>> foodTargets = null;
//			if (canFeed) {
				foodTargets = locateAvailableFood();
				Collections.shuffle(foodTargets, randomizer);
				for (int i = 0; i < foodTargets.size(); i++) {
					if (foodTargets.get(i).getValue() > maxFoodRate) {
						maxFoodRate = foodTargets.get(i).getValue();
						maxFoodRateInd = i;
					}
				}
//			}

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
					creature.nextGoalChange = mapState.turn + (20 * creature.genome.getActivationSpeed());
					
					
				} else if (
						((!canFeed || maxFoodRate == 0) && maxThreatRate > 0) 
						|| 
						maxThreatRate 
						+ 10*creature.genome.getReactiveness() 
						-  10*creature.genome.getAggression() 
						> maxFoodRate ) {

					creature.goal = CreatureGoal.FLEE;
					creature.targetCreature = threatTargets.get(maxThreatRateInd).getKey();
					creature.nextGoalChange = mapState.turn + (10 * creature.genome.getActivationSpeed());
					
				} else if (maxFoodRate > 0) {
					
					if (canFeed) {
						creature.goal = CreatureGoal.FEED;
//						System.out.println("CreatureGoal.FEED"); 
					} else {
						creature.goal = CreatureGoal.WANDER;
//						System.out.println("CreatureGoal.WANDER"); 
						//Note: If the creature can't feed, she'll get the 'Wander' goal, 
						//      but will still move towards food. 
					}
//					System.out.println("creature at: " + creature.x + ", " + creature.y); 
//					System.out.println("target at: " + foodTargets.get(maxFoodRateInd).getKey().x + ", " + foodTargets.get(maxFoodRateInd).getKey().y); 
					creature.registerAttackerAndTarget(foodTargets.get(maxFoodRateInd).getKey());
					creature.nextGoalChange = mapState.turn + (10 * creature.genome.getActivationSpeed());
				}
			}
		}
		
		
		int dirX = 0;
		int dirY = 0;
		
		if (creature.targetCreature == null) {

			//No targets of any kind - move at random

			creature.goal = CreatureGoal.WANDER;
			creature.nextGoalChange = mapState.turn + (5 * creature.genome.getActivationSpeed());
			
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
			
			if (creature.targetCreature.x > creature.x + 1) {
				dirX = 1;
			} else if (creature.targetCreature.x < creature.x - 1) {
				dirX = -1;
			} else {
				dirX = 0;
			}
			if (creature.targetCreature.y > creature.y + 1) {
				dirY = 1;
			} else if (creature.targetCreature.y < creature.y - 1) {
				dirY = -1;
			} else {
				dirY = 0;
			}
		}

//		if (creature.genome.diet == Diet.HERBIVOROUS && creature.goal == CreatureGoal.MATE) {
//			System.out.println("Animal " + creature + " looking for mate.");
//			System.out.println("food: " + creature.food);
//			System.out.println("maxFood: " + creature.getMaximumFoodStorage());
//			System.out.println("isFertile: " + creature.isFertile);
//			System.out.println("cooldown: " + creature.getReproductionCooldown());
//			System.out.println("isFertile: " + creature.isFertile);
//			System.out.println("willAcceptOffSpeciesMating: " + mapState.willAcceptOffSpeciesMating(creature));
//			System.out.println("target: " + creature.targetCreature);
//			System.out.println("target species: " + creature.targetCreature.species.name);
//			System.out.println();
//		}


		
//		if (creature.genome.diet == Diet.HERBIVOROUS && 
//			creature.goal == CreatureGoal.MATE &&	
//			creature.targetCreature.species.id == creature.species.id) {
//			System.out.println(creature.species.name + " " + creature.id + 
//				" is trying to mate with " + creature.targetCreature.species.name + " " + creature.targetCreature.id + "!");
//			System.out.println();
//		}

//		System.out.println("movement dir: " + Direction.getDirection(dirX, dirY)); 
//		System.out.println();
		
		return Direction.getDirection(dirX, dirY);
	}

	
	
	private ArrayList <Pair <Creature, Integer>> locateAvailableFood() {
		
//		int maxRange = 4*Math.max(creature.genome.getPerception(), creature.genome.getAggression()) + 4;
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		Diet diet = creature.genome.diet;

		int maxRange = maxRangeBase * diet.getFoodDetectionCoef();
		
		int maxRangeExtensionFromNonOptimal = maxRange/2;
		
		int priority;
		int distToNonOptimalFood = -1;
		boolean optimalTargetFound = false;
		boolean nonOptimalTargetFound = false;
		int perpendicularDistA;
		int perpendicularDistB;
		
		if (creature.genome.specificDiet == null) {
			
			//For non-specific diets, we look around for the closest compatible food source.
			
	        ArrayList <Pair <Creature, Integer>> targets = new ArrayList <Pair <Creature, Integer>> ();
	        
	        //Note: We look in concentric squares around the creature's location,
	        //		skipping the first square (dist == 1) because we already checked adjacent tiles.
	        
			for (int dist = 2; dist <= maxRange; dist++) {

				perpendicularDistA = creature.y - dist;
				perpendicularDistB = creature.y + dist;
				for (int x = creature.x - dist; x <= creature.x + dist; x++) {
					if (mapState.hasAccessibleFood(x, perpendicularDistA, diet, creature)) {
						priority = getFoodPriority(x, perpendicularDistA, dist);
						if (priority > 0) {
							if (isOptimalTarget(x, perpendicularDistA)) {
								priority += 100000;
								optimalTargetFound = true;
							} else {
								nonOptimalTargetFound = true;
							}
							targets.add(new Pair<Creature, Integer>(
									mapState.getCreature(x, perpendicularDistA), priority));
						}
					}
					if (mapState.hasAccessibleFood(x, perpendicularDistB, diet, creature)) {
						priority = getFoodPriority(x, perpendicularDistB, dist);
						if (priority > 0) {
							if (isOptimalTarget(x, perpendicularDistB)) {
								priority += 100000;
								optimalTargetFound = true;
							} else {
								nonOptimalTargetFound = true;
							}
							targets.add(new Pair<Creature, Integer>(
									mapState.getCreature(x, perpendicularDistB), priority));
						}
					}
				}
				perpendicularDistA = creature.x - dist;
				perpendicularDistB = creature.x + dist;
				for (int y = creature.y - dist + 1; y <= creature.y + dist - 1; y++) {
					if (mapState.hasAccessibleFood(perpendicularDistA, y, diet, creature)) {
						priority = getFoodPriority(perpendicularDistA, y, dist);
						if (priority > 0) {
							if (isOptimalTarget(perpendicularDistA, y)) {
								priority += 100000;
								optimalTargetFound = true;
							} else {
								nonOptimalTargetFound = true;
							}
							targets.add(new Pair<Creature, Integer>(
									mapState.getCreature(perpendicularDistA, y), priority));
						}
					}
					if (mapState.hasAccessibleFood(perpendicularDistB, y, diet, creature)) {
						priority = getFoodPriority(perpendicularDistB, y, dist);
						if (priority > 0) {
							if (isOptimalTarget(perpendicularDistB, y)) {
								priority += 100000;
								optimalTargetFound = true;
							} else {
								nonOptimalTargetFound = true;
							}
							targets.add(new Pair<Creature, Integer>(
									mapState.getCreature(perpendicularDistB, y), priority));
						}
					}
				}
				if (optimalTargetFound) {
					break;
				}
				if (nonOptimalTargetFound) {
					if (distToNonOptimalFood > maxRangeExtensionFromNonOptimal) {
						break;
					}
					distToNonOptimalFood++;
				}
//				if (targets.size() > 0) {
//					if (tilesAfterFirstChecked > tilesAfterFirstToCheck) {
//						break;
//					}
//					tilesAfterFirstChecked ++;
//				}
			}
			return targets;
			
		} else {
			//TODO: For specific diets, we should iterate through the target species' members.
		}
		return null;
	}
	
	
	private int getFoodPriority(int x, int y, int dist) {
		return
		getDistanceBasedTargetPriority(dist) + 
		MapStateSingleton.getInstance().getCombatBasedTargetPriority(creature, MapStateSingleton.getInstance().getCreature(x, y)) +
		getMapCenterPriorityMod(x, y);
	}

	private ArrayList <Pair <Creature, Integer>> locateThreats() {
		
		if (creature.threats.size() > 0) {
			MapStateSingleton mapState = MapStateSingleton.getInstance();
			ArrayList <Pair <Creature, Integer>> threats = new ArrayList <Pair <Creature, Integer>> ();
			for (Creature c : creature.threats) {
				if (mapState.isTargetDetectedByObserver(creature, c)) {
					int combatThreat = mapState.getCombatBasedTargetPriority(c, creature);
					if (combatThreat > 0) {
						int distX = Math.abs(creature.x - c.x);
						int distY = Math.abs(creature.y - c.y);
						int distanceThreat = Math.max(distX, distY) + Math.abs(distX - distY);

						threats.add(new Pair<Creature, Integer>(c, distanceThreat + combatThreat));
					}
				}
			}
			
		}
		return null;
	}
	
//	private ArrayList <Pair <Creature, Integer>> locateThreats() {
//		
////		int maxRange = 2*Math.max(creature.genome.getPerception(), creature.genome.getReactiveness()) + 2;
//		
//		MapStateSingleton mapState = MapStateSingleton.getInstance();
//		
//        ArrayList <Pair <Creature, Integer>> threats = new ArrayList <Pair <Creature, Integer>> ();
//        int perpendicularDistA;
//        int perpendicularDistB;
//		for (int dist = 2; dist <= maxRange/2; dist++) {
//
//			perpendicularDistA = creature.y - dist;
//			perpendicularDistB = creature.y + dist;
//			for (int x = creature.x - dist; x <= creature.x + dist; x++) {
//				if (mapState.isThreat(creature, mapState.getCreature(x, perpendicularDistA))) {
//					threats.add(new Pair<Creature, Integer>(
//							mapState.getCreature(x, perpendicularDistA), 
//							getDistanceBasedTargetPriority(dist)-maxRange/2 + 
//							mapState.getCombatBasedTargetPriority(mapState.getCreature(x, perpendicularDistA), creature)
//								));
//				}
//				if (mapState.isThreat(creature, mapState.getCreature(x, perpendicularDistB))) {
//					threats.add(new Pair<Creature, Integer>(
//							mapState.getCreature(x, perpendicularDistB), 
//							getDistanceBasedTargetPriority(dist)-maxRange/2 + 
//							mapState.getCombatBasedTargetPriority(mapState.getCreature(x, perpendicularDistB), creature)
//								));
//				}
//			}
//			perpendicularDistA = creature.x - dist;
//			perpendicularDistB = creature.x + dist;
//			for (int y = creature.y - dist + 1; y <= creature.y + dist - 1; y++) {
//				if (mapState.isThreat(creature, mapState.getCreature(perpendicularDistA, y))) {
//					threats.add(new Pair<Creature, Integer>(
//							mapState.getCreature(perpendicularDistA, y), 
//							getDistanceBasedTargetPriority(dist)-maxRange/2 + 
//							mapState.getCombatBasedTargetPriority(mapState.getCreature(perpendicularDistA, y), creature)
//								));
//				}
//				if (mapState.isThreat(creature, mapState.getCreature(perpendicularDistB, y))) {
//					threats.add(new Pair<Creature, Integer>(
//							mapState.getCreature(perpendicularDistB, y), 
//							getDistanceBasedTargetPriority(dist)-maxRange/2 + 
//							mapState.getCombatBasedTargetPriority(mapState.getCreature(perpendicularDistB, y), creature)
//							));
//				}
//			}
//			//TODO: Performance allowing, maybe an intelligence stat would allow creatures to also 
//			//		analyze threats farther away than the first source found, and choose the biggest threat. 
//			if (threats.size() > 0) {
//				break;
//			}
//		}
//		return threats;
//		
//	}
	
	

	private ArrayList <Pair <Creature, Integer>> locateMates() {

//		int maxRange = 4*creature.genome.getPerception() + 4;

		int tilesAfterFirstToCheck = 4;
		int tilesAfterFirstChecked = 0;
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		boolean willAcceptOffSpeciesMating = mapState.willAcceptOffSpeciesMating(creature);

		int maxRange = maxRangeBase * creature.genome.diet.getFoodDetectionCoef();
		
        ArrayList <Pair <Creature, Integer>> mates = new ArrayList <Pair <Creature, Integer>> ();
        int perpendicularDistA;
        int perpendicularDistB;
		for (int dist = 2; dist <= maxRange; dist++) {

			perpendicularDistA = creature.y - dist;
			perpendicularDistB = creature.y + dist;
			for (int x = creature.x - dist; x <= creature.x + dist; x++) {
				
				if (
					mapState.acceptsMating(creature, mapState.getCreature(x, perpendicularDistA), willAcceptOffSpeciesMating)) {
					mates.add(new Pair<Creature, Integer>(
							mapState.getCreature(x, perpendicularDistA), 
							getDistanceBasedTargetPriority(dist) +
							getMapCenterPriorityMod(x, perpendicularDistA)
								));
				}
				if (mapState.acceptsMating(creature, mapState.getCreature(x, perpendicularDistB), willAcceptOffSpeciesMating)) {
					mates.add(new Pair<Creature, Integer>(
							mapState.getCreature(x, perpendicularDistB), 
							getDistanceBasedTargetPriority(dist) +
							getMapCenterPriorityMod(x, perpendicularDistB)
								));
				}
			}
			perpendicularDistA = creature.x - dist;
			perpendicularDistB = creature.x + dist;
			for (int y = creature.y - dist + 1; y <= creature.y + dist - 1; y++) {
				if (mapState.acceptsMating(creature, mapState.getCreature(perpendicularDistA, y), willAcceptOffSpeciesMating)) {
					mates.add(new Pair<Creature, Integer>(
							mapState.getCreature(perpendicularDistA, y), 
							getDistanceBasedTargetPriority(dist) +
							getMapCenterPriorityMod(perpendicularDistA, y)
								));
				}
				if (mapState.acceptsMating(creature, mapState.getCreature(perpendicularDistB, y), willAcceptOffSpeciesMating)) {
					mates.add(new Pair<Creature, Integer>(
							mapState.getCreature(perpendicularDistB, y), 
							getDistanceBasedTargetPriority(dist) +
							getMapCenterPriorityMod(perpendicularDistB, y)
							));
				}
			}
			if (mates.size() > 0) {
				if (tilesAfterFirstChecked > tilesAfterFirstToCheck) {
					break;
				}
				tilesAfterFirstChecked ++;
			}
		}
		return mates;
	}
	


	private int getMapCenterPriorityMod(int targetX, int targetY) {
		int mod = 0;
		if ((targetX < creature.x && creature.x < SettingsSingleton.getInstance().mapCellsX/5) || 
			(targetX > creature.x && creature.x > 3*SettingsSingleton.getInstance().mapCellsX/5)){
			
		} else {
			mod += 50;
		}
		if ((targetY < creature.y && creature.y < SettingsSingleton.getInstance().mapCellsY/5) || 
			(targetY > creature.y && creature.y > 4*SettingsSingleton.getInstance().mapCellsY/5)){
				
		} else {
			mod += 50;
		}
		return mod;
	}


	private boolean isOptimalTarget(int x, int y) {
		return (isOptimalBasedOnMapPosition(x, y) && isOptimalBasedOnCompetingAttackers(x, y));
	}
	
	
	private boolean isOptimalBasedOnMapPosition(int x, int y) {
		
		if ((x < creature.x && creature.x < SettingsSingleton.getInstance().mapCellsX/5) || 
			(x > creature.x && creature.x > 4*SettingsSingleton.getInstance().mapCellsX/5)){
			return false;
			
		} else if ((x == creature.x && creature.x < SettingsSingleton.getInstance().mapCellsX/8) || 
				   (x == creature.x && creature.x > 7*SettingsSingleton.getInstance().mapCellsX/8)){
					
			return false;
		}
		
		if ((y < creature.y && creature.y < SettingsSingleton.getInstance().mapCellsY/5) || 
			(y > creature.y && creature.y > 4*SettingsSingleton.getInstance().mapCellsY/5)){
			return false;
			
		} else if ((y == creature.y && creature.y < SettingsSingleton.getInstance().mapCellsY/8) || 
				   (y == creature.y && creature.y > 7*SettingsSingleton.getInstance().mapCellsY/8)){
					
			return false;
		}
		return true;
	}

	private boolean isOptimalBasedOnCompetingAttackers(int x, int y) {
		for (Creature c : MapStateSingleton.getInstance().getCreature(x, y).threats) {
			if (c.id != creature.id) {
				return false;
			}
		}
		return true;
	}
	
	
	public int getDistanceBasedTargetPriority(int distance) {
		int maxRange = maxRangeBase * creature.genome.diet.getFoodDetectionCoef();
		return 10*(maxRange-distance);
	}

	@Override
	public CreatureGoal getStartingGoal() {
		return CreatureGoal.WANDER;
	}
	
}
