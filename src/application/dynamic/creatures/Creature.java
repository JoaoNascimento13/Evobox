package application.dynamic.creatures;

import java.io.Serializable;
import java.util.ArrayList;

import application.core.RandomizerSingleton;
import application.core.Direction;
import application.core.MapStateSingleton;
import application.core.OutdatedPositionsSingleton;
import application.gui.SceneManagerSingleton;

public class Creature implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	public int x;
	public int y;
	
	private int obstacleAvoidanceRotation; //1 to rotate clockwise, -1 to rotate counterclockwise

	public ArrayList<Creature> threats;
	
	public long nextActivation;
	
	public Genome genome;
	public Species species;
	
	public int age;
	public int food;
	public int health;
	public boolean isFertile;
	public int numberOfOffspring = 0;
	
	public long id;
	public long numberInSpecies;
	
	public boolean mutated = false;
	
	
	public Creature targetCreature;
	public CreatureGoal goal;
	public long nextGoalChange;
	
	public Creature (int x, int y) {
		this.x = x;
		this.y = y;
		threats = new ArrayList<Creature>(); 
	}
	
	
	public MovementDecisionStrategy movementDecisionStrategy;

	public FeedingStrategy feedingStrategy;

	public ReproductionStrategy reproductionStrategy;

	
	
	
	public void exposeToActivation(long currentTick) {
		if (this.nextActivation == currentTick) {
			act(currentTick);
			nextActivation = currentTick + ticksPerTurn();
		}
	}
	

	public void act(long tick) {

//		System.out.println();
//		System.out.println("activating creature: " + this);
		
		age();
		useFood();
		
		if(exposeToDeath()) {
			return;
		}
		
		move();
		
		feed();
		
		reproduce(tick);
		
//		System.out.println("creature finished: " + this);
	}
	
	
	public int ticksPerTurn() {
		return (11-this.genome.getActivationSpeed());
	}


	public boolean isActive() {
		if (goal == null || goal.isActive()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void age() {
		if (isActive()) {
			this.age += ticksPerTurn();
		}
	}
	public void useFood() {
		if (isActive()) {
			this.food -= ticksPerTurn() * 
					 genome.getFoodNeededPerTick() * 
					 feedingStrategy.getFoodNeededPerTickModifier();
		}
	}


	public boolean exposeToDeath() {
		if (exposeToOldAgeDeath()) {
			return true;
		}
		if (exposeToStarvationDeath()) {
			return true;
		}
		return false;
	}
	
	
	public boolean exposeToOldAgeDeath() {

		int chanceOfDeath = 0;
		
		if (this.age > (6*this.genome.getMaxAge())/5) {
			
			chanceOfDeath = 25;
			
		} else if (this.age > this.genome.getMaxAge()) {

			chanceOfDeath = 5;
		
		} else if (this.age > (4*this.genome.getMaxAge())/5) {
			
			chanceOfDeath = 1;
		}

		RandomizerSingleton randomizer = RandomizerSingleton.getInstance();
		
		if (randomizer.nextInt(100) < chanceOfDeath) {
//			System.out.println("- 1 old age");
			die();
			return true;
		} else {
			return false;
		}
	}
	

	public boolean exposeToStarvationDeath() {
		
		if (food <= 0) {
//			System.out.println("- 1 starved");
			die();
			return true;
		} else {
			return false;
		}
	}
	

	public void die() {
		this.health = 0;
		OutdatedPositionsSingleton.getInstance().addCreaturePosition(x, y);
		MapStateSingleton.getInstance().clearCreature(this);
		MapStateSingleton.getInstance().queueCreatureUnregister(this);
		this.species.currentMembers--;
		if (this.species.currentMembers <= 0) {
			System.out.println(species.name + " went extinct!");
			System.out.println();
			MapStateSingleton.getInstance().unregisterSpecies(species);
			SceneManagerSingleton.getInstance().simulatorController.removeSpeciesFromOverview(species);
		}
		
		if (mutated) {
			this.species.currentMutatedMembers--;
		}
	}
	
	
	public void move() {
		
		if (isActive()) {

			Direction dir = this.movementDecisionStrategy.decideMovementDirection();

			moveInDir(dir);
			
		} else {

			Direction dir = ((PlantMovementDecision) this.movementDecisionStrategy).decideSproutingDirection();
			if (dir == null) {
				//Do nothing for now, try again next activation.
			} else {
//				System.out.println("Creature sprouted!" + this);
				sprout(dir);
			}
			
		}
	}



	private void feed() {
		
		this.feedingStrategy.exposeToFeeding();
		
	}

	

	public void reproduce(long tick) {
		
		this.reproductionStrategy.exposeToReproduction(tick);
		
	}

	
	
	
	
	public void sprout(Direction dir) {

		this.x += dir.x;
		this.y += dir.y;

		MapStateSingleton mapState = MapStateSingleton.getInstance();
		mapState.setCreatureInPoint(this);
		
		mapState.addCreatureToSpecies(this);
		
		goal = CreatureGoal.FLOAT;
	}
	
	
	
	public void moveInDir(Direction mainDir) {

		if (mainDir == Direction.NONE) {
			return;
		}
		
		RandomizerSingleton randomizer = RandomizerSingleton.getInstance();
		
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		Direction movementDir = mainDir;
		
		if (!mapState.isAvailable(this.x+movementDir.x, this.y+movementDir.y)) {
			
			
			if (obstacleAvoidanceRotation == 0) {
				obstacleAvoidanceRotation = (2*randomizer.nextInt(2))-1;
			}
			
			if (obstacleAvoidanceRotation == 1) {
				
				movementDir = movementDir.clockwise();
				
				if (!mapState.isAvailable(this.x+movementDir.x, this.y+movementDir.y)) {
					
					movementDir = movementDir.clockwise();
					
					if (!mapState.isAvailable(this.x+movementDir.x, this.y+movementDir.y)) {
						
						//If we can't move in a perpendicular direction to the original, 
						//we'll stop on this turn and move on the opposite direction on the next
						
						movementDir = Direction.NONE;
						obstacleAvoidanceRotation = -1;
					}
				}

			} else if (obstacleAvoidanceRotation == -1) {

				movementDir = movementDir.counterClockwise();
				
				if (!mapState.isAvailable(this.x+movementDir.x, this.y+movementDir.y)) {
					
					
					movementDir = movementDir.counterClockwise();
					
					if (!mapState.isAvailable(this.x+movementDir.x, this.y+movementDir.y)) {
						
						//If we can't move in a perpendicular direction to the original, 
						//we'll stop on this turn and move on the opposite direction on the next
						
						movementDir = Direction.NONE;
						obstacleAvoidanceRotation = 1;
					}
				}
			}
			
		} else {
			obstacleAvoidanceRotation = 0;
		}
		
		if (movementDir != Direction.NONE) {
			
			OutdatedPositionsSingleton.getInstance().addCreaturePosition(x, y);
			mapState.clearCreature(this);
			
			this.x += movementDir.x;
			this.y += movementDir.y;
			
			mapState.setCreatureInPoint(this);
			
			if (mapState.getCreatureTracking() && mapState.getFocusedCreatureId() == this.id) {
				SceneManagerSingleton.getInstance().renderer.activeMapScrollPane.moveInDir(movementDir);
			}
		}
	}
	
	
	
	
	
	

	public void setNextActivation(long nextActivation) {
		this.nextActivation = nextActivation;
	}

	public void setSpecies(Species species) {
		this.species = species;
	}
	public void setGenome(Genome genome) {
		this.genome = genome;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setFood(int food) {
		this.food = food;
	}
	public void setFullHealth() {
		this.health = this.genome.getMaxHealth();
	}
	
	public void setMovementDecisionStrategy(MovementDecisionStrategy moveStrategy) {
		this.movementDecisionStrategy = moveStrategy;
		this.goal = movementDecisionStrategy.getStartingGoal();
	}
	public void setFeedingStrategy(FeedingStrategy feedingStrategy) {
		this.feedingStrategy = feedingStrategy;
	}
	public void setReproductionStrategy(ReproductionStrategy reproductionStrategy) {
		this.reproductionStrategy = reproductionStrategy;
	}
	public void setFertility(boolean fertility) {
		this.isFertile = fertility;
	}
	public void setRandomFertilityCooldown() {
		this.reproductionStrategy.setRandomReproductionCooldown();
	}
	public int getReproductionCooldown() {
		return this.reproductionStrategy.getReproductionCooldown();
	}


	
	
	

	public int getMaximumFoodStorage() {
		return feedingStrategy.getMaximumFoodStorage();
	}
	
	
	
	public void setStrategiesFromGenome() {
		genome.setStrategiesFromGenome(this);
	}


	public void calculateAvailableEvoPoints() {
		genome.calculateAvailableEvoPoints();
	}


	public void takeDamage(int damage) {
		health -= damage;
		if (health <= 0) {
			die();
		}
	}



	public void registerAttackerAndTarget(Creature targetCreature) {
		this.targetCreature = targetCreature;
		targetCreature.threats.add(this);
	}

	public void unregisterAttackerAndTarget() {
		if (this.targetCreature != null) {
			for (int i = 0; i < this.targetCreature.threats.size(); i++) {
				if (this.targetCreature.threats.get(i).id == this.id) {
					this.targetCreature.threats.remove(i);
					i--;
				}
			}
			this.targetCreature = null;
		}
	}
	


}
