package application.core;

import java.util.ArrayList;

public class OutdatedPositionsSingleton {

	private ArrayList<Integer> outdatedCreaturePositionsX = new ArrayList<Integer>();
	private ArrayList<Integer> outdatedCreaturePositionsY = new ArrayList<Integer>();
	
	static private OutdatedPositionsSingleton outdatedPositions;
	
	private OutdatedPositionsSingleton() {}
	
    public static OutdatedPositionsSingleton getInstance(){
    	if (outdatedPositions == null) {
    		outdatedPositions = new OutdatedPositionsSingleton();
	    }
    	return outdatedPositions;
    }
    
    public void clearPositions() {
		this.outdatedCreaturePositionsX.clear();
		this.outdatedCreaturePositionsY.clear();
    }
   
    public void addCreaturePosition(int x, int y) {
		this.outdatedCreaturePositionsX.add(x);
		this.outdatedCreaturePositionsY.add(y);
    }
    
    public ArrayList<Integer> getOutdatedCreaturesX() {
    	return outdatedCreaturePositionsX;
    }
    public ArrayList<Integer> getOutdatedCreaturesY() {
    	return outdatedCreaturePositionsY;
    }
    
    
}
