package application.dynamic;

import application.core.Direction;
import application.core.MapStateSingleton;

public class Photosynthesis extends FeedingStrategy  {
	private static final long serialVersionUID = 1L;
	
	public Photosynthesis (Creature creature) {
		this.creature = creature;
	}
	
	@Override
	public void feed() {
		
		if (creature.food >= getMaximumFoodStorage()) {
			return;
		}
		
		int foodGainedperTick = 5;

//		System.out.println("feeding - " + creature);
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		
		for (Direction dir : Direction.ALL_DIRS) {
			
			//Note: we decide not to let the creature feed from offmap, this will limit their ability to grow on map edges.
			
			if (
				mapState.isWithinBounds(creature.x+dir.x, creature.y+dir.y) && 
				!mapState.hasPlant(creature.x+dir.x, creature.y+dir.y)
				) {
				
//				System.out.println(dir + " - No creature - can feed");
				
				//Creature can feed from this direction
			} else {

//				if (!mapState.isWithinBounds(creature.x+dir.x, creature.y+dir.y)) {
//
//					System.out.println(dir + " - out of bounds - Cant feed");
//					
//				} else if (mapState.hasPlant(creature.x+dir.x, creature.y+dir.y)) {
//
//					System.out.println(dir + " - has plant - Cant feed");
//
//					System.out.println("in location: " + (creature.x+dir.x) + ", " + (creature.y+dir.y));
//					
//					Creature c = mapState.getCreature(creature.x+dir.x, creature.y+dir.y);
//					
//					System.out.println("exists plant: " + c);
//					System.out.println("with coords: " + c.x + ", " + c.y);
//				}
				
				foodGainedperTick -= 1;
				if (foodGainedperTick == 0) {
					break;
				}
			}
		}
		
//		System.out.println("Current food " + creature.food + ", gained: " + (foodGainedperTick * creature.ticksPerTurn()));
		
		creature.food += foodGainedperTick * creature.ticksPerTurn();
	}

	

	@Override
	public int getfoodUsedPerTick() {
		return 2;
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
