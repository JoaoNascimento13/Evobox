package application.dynamic;

import application.core.CloneableRandom;

public abstract class FeedingStrategy extends CreatureStrategy {
	private static final long serialVersionUID = 1L;
	

	abstract void feed(CloneableRandom randomizer);

	abstract int getfoodUsedPerTick();
	
	abstract int getStartingFoodStorage();
	
	public abstract int getMaximumFoodStorage();
	
}
