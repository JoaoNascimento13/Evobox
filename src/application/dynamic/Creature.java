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
	
	public int speed;
	
	public long nextActivation;
	
	
	
	public Creature (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	private MoveStrategy moveStrategy;
	
	
	
	
	public void actOrPostpone(long currentTick, CloneableRandom randomizer) {
		if (this.nextActivation == currentTick) {
			act(randomizer);
			nextActivation = currentTick + (11-this.speed);
		}
	}
	

	public void act(CloneableRandom randomizer) {
		move(randomizer);
	}
	
	
	
	
	
	public void move(CloneableRandom randomizer) {
		this.moveStrategy.move(randomizer);
		MapStateSingleton.getInstance().setCreature(this);
	}


	public boolean canMoveInDir(Direction dir, MapStateSingleton mapState, int maxX, int maxY) {

		return (
				x + dir.x < maxX && x + dir.x > -1 && 
				y + dir.y < maxY && y + dir.y > -1 && 
				(!mapState.hasCreature(x + dir.x, y + dir.y)) 
				);
	}
	
	public void moveInDir(Direction mainDir, CloneableRandom randomizer) {
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		int maxX = SettingsSingleton.getInstance().mapCellsX;
		int maxY = SettingsSingleton.getInstance().mapCellsY;
		
		
		if (!canMoveInDir(mainDir, mapState, maxX, maxY)) {
			

			if (obstacleAvoidanceRotation == 0) {
				obstacleAvoidanceRotation = (2*randomizer.nextInt(2))-1;
			}
			
			
			if (obstacleAvoidanceRotation == 1) {
				
				mainDir = mainDir.clockwise();
				
				if (!canMoveInDir(mainDir, mapState, maxX, maxY)) {

					mainDir = mainDir.clockwise();
					
					if (!canMoveInDir(mainDir, mapState, maxX, maxY)) {
						
						//If we can't move in a perpendicular direction to the original, 
						//we'll stop on this turn and move on the opposite direction on the next
						
						mainDir = Direction.NONE;
						obstacleAvoidanceRotation = -1;
					}

					
				}

			} else if (obstacleAvoidanceRotation == -1) {

				mainDir = mainDir.counterClockwise();
				
				if (!canMoveInDir(mainDir, mapState, maxX, maxY)) {

					mainDir = mainDir.counterClockwise();
					
					if (!canMoveInDir(mainDir, mapState, maxX, maxY)) {
						
						//If we can't move in a perpendicular direction to the original, 
						//we'll stop on this turn and move on the opposite direction on the next
						
						mainDir = Direction.NONE;
						obstacleAvoidanceRotation = 1;
					}
				}
			}
			
		} else {
			obstacleAvoidanceRotation = 0;
		}
		
		
		if (mainDir != Direction.NONE) {

			OutdatedPositionsSingleton.getInstance().addCreaturePosition(x, y);
			mapState.clearCreature(this);
			
			this.x += mainDir.x;
			this.y += mainDir.y;
			
			
			mapState.setCreature(this);
			
		}
		
	}
	
	
	
	
	
	

	public void setNextActivation(long nextActivation) {
		this.nextActivation = nextActivation;
	}
	
	
	public void setMoveStrategy(MoveStrategy moveStrategy) {
		this.moveStrategy = moveStrategy;
	}

	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
