package application.dynamic;

import java.io.Serializable;

import application.core.CloneableRandom;
import application.core.Direction;
import application.core.MapStateSingleton;
import application.core.OutdatedPositionsSingleton;
import application.core.SettingsSingleton;

public class Creature implements Cloneable, Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	public int x;
	public int y;
	
	private int obstacleAvoidanceRotation; //1 to rotate clockwise, -1 to rotate counterclockwise
	
	
	public long nextActivation;
	
	public Genome genome;
	
	public int age;
	
	public long id;
	
	public Creature (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	private MovementDecisionStrategy movementDecisionStrategy;
	
	
	
	
	public void actOrPostpone(long currentTick, CloneableRandom randomizer) {
		if (this.nextActivation == currentTick) {
			act(randomizer);
			nextActivation = currentTick + (11-this.genome.speed);
		}
	}
	

	public void act(CloneableRandom randomizer) {
		
		age();
		
		dieFromOldAgeOrContinue(randomizer);
		
		
		move(randomizer);
	}
	
	


	public void age() {
		this.age += (11-this.genome.speed);
	}

	
	public void dieFromOldAgeOrContinue(CloneableRandom randomizer) {

		int chanceOfDeath = 0;
		
		if (this.age > (6*this.genome.ageExpectancy)/5) {
			
			chanceOfDeath = 25;
			
		} else if (this.age > this.genome.ageExpectancy) {

			chanceOfDeath = 5;
		
		} else if (this.age > (4*this.genome.ageExpectancy)/5) {
			
			chanceOfDeath = 1;
		}
		
		if (randomizer.nextInt(100) < chanceOfDeath) {
			die();
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


	public boolean canMoveInDir(Direction dir, MapStateSingleton mapState, int maxX, int maxY) {
		
//		System.out.println("testing dir: " + dir);
		return (
				x + dir.x < maxX && x + dir.x > -1 && 
				y + dir.y < maxY && y + dir.y > -1 && 
				(mapState.isEmpty(x + dir.x, y + dir.y)) 
				);
	}
	
	
	public void moveInDir(Direction mainDir, CloneableRandom randomizer) {

		if (mainDir == Direction.NONE) {
			return;
		}
		
//		System.out.println();
//		System.out.println("moveInDir");
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		int maxX = SettingsSingleton.getInstance().mapCellsX;
		int maxY = SettingsSingleton.getInstance().mapCellsY;
		
		Direction movementDir = mainDir;
		
		if (!canMoveInDir(movementDir, mapState, maxX, maxY)) {
			
			if (obstacleAvoidanceRotation == 0) {
				obstacleAvoidanceRotation = (2*randomizer.nextInt(2))-1;
			}
			
			if (obstacleAvoidanceRotation == 1) {
				
				movementDir = movementDir.clockwise();
				
				if (!canMoveInDir(movementDir, mapState, maxX, maxY)) {

					movementDir = movementDir.clockwise();
					
					if (!canMoveInDir(movementDir, mapState, maxX, maxY)) {
						
						//If we can't move in a perpendicular direction to the original, 
						//we'll stop on this turn and move on the opposite direction on the next
						
						movementDir = Direction.NONE;
						obstacleAvoidanceRotation = -1;
					}
				}

			} else if (obstacleAvoidanceRotation == -1) {

				movementDir = movementDir.counterClockwise();
				
				if (!canMoveInDir(movementDir, mapState, maxX, maxY)) {

					movementDir = movementDir.counterClockwise();
					
					if (!canMoveInDir(movementDir, mapState, maxX, maxY)) {
						
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
			
//			System.out.println("moving");
			
			OutdatedPositionsSingleton.getInstance().addCreaturePosition(x, y);
			mapState.clearCreature(this);
			
			this.x += movementDir.x;
			this.y += movementDir.y;
			
			mapState.setCreatureInPoint(this);
			
		} else {

//			System.out.println("not moving");
		}
	}
	
	
	
	
	
	

	public void setNextActivation(long nextActivation) {
		this.nextActivation = nextActivation;
	}
	
	public void setGenome(Genome genome) {
		this.genome = genome;
	}
	
	public void setMoveStrategy(MovementDecisionStrategy moveStrategy) {
		this.movementDecisionStrategy = moveStrategy;
	}

	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
