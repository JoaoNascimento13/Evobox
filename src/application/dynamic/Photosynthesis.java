package application.dynamic;

import application.core.CloneableRandom;
import application.core.Direction;
import application.core.MapStateSingleton;
import application.core.SettingsSingleton;

public class Photosynthesis extends CreatureStrategy implements FeedingStrategy  {
	private static final long serialVersionUID = 1L;

	private Creature creature;
	
	public Photosynthesis (Creature creature) {
		this.creature = creature;
	}
	
	@Override
	public void feed(CloneableRandom randomizer) {
		
		if (creature.food >= getMaximumFoodStorage()) {
			return;
		}
		
		int foodGainedperTick = 6;

		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		int maxX = SettingsSingleton.getInstance().mapCellsX;
		int maxY = SettingsSingleton.getInstance().mapCellsY;
		
		for (Direction dir : Direction.ALL_DIRS) {
			
			//Note: we decide not to let hte creature feed from offmap, this will limit their ability to grow on map edges.
			
			if (creature.x + dir.x < maxX && creature.x + dir.x > -1 && 
				creature.y + dir.y < maxY && creature.y + dir.y > -1 && 
				!mapState.hasPlant(creature.x, creature.y)
				) {
				//Creature can feed from this direction
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
		return 3;
	}

	public int getStartingFoodStorage() {
		//TODO: should take into account creature size, once implemented;
		return 250;
	}
	public int getMaximumFoodStorage() {
		//TODO: should take into account creature size, once implemented;
		return 1000;
	}
	

	
	
	
	
}
