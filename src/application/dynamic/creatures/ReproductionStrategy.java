package application.dynamic.creatures;

public abstract class ReproductionStrategy extends CreatureStrategy {
	private static final long serialVersionUID = 1L;

	abstract void exposeToReproduction(long tick);
	
	abstract void startReproductionCooldown();

	
}
