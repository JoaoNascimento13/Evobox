package application.core;

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
   
}