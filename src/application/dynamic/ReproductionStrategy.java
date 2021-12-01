package application.dynamic;

import application.core.CloneableRandom;

public abstract class ReproductionStrategy extends CreatureStrategy {
	private static final long serialVersionUID = 1L;

	abstract void reproduce(CloneableRandom randomizer, long tick);

	abstract void startReproductionCooldown();

	
}
