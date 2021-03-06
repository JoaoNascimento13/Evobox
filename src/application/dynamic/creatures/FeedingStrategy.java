package application.dynamic.creatures;

public abstract class FeedingStrategy extends CreatureStrategy {
	private static final long serialVersionUID = 1L;
	

	abstract void exposeToFeeding();

	abstract int getFoodNeededPerTickModifier();
	
	public abstract int getStartingFoodStorage();
	
	public abstract int getMaximumFoodStorage();
	
}
