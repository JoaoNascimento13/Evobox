package application.dynamic;

import java.awt.Point;
import application.core.CloneableRandom;
import application.core.Direction;
import application.core.MapStateSingleton;

public class FloaterMovementDecision extends MovementDecisionStrategy  {
	private static final long serialVersionUID = 1L;
	
	private Creature creature;
	
	public FloaterMovementDecision (Creature creature) {
		this.creature = creature;
	}
	
	

	@Override
	public Direction decideMovementDirection(CloneableRandom randomizer) {

//		OutdatedPositionsSingleton outdatedPositions = OutdatedPositionsSingleton.getInstance();


//		int lastPixelX = SettingsSingleton.getInstance().mapCellsX-1;
//		int lastPixelY = SettingsSingleton.getInstance().mapCellsY-1;;
		
		
		Point[][] flowMap = MapStateSingleton.getInstance().getFlowMap();
		
		int dirX = 0;
		int dirY = 0;

		
		if (randomizer.nextInt(100) < Math.abs(flowMap[creature.x][creature.y].x)) {
			
//			outdatedPositions.addCreaturePosition(creature.x, creature.y);
			
			if (flowMap[creature.x][creature.y].x > 0) {
				dirX = 1;
			} else if (flowMap[creature.x][creature.y].x < 0) {
				dirX = -1;
			}

		}
		
		if (randomizer.nextInt(100) < Math.abs(flowMap[creature.x][creature.y].y)) {
			
//			outdatedPositions.addCreaturePosition(creature.x, creature.y);
			
			if (flowMap[creature.x][creature.y].y > 0) {
				dirY = 1;
			} else if (flowMap[creature.x][creature.y].y < 0) {
				dirY = -1;
			}
		}
		
		return Direction.getDirection(dirX, dirY);
		
		
		
	}

	
	
	
	
}
