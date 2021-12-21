package application.dynamic.creatures;

import java.awt.Point;
import application.core.RandomizerSingleton;
import application.core.Direction;
import application.core.MapStateSingleton;

public class PlantMovementDecision extends MovementDecisionStrategy  {
	private static final long serialVersionUID = 1L;
	
	private Creature creature;
	
	public PlantMovementDecision (Creature creature) {
		this.creature = creature;
	}
	
	

	@Override
	public Direction decideMovementDirection() {
		
		creature.goal = CreatureGoal.FLOAT;

		RandomizerSingleton randomizer = RandomizerSingleton.getInstance();
		
		Point[][] flowMap = MapStateSingleton.getInstance().getFlowMap();
		
		int dirX = 0;
		int dirY = 0;

		
		if (randomizer.nextInt(100) < Math.abs(flowMap[creature.x][creature.y].x)) {
			
			if (flowMap[creature.x][creature.y].x > 0) {
				dirX = 1;
			} else if (flowMap[creature.x][creature.y].x < 0) {
				dirX = -1;
			}

		}
		
		if (randomizer.nextInt(100) < Math.abs(flowMap[creature.x][creature.y].y)) {
			
			if (flowMap[creature.x][creature.y].y > 0) {
				dirY = 1;
			} else if (flowMap[creature.x][creature.y].y < 0) {
				dirY = -1;
			}
		}
		return Direction.getDirection(dirX, dirY);
	}
	
	
	
	public Direction decideSproutingDirection() {
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		if (mapState.isAvailable(creature.x, creature.y)) {
			return Direction.NONE;
		}
		for (Direction dir : Direction.randomArrayList(RandomizerSingleton.getInstance())) {
			if (mapState.isAvailable(creature.x+dir.x, creature.y+dir.y)) {
				return dir;
			}
		}
		return null;
	}
	

	@Override
	public CreatureGoal getStartingGoal() {
		return CreatureGoal.FLOAT;
	}

	
	
	
	
}
