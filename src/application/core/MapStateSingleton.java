package application.core;

import java.awt.Point;

import application.dynamic.Creature;

public class MapStateSingleton {

	private Point[][] flowMap;
	private Creature[][] creatureMap;

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
    	initializeCreatureMap();
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
    public void initializeCreatureMap() {
		SettingsSingleton settings = SettingsSingleton.getInstance();
		creatureMap = new Creature[settings.mapCellsX][settings.mapCellsY];
    }
   
   

    public boolean hasCreature(int x, int y) {
    	return (creatureMap[x][y] != null);
    }
    public void clearCreature(Creature creature) {
   	 creatureMap[creature.x][creature.y] = null;
   }
    public void clearCreatureFrom(int x, int y) {
    	creatureMap[x][y] = null;
    }
    public void setCreature(Creature creature) {
    	 creatureMap[creature.x][creature.y] = creature;
    }
    public Creature getCreature(int x, int y) {
    	return creatureMap[x][y];
    }
}