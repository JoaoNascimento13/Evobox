package application.dynamic.creatures;

public enum Diet {
	
    PHOTOSYNTHESIS ("Photosynthetic"), 
    HERBIVOROUS ("Herbivorous"), 
    CARNIVOROUS ("Carnivorous");
    

    public String name;
    
    private Diet(String name) {
		this.name = name;
	}

	public int getEvoPointsGranted() {
    	switch(this) {
		case PHOTOSYNTHESIS:
			return 20;
		case HERBIVOROUS:
			return  50;
		case CARNIVOROUS:
			return  70;
		default:
			return 0;
    	}
    }
	
	public String getBarColorString() {
    	switch(this) {
		case PHOTOSYNTHESIS:
			return "-fx-accent: green;";
		case HERBIVOROUS:
			return  "-fx-accent: blue;";
		case CARNIVOROUS:
			return  "-fx-accent: red;";
		default:
			return "-fx-accent: black;";
    	}
	}

	int getFoodEfficiencyCoef() {
    	switch(this) {
		case PHOTOSYNTHESIS:
			return 1;
		case HERBIVOROUS:
			return  1;
		case CARNIVOROUS:
			return  4;
		default:
			return 1;
    	}
	}

	int getFertilityCoef() {
    	switch(this) {
		case PHOTOSYNTHESIS:
			return 3;
		case HERBIVOROUS:
			return  2;
		case CARNIVOROUS:
			return  1;
		default:
			return 1;
    	}
	}
}
