package application.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public enum Direction {

    UP(0, -1), 
    DOWN(0, 1), 
    LEFT(-1, 0), 
    RIGHT(1, 0),
    UP_LEFT(-1, -1), 
    UP_RIGHT(1, -1), 
    DOWN_LEFT(-1, 1), 
    DOWN_RIGHT(1, 1),
    NONE(0, 0);
    
    
    public static final Direction[] CARDINALS = {UP, DOWN, LEFT, RIGHT};
    
    public static final Direction[] DIAGONALS = {UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT};
    
    public static final Direction[] ALL_DIRS = {UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT};
    
    public int x;
    
    public int y;
    
    static public Direction getDirection(int x, int y) {
        if (x < 0) {
            if (y < 0) {
                return UP_LEFT;
            } else if (y == 0) {
                return LEFT;
            } else {
                return DOWN_LEFT;
            }
        } else if (x == 0) {
            if (y < 0) {
                return UP;
            } else if (y == 0) {
                return NONE;
            } else {
                return DOWN;
            }
        } else {
            if (y < 0) {
                return UP_RIGHT;
            } else if (y == 0) {
                return RIGHT;
            } else {
                return DOWN_RIGHT;
            }
        }
    }
    
    public Direction clockwise() {
        switch (this) {
            case UP:
                return UP_RIGHT;
            case DOWN:
                return DOWN_LEFT;
            case LEFT:
                return UP_LEFT;
            case RIGHT:
                return DOWN_RIGHT;
            case UP_LEFT:
                return UP;
            case UP_RIGHT:
                return RIGHT;
            case DOWN_LEFT:
                return LEFT;
            case DOWN_RIGHT:
                return DOWN;
            case NONE:
            default:
                return NONE;
        }
    }
    
    public Direction counterClockwise() {
        switch (this) {
            case UP:
                return UP_LEFT;
            case DOWN:
                return DOWN_RIGHT;
            case LEFT:
                return DOWN_LEFT;
            case RIGHT:
                return UP_RIGHT;
            case UP_LEFT:
                return LEFT;
            case UP_RIGHT:
                return UP;
            case DOWN_LEFT:
                return DOWN;
            case DOWN_RIGHT:
                return RIGHT;
            case NONE:
            default:
                return NONE;
        }
    }
    
    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case UP_LEFT:
                return DOWN_RIGHT;
            case UP_RIGHT:
                return DOWN_LEFT;
            case DOWN_LEFT:
                return UP_RIGHT;
            case DOWN_RIGHT:
                return UP_LEFT;
            case NONE:
            default:
                return NONE;
        }
    }

    public static Direction random(Random random) {
    	if (random == null) {
    		random = new Random();
    	}
        switch (random.nextInt(8)) {
            case 0:
                return DOWN;
            case 1:
                return UP;
            case 2:
                return RIGHT;
            case 3:
                return LEFT;
            case 4:
                return DOWN_RIGHT;
            case 5:
                return DOWN_LEFT;
            case 6:
                return UP_RIGHT;
            case 7:
                return UP_LEFT;
            default:
                return NONE;
        }
    }
    

    public static Direction[] closestFirstArray() {
    	Direction[] dirs = new Direction[] {NONE, UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT};
    	return dirs;
    }
    
    public static Direction[] cardinalFirstArray() {
    	Direction[] dirs = new Direction[] {UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT};
    	return dirs;
    }

    public static Direction[] diagonalFirstArray() {
    	Direction[] dirs = new Direction[] {UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT, UP, DOWN, LEFT, RIGHT};
    	return dirs;
    }
    
    public static ArrayList<Direction> randomCardinalArrayList(Random random) {
    	ArrayList<Direction> randomList = new ArrayList<Direction>();
    	randomList.add(UP);
    	randomList.add(DOWN);
    	randomList.add(LEFT);
    	randomList.add(RIGHT);
    	if (random == null) {
        	Collections.shuffle(randomList);
    	} else {
        	Collections.shuffle(randomList, random);
    	}
    	return randomList;
    }

    public static ArrayList<Direction> randomDiagonalArrayList(Random random) {
    	ArrayList<Direction> randomList = new ArrayList<Direction>();
    	randomList.add(UP_LEFT);
    	randomList.add(UP_RIGHT);
    	randomList.add(DOWN_LEFT);
    	randomList.add(DOWN_RIGHT);
    	if (random == null) {
        	Collections.shuffle(randomList);
    	} else {
        	Collections.shuffle(randomList, random);
    	}
    	return randomList;
    }
    
    public static ArrayList<Direction> randomArrayList(Random random) {
    	ArrayList<Direction> randomList = new ArrayList<Direction>();
    	randomList.add(UP);
    	randomList.add(DOWN);
    	randomList.add(LEFT);
    	randomList.add(RIGHT);
    	randomList.add(UP_LEFT);
    	randomList.add(UP_RIGHT);
    	randomList.add(DOWN_LEFT);
    	randomList.add(DOWN_RIGHT);
    	if (random == null) {
        	Collections.shuffle(randomList);
    	} else {
        	Collections.shuffle(randomList, random);
    	}
    	return randomList;
    } 

    public static ArrayList<Direction> randomCardinalFirstArrayList(Random random) {
    	ArrayList<Direction> randomList = new ArrayList<Direction>();
    	randomList.add(UP);
    	randomList.add(DOWN);
    	randomList.add(LEFT);
    	randomList.add(RIGHT);
    	if (random == null) {
        	Collections.shuffle(randomList);
    	} else {
        	Collections.shuffle(randomList, random);
    	}
    	ArrayList<Direction> randomListB = new ArrayList<Direction>();
    	randomListB.add(UP_LEFT);
    	randomListB.add(UP_RIGHT);
    	randomListB.add(DOWN_LEFT);
    	randomListB.add(DOWN_RIGHT);
    	if (random == null) {
        	Collections.shuffle(randomListB);
    	} else {
        	Collections.shuffle(randomListB, random);
    	}
    	randomList.addAll(randomListB);
    	return randomList;
    }

    public static ArrayList<Direction> randomCloserFirstArrayList(Random random) {
    	ArrayList<Direction> randomListA = new ArrayList<Direction>();
    	randomListA.add(UP);
    	randomListA.add(DOWN);
    	randomListA.add(LEFT);
    	randomListA.add(RIGHT);
    	if (random == null) {
        	Collections.shuffle(randomListA);
    	} else {
        	Collections.shuffle(randomListA, random);
    	}
    	ArrayList<Direction> randomListB = new ArrayList<Direction>();
    	randomListB.add(UP_LEFT);
    	randomListB.add(UP_RIGHT);
    	randomListB.add(DOWN_LEFT);
    	randomListB.add(DOWN_RIGHT);
    	if (random == null) {
        	Collections.shuffle(randomListB);
    	} else {
        	Collections.shuffle(randomListB, random);
    	}
    	ArrayList<Direction> randomList = new ArrayList<Direction>();
    	randomList.add(Direction.NONE);
    	randomList.addAll(randomListA);
    	randomList.addAll(randomListB);
    	return randomList;
    } 
    
    public int projectileModifier() {
        switch (this) {
            case UP:
                return 0;
            case DOWN:
                return 1;
            case LEFT:
                return 2;
            case RIGHT:
                return 3;
            case UP_LEFT:
                return 4;
            case UP_RIGHT:
                return 5;
            case DOWN_LEFT:
                return 6;
            case DOWN_RIGHT:
                return 7;
            case NONE:
            default:
                return 0;
        }
    }
    
    private Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

	public String getDescription() {
        switch (this) {
        case UP:
            return "north";
        case DOWN:
            return "south";
        case LEFT:
            return "west";
        case RIGHT:
            return "east";
        case UP_LEFT:
            return "northwest";
        case UP_RIGHT:
            return "northeast";
        case DOWN_LEFT:
            return "southwest";
        case DOWN_RIGHT:
            return "southeast";
        case NONE:
        default:
            return "";
        }
	}

	public boolean isDiagonal() {
		if (this == UP_LEFT || this == UP_RIGHT || this == DOWN_LEFT || this == DOWN_RIGHT) {
			return true;
		}
		return false;
	}
	
	
	

	public int getRotationDir(Direction targetDir) {
		//Note: returns the shortest rotation direction (clockwise or counterclockwise) to rotate this direction, in order to obtain the target direction.
		//0: No rotation (dirs are the same)
		//-2: Dirs are opposite, both rotations are equal
		//-1: counterclockwise
		//1: clockwise
		
		if (this == targetDir) {
			return 0;
		}
		if (this == targetDir.opposite()) {
			return -2;
		}
		switch (this) {
		case DOWN:
			if (targetDir.x < 0) {
				return 1;
			}
			if (targetDir.x > 0) {
				return -1;
			}
			break;
			
		case UP:
			if (targetDir.x < 0) {
				return -1;
			}
			if (targetDir.x > 0) {
				return 1;
			}
			break;
				

		case LEFT:
			if (targetDir.y < 0) {
				return 1;
			}
			if (targetDir.y > 0) {
				return -1;
			}
			break;
			
		case RIGHT:
			if (targetDir.y < 0) {
				return -1;
			}
			if (targetDir.y > 0) {
				return 1;
			}
			break;
			
			
		case DOWN_LEFT:
			if (targetDir.x < 0 || targetDir.y < 0 ) {
				return 1;
			}
			if (targetDir.x > 0 || targetDir.y > 0 ) {
				return -1;
			}
			break;

		case UP_RIGHT:
			if (targetDir.x < 0 || targetDir.y < 0 ) {
				return -1;
			}
			if (targetDir.x > 0 || targetDir.y > 0 ) {
				return 1;
			}
			break;
			
		case DOWN_RIGHT:
			if (targetDir.y < targetDir.x) {
				return -1;
			}
			if (targetDir.y > targetDir.x) {
				return 1;
			}
			break;
			
		case UP_LEFT:
			if (targetDir.y < targetDir.x) {
				return 1;
			}
			if (targetDir.y > targetDir.x) {
				return -1;
			}
			break;
		
		}
		
		return 0;
	}

    
}
