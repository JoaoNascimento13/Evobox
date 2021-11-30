package application.dynamic;

import application.core.CloneableRandom;

public interface CreatureFactory {
	
	Creature createCreature(CloneableRandom randomizer);
}
