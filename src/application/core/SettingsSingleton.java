package application.core;

import application.dynamic.creatures.Diet;
import application.dynamic.creatures.Genome;

public class SettingsSingleton {
	
	public int periodicRecordings;
	public int mapCellsX;
	public int mapCellsY;
	
	public int plantBirthsPerMutation;
	public int plantMutationsPerDietChange;
	public int plantMutationsPerSizeChange;
	public int animalBirthsPerMutation;
	public int animalMutationsPerDietChange;
	public int animalMutationsPerSizeChange;

	static private SettingsSingleton settings = new SettingsSingleton();
	
	private SettingsSingleton() {}
	
	
    public static SettingsSingleton getInstance(){
    	return settings;
    }
    
    public void setMapSize(int mapCellsX, int mapCellsY) {
		this.mapCellsX = mapCellsX;
		this.mapCellsY = mapCellsY;
    }
   
    public void setPeriodicRecordings(int periodicRecordings) {
		this.periodicRecordings = periodicRecordings;
    }
    
    
    public void setPlantBirthsPerMutation(int plantBirthsPerMutation) {
		this.plantBirthsPerMutation = plantBirthsPerMutation;
    }
    public void setPlantMutationsPerDietChange(int plantMutationsPerDietChange) {
		this.plantMutationsPerDietChange = plantMutationsPerDietChange;
    }
    public void setPlantMutationsPerSizeChange(int plantMutationsPerSizeChange) {
		this.plantMutationsPerSizeChange = plantMutationsPerSizeChange;
    }
    public void setAnimalBirthsPerMutation(int animalBirthsPerMutation) {
		this.animalBirthsPerMutation = animalBirthsPerMutation;
    }
    public void setAnimalMutationsPerDietChange(int animalMutationsPerDietChange) {
		this.animalMutationsPerDietChange = animalMutationsPerDietChange;
    }
    public void setAnimalMutationsPerSizeChange(int animalMutationsPerSizeChange) {
		this.animalMutationsPerSizeChange = animalMutationsPerSizeChange;
    }
    
    
    
    
    public Genome getStarterGenome() {

		Genome genome = new Genome();

		genome.setDiet(Diet.PHOTOSYNTHESIS);
		
		genome.setSize(2);
		genome.setSpeed(0);
		genome.setPerception(0);
		genome.setStealth(0);
		genome.setAggression(0);
		genome.setReactiveness(0);
		genome.setAttackDamage(0);
		genome.setDefenseDamage(0);
		genome.setToughness(1);
		
		genome.setAgeExpectancy(2);
		//maxAge = *500 -> 500 - 5000
		
		genome.setFertility(4);
		//children = *2+1 -> 3 - 21
		
		genome.setClutchSize(2);
		
		genome.calculateAvailableEvoPoints();
		
		return genome;
    }
}