package application.dynamic.factories;

import java.awt.Point;

import application.dynamic.creatures.Creature;

public interface CreatureFactory {
	
	Creature createCreature(long currentTick);
	
	void setParents(Creature parentA, Creature parentB);
	
	void setSpawnPoint(Point spawnPoint);
}
