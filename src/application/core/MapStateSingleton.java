package application.core;

import java.awt.Point;
import java.util.ArrayList;

import application.dynamic.Creature;
import application.dynamic.FlowGenerator;

public class MapStateSingleton {

	private Point[][] flowMap;
	private Creature[][] creatureMap;

	public ArrayList<Creature> creatures;
	
	public ArrayList<Long> deadCreaturesToRemoveIds;
	
	public ArrayList<FlowGenerator> flowGenerators;
	

	public long nextCreatureId;
	
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
    	
    	nextCreatureId = 1;
		creatures = new ArrayList<Creature>();
		deadCreaturesToRemoveIds = new ArrayList<Long>();
		flowGenerators = new ArrayList<FlowGenerator>();
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
   
   

    public void registerCreature(Creature creature) {
    	creature.id = nextCreatureId;
    	nextCreatureId++;
    	creatures.add(creature);
    }

    public void queueCreatureUnregister(Creature creature) {
    	deadCreaturesToRemoveIds.add(creature.id);
    }
    public void unregisterDeadCreatures() {
    	for (long id : deadCreaturesToRemoveIds) {
    		for (int i = 0; i < creatures.size(); i++) {
        		if (creatures.get(i).id == id) {
        			creatures.remove(i);
        			break;
        		}
    		}
    	}
    }
    public void unregisterCreature(Creature creature) {
    	for (int i = 0; i < creatures.size(); i++) {
    		if (creatures.get(i).id == creature.id) {
    			creatures.remove(i);
    			break;
    		}
    	}
    }

    public boolean isEmpty(int x, int y) {
//		System.out.println("testing empty: " + x + ", " + y);
    	return (creatureMap[x][y] == null);
    }
    public boolean hasCreature(int x, int y) {
    	return (!isEmpty(x, y));
    }
    public void clearCreature(Creature creature) {
    	creatureMap[creature.x][creature.y] = null;
    }
    public void clearCreatureFrom(int x, int y) {
    	creatureMap[x][y] = null;
    }
    public void setCreatureInPoint(Creature creature) {
    	if (hasCreature(creature.x, creature.y)) {
    		System.out.println("OVERLAPPING CREATURES");
    		int crash = 1/0;
    	}
    	 creatureMap[creature.x][creature.y] = creature;
    }
    public Creature getCreature(int x, int y) {
    	return creatureMap[x][y];
    }
}