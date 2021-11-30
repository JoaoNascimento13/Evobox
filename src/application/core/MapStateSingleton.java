package application.core;

import java.awt.Point;

public class MapStateSingleton {

	private Point[][] flowMap;

	static private MapStateSingleton mapState;
	
	private MapStateSingleton() {}
	
	
    public static MapStateSingleton getInstance(){
    	if (mapState == null) {
    		mapState = new MapStateSingleton();
	    }
    	return mapState;
    }



    public Point[][] getFlowMap() {
    	return flowMap;
    }
    
    
    public void initialize() {
    	initializeFlowMap();
    }
    
    public void initializeFlowMap() {
		SettingsSingleton settings = SettingsSingleton.getInstance();
		flowMap = new Point[settings.mapCellsX][settings.mapCellsY];
		for (int i = 0; i < settings.mapCellsX; i++) {
			for (int j = 0; j < settings.mapCellsX; j++) {
				flowMap[i][j] = new Point(0, 0);
			}
		}
    }
   
   
}