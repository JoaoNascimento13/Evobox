package application.dynamic.creatures;

public enum CreatureGoal {
	FEED,
	MATE,
	FLEE,
	WANDER, 
	
	FLOAT;

	boolean allowsReproduction() {
		if (this == MATE || this == FLOAT) {
			return true;
		} else {
			return false;
		}
	}
}
