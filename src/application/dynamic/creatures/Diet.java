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
}
