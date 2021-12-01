package application.dynamic;

import application.core.CloneableRandom;

public interface FeedingStrategy {

	
	void feed(CloneableRandom randomizer);

	int getfoodUsedPerTick();
	
	int getStartingFoodStorage();
	
	int getMaximumFoodStorage();
	
}
