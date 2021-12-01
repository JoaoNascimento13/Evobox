package application.dynamic;

import application.core.CloneableRandom;
import application.core.Direction;

public interface MovementDecisionStrategy {

	
	Direction decideMovementDirection(CloneableRandom randomizer);
	
}
