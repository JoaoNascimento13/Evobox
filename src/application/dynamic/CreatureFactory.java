package application.dynamic;

import java.awt.Point;

import application.core.CloneableRandom;

public interface CreatureFactory {
	
	Creature createCreature(CloneableRandom randomizer, long currentTick);
	
	void setParents(Creature parentA, Creature parentB);
	
	void setSpawnPoint(Point spawnPoint);
}
