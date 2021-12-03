package application.dynamic;

import java.io.Serializable;

import application.core.CloneableRandom;
import application.core.Direction;
import application.core.MapStateSingleton;
import application.core.OutdatedPositionsSingleton;
import application.core.SettingsSingleton;
import application.core.Simulator;

public class Creature implements Cloneable, Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	public int x;
	public int y;
	
	private int obstacleAvoidanceRotation; //1 to rotate clockwise, -1 to rotate counterclockwise
	
	
	public long nextActivation;
	
	public Genome genome;
	
	public int age;
	public int food;
	public boolean isFertile;
	
	public long id;
	
	public Creature (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	public MovementDecisionStrategy movementDecisionStrategy;

	public FeedingStrategy feedingStrategy;

	public ReproductionStrategy reproductionStrategy;
	
	
	
	public void actOrPostpone(long currentTick, CloneableRandom randomizer) {
		if (this.nextActivation == currentTick) {
			act(randomizer, currentTick);
			nextActivation = currentTick + ticksPerTurn();
		}
	}
	

	public void act(CloneableRandom randomizer, long tick) {

//		System.out.println();
//		System.out.println("activating creature: " + this);
		
		age();
		useFood();
		
		if(die(randomizer)) {
			return;
		}
		
		move(randomizer);
		
		feed(randomizer);
		
		reproduce(randomizer, tick);
		
//		System.out.println("creature finished: " + this);
	}
	
	
	public int ticksPerTurn() {
		return (11-this.genome.speed);
	}


	public void age() {
		this.age += ticksPerTurn();
	}
	public void useFood() {
		this.food -= ticksPerTurn()*feedingStrategy.getfoodUsedPerTick();
	}


	public boolean die(CloneableRandom randomizer) {
		if (dieFromOldAgeOrContinue(randomizer)) {
			return true;
		}
		if (dieFromStarvationOrContinue()) {
			return true;
		}
		return false;
	}
	
	
	public boolean dieFromOldAgeOrContinue(CloneableRandom randomizer) {

		int chanceOfDeath = 0;
		
		if (this.age > (6*this.genome.ageExpectancy)/5) {
			
			chanceOfDeath = 25;
			
		} else if (this.age > this.genome.ageExpectancy) {

			chanceOfDeath = 5;
		
		} else if (this.age > (4*this.genome.ageExpectancy)/5) {
			
			chanceOfDeath = 1;
		}
		
		if (randomizer.nextInt(100) < chanceOfDeath) {
//			System.out.println("- 1 old age");
			die();
			return true;
		} else {
			return false;
		}
	}
	

	public boolean dieFromStarvationOrContinue() {
		
		if (food <= 0) {
//			System.out.println("- 1 starved");
			die();
			return true;
		} else {
			return false;
		}
	}
	

	public void die() {
		OutdatedPositionsSingleton.getInstance().addCreaturePosition(x, y);
		
		MapStateSingleton.getInstance().clearCreature(this);

		MapStateSingleton.getInstance().queueCreatureUnregister(this);
		
	}
	
	
	public void move(CloneableRandom randomizer) {
		
		Direction dir = this.movementDecisionStrategy.decideMovementDirection(randomizer);

		moveInDir(dir, randomizer);
	}



	public void feed(CloneableRandom randomizer) {
		
		this.feedingStrategy.feed(randomizer);
		
	}

	

	public void reproduce(CloneableRandom randomizer, long tick) {
		
		this.reproductionStrategy.reproduce(randomizer, tick);
		
	}

	
	
	
	
	
//	public boolean canMoveInDir(Direction dir, MapStateSingleton mapState) {
//		return (mapState.isAvailable(x + dir.x, y + dir.y);
//	}
	
	
	public void moveInDir(Direction mainDir, CloneableRandom randomizer) {

		if (mainDir == Direction.NONE) {
			return;
		}
		
//		System.out.println("moveInDir: " + this);
		
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		Direction movementDir = mainDir;
		
		if (!mapState.isAvailable(this.x+movementDir.x, this.y+movementDir.y)) {
			

//			System.out.println("can't move in dir: " + movementDir);
			
			if (obstacleAvoidanceRotation == 0) {
				obstacleAvoidanceRotation = (2*randomizer.nextInt(2))-1;
			}
			
			if (obstacleAvoidanceRotation == 1) {
				
				movementDir = movementDir.clockwise();
				
				if (!mapState.isAvailable(this.x+movementDir.x, this.y+movementDir.y)) {

//					System.out.println("can't move in dir: " + movementDir);
					
					movementDir = movementDir.clockwise();
					
					if (!mapState.isAvailable(this.x+movementDir.x, this.y+movementDir.y)) {

//						System.out.println("can't move in dir: " + movementDir);
						
						
						
						//If we can't move in a perpendicular direction to the original, 
						//we'll stop on this turn and move on the opposite direction on the next
						
						movementDir = Direction.NONE;
						obstacleAvoidanceRotation = -1;
					}
				}

			} else if (obstacleAvoidanceRotation == -1) {

				movementDir = movementDir.counterClockwise();
				
				if (!mapState.isAvailable(this.x+movementDir.x, this.y+movementDir.y)) {


//					System.out.println("can't move in dir: " + movementDir);
					
					
					movementDir = movementDir.counterClockwise();
					
					if (!mapState.isAvailable(this.x+movementDir.x, this.y+movementDir.y)) {


//						System.out.println("can't move in dir: " + movementDir);
						
						
						//If we can't move in a perpendicular direction to the original, 
						//we'll stop on this turn and move on the opposite direction on the next
						
						movementDir = Direction.NONE;
						obstacleAvoidanceRotation = 1;
					}
				}
			}
			
		} else {
			obstacleAvoidanceRotation = 0;

//			System.out.println("can move in main dir: " + movementDir);
			
			
		}
		
		if (movementDir != Direction.NONE) {

//			System.out.println("moving in dir: " + movementDir);
			
//			if (mapState.isAvailable(this.x+movementDir.x, this.y+movementDir.y)) {
//				System.out.println("is available");
//			} else {
//				System.out.println("IS NOT AVAILABLE");
//			}
			
			OutdatedPositionsSingleton.getInstance().addCreaturePosition(x, y);
			mapState.clearCreature(this);
			
			this.x += movementDir.x;
			this.y += movementDir.y;
			
			mapState.setCreatureInPoint(this);
			
		}
	}
	
	
	
	
	
	

	public void setNextActivation(long nextActivation) {
		this.nextActivation = nextActivation;
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
	
	public void setMovementDecisionStrategy(MovementDecisionStrategy moveStrategy) {
		this.movementDecisionStrategy = moveStrategy;
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


	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
