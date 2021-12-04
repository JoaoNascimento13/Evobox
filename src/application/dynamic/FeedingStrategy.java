package application.dynamic;

import application.core.RandomizerSingleton;

public abstract class FeedingStrategy extends CreatureStrategy {
	private static final long serialVersionUID = 1L;
	

	abstract void feed();

	abstract int getfoodUsedPerTick();
	
	abstract int getStartingFoodStorage();
	
	public abstract int getMaximumFoodStorage();
	
}
