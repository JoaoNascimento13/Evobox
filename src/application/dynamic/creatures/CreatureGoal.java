package application.dynamic.creatures;

public enum CreatureGoal {
	
	FEED ("Hunting for food."),
	MATE ("Looking for a mate."),
	FLEE ("Running from a threat."),
	WANDER ("Wandering around."), 
	
	FLOAT ("Floating in the currents."),
	SPROUT ("About to sprout...");
	
    public String description;
	private CreatureGoal(String name) {
		this.description = name;
	}
    
	boolean allowsReproduction() {
		if (this == MATE || this == FLOAT) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isActive() {
		if (this == SPROUT) {
			return false;
		} else {
			return true;
		}
	}
}
