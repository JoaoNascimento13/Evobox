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
			return 10;
		case HERBIVOROUS:
			return  20;
		case CARNIVOROUS:
			return  30;
		default:
			return 0;
    	}
    }
}
