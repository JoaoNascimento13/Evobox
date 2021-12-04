package application.core;

import application.dynamic.Diet;
import application.dynamic.Genome;

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
		
		genome.setSpeed(0);
		genome.setPerception(0);
		genome.setStealth(0);
		genome.setAgression(0);
		genome.setReactiveness(0);

		genome.setAttackDamage(0);
		genome.setDefenseDamage(0);
		genome.setToughness(1);
		
		genome.setAgeExpectancy(1000);
		genome.setFertility(8);
		genome.setClutchSize(2);
		
		return genome;
    }
}