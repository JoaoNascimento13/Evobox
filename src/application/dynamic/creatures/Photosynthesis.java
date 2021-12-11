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
		
		int foodGainedperSizePerTick = 5;
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		
		for (Direction dir : Direction.ALL_DIRS) {
			
			//Note: we decide not to let the creature feed from offmap, this will limit their ability to grow on map edges.
			//This is a design decision, there's no problems with allowing it to feed based on off-map points.
			
			if (
				mapState.isWithinBounds(creature.x+dir.x, creature.y+dir.y) && 
				!mapState.hasPlant(creature.x+dir.x, creature.y+dir.y)
				) {
				
			} else {
				
				foodGainedperSizePerTick -= 1;
				if (foodGainedperSizePerTick == 0) {
					break;
				}
			}
		}
		
		creature.food += foodGainedperSizePerTick * creature.genome.getSize() * creature.ticksPerTurn();
	}

	

	@Override
	public int getFoodNeededPerTickModifier() {
		return creature.genome.getSize();
	}

	@Override
	public int getStartingFoodStorage() {
		return 200 * creature.genome.getSize();
	}
	
	@Override
	public int getMaximumFoodStorage() {
		return 500 * creature.genome.getSize();
	}
	

	
	
	
	
}
