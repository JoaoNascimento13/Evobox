package application.dynamic;

import application.core.RandomizerSingleton;

public abstract class ReproductionStrategy extends CreatureStrategy {
	private static final long serialVersionUID = 1L;

	abstract void reproduce(long tick);

	abstract void startReproductionCooldown();

	
}
