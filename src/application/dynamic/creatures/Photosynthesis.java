package application.dynamic.creatures;

import application.core.Direction;
import application.core.MapStateSingleton;

public class Photosynthesis extends FeedingStrategy  {
	private static final long serialVersionUID = 1L;
	
	public Photosynthesis (Creature creature) {
		this.creature = creature;
	}
	
	@Override
	public void exposeToFeeding() {
		
		if (creature.food >= getMaximumFoodStorage()) {
			return;
		}
		
		int foodGainedperTick = 5;
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		
		for (Direction dir : Direction.ALL_DIRS) {
			
			//Note: we decide not to let the creature feed from offmap, this will limit their ability to grow on map edges.
			//This is a design decision, there's no problems with allowing it to feed based on off-map points.
			
			if (
				mapState.isWithinBounds(creature.x+dir.x, creature.y+dir.y) && 
				!mapState.hasPlant(creature.x+dir.x, creature.y+dir.y)
				) {
				
			} else {
				
				foodGainedperTick -= 1;
				if (foodGainedperTick == 0) {
					break;
				}
			}
		}
		
		creature.food += foodGainedperTick * creature.ticksPerTurn();
	}

	

	@Override
	public int getfoodUsedPerTick() {
		return 2;
	}

	@Override
	public int getStartingFoodStorage() {
		//TODO: should take into account creature size, once implemented;
		return 250;
	}
	
	@Override
	public int getMaximumFoodStorage() {
		//TODO: should take into account creature size, once implemented;
		return 1000;
	}
	

	
	
	
	
}
