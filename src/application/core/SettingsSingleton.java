package application.core;

import application.dynamic.creatures.Diet;
import application.dynamic.creatures.Genome;
import application.dynamic.creatures.Size;

public class SettingsSingleton {
	
	public int periodicRecordings;
	public int mapCellsX;
	public int mapCellsY;

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
   
    
    
    public Genome getStarterGenome() {

		
		Genome genome = new Genome();

		genome.setDiet(Diet.PHOTOSYNTHESIS);
		
		genome.setSize(Size.SMALL);
		
		genome.setSpeed(0);
		
		genome.setPerception(0);
		
		genome.setStealth(0);
		genome.setAgression(0);
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