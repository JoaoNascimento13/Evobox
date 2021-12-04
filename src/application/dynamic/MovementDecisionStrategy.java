package application.dynamic;

import application.core.Direction;

public abstract class MovementDecisionStrategy extends CreatureStrategy {
	private static final long serialVersionUID = 1L;

	abstract Direction decideMovementDirection();
	
}
