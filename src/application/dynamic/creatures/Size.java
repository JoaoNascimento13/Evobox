package application.dynamic.creatures;

public enum Size {
	
    SMALL, 
    MEDIUM, 
    LARGE;
	

    public int getEvoPointsGranted() {
    	switch(this) {
		case SMALL:
			return 0;
		case MEDIUM:
			return  10;
		case LARGE:
			return  20;
		default:
			return 0;
    	}
    }
}
