package application.dynamic;

import java.awt.Point;
import application.core.CloneableRandom;
import application.core.Direction;
import application.core.MapStateSingleton;
import application.core.OutdatedPositionsSingleton;
import application.core.SettingsSingleton;

public class FloaterMoveStrategy extends CreatureStrategy implements MoveStrategy  {
	private static final long serialVersionUID = 1L;
	
	private Creature creature;
	
	public FloaterMoveStrategy (Creature creature) {
		this.creature = creature;
	}
	
	
	
	public void move(CloneableRandom randomizer) {

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
		
		Direction dir = Direction.getDirection(dirX, dirY);
		
		creature.moveInDir(dir, randomizer);
		
//		if (randomizer.nextInt(100) < Math.abs(flowMap[creature.x][creature.y].x)) {
//
//			
////			if (flowMap[creature.x][creature.y].x > 0) {
////				creature.x = Math.max(0,Math.min(lastPixelX, creature.x + 1));
////				
////			} else if (flowMap[creature.x][creature.y].x < 0) {
////				creature.x = Math.max(0,Math.min(lastPixelX, creature.x - 1));
////			}
//			
////			
//		}
		
//		if (randomizer.nextInt(100) < Math.abs(flowMap[creature.x][creature.y].x)) {
//			
//			outdatedPositions.addCreaturePosition(creature.x, creature.y);
//			
//			if (flowMap[creature.x][creature.y].x > 0) {
//				creature.x = Math.max(0,Math.min(lastPixelX, creature.x + 1));
//				
//			} else if (flowMap[creature.x][creature.y].x < 0) {
//				creature.x = Math.max(0,Math.min(lastPixelX, creature.x - 1));
//			}
//
//		}
//		
//		if (randomizer.nextInt(100) < Math.abs(flowMap[creature.x][creature.y].y)) {
//			
//			outdatedPositions.addCreaturePosition(creature.x, creature.y);
//			
//			if (flowMap[creature.x][creature.y].y > 0) {
//				creature.y = Math.max(0,Math.min(lastPixelY, creature.y + 1));
//			} else if (flowMap[creature.x][creature.y].y < 0) {
//				creature.y = Math.max(0,Math.min(lastPixelY, creature.y - 1));
//			}
//		}
		
	}

	
	
	
	
}
