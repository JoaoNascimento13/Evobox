package application.dynamic;

import java.awt.Point;

public interface CreatureFactory {
	
	Creature createCreature(long currentTick);
	
	void setParents(Creature parentA, Creature parentB);
	
	void setSpawnPoint(Point spawnPoint);
}
