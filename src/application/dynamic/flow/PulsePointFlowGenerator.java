package application.dynamic.flow;


import java.awt.Point;
import java.util.ArrayList;

import application.core.Direction;

public class PulsePointFlowGenerator extends FlowGenerator {
	private static final long serialVersionUID = 1L;

	private int maxFlow;
	
	
	private int pulseStage;
	private boolean pulseReversed;


	private Flow centralPointFlow;
	
	public PulsePointFlowGenerator(int maxFlow, int speed, Point startingCoords, 
			int startPulseStage, boolean pulseReversed, Direction startDir) {

		this.maxFlow = maxFlow;
		this.speed = Math.max(1, Math.min(10, speed));
		this.coords = startingCoords;
		this.pulseStage = startPulseStage;
		this.direction = startDir;
		this.currentFlow = new ArrayList<Flow>();
		
		calculateBaseFlow ();
	}

	public void removeFlow() {
		int percent = getFlowCoef();
		if (percent != 0) {
			for (Flow f : currentFlow) {
				f.removeFromMapWithCoef(percent);
//				f.removeFromMap(flowMap);
			}
		}
		if (centralPointFlow != null) {
			centralPointFlow.removeFromMap();
		}
	}
	public void moveGenerator(Direction dir) {
		for (Flow f : currentFlow) {
			f.moveFlowCoords(dir);
		}
		coords = getShiftedPointWithMapWrap(coords.x, dir.x, coords.y, dir.y);
	}
	
	public void addFlow() {
		int percent = getFlowCoef();
		if (percent != 0) {
			for (Flow f : currentFlow) {
				f.addToMapWithCoef(percent);
//				f.addToMap(flowMap);
			}
			if (percent > 0) {
				centralPointFlow = new Flow(coords.x, coords.y, 100, 100);
				centralPointFlow.addToMap();
			} else {
				centralPointFlow = null;
			}
		}
	}

	public int getFlowCoef() {
		int percent = (int) Math.sqrt(50*Math.abs(pulseStage));
		if (pulseStage < 0) {
			percent = -1*percent;
		}
		return percent;
	}
	
	
	public void modifyFlow() {
		
		int maxPulse = 10;

		if (pulseStage == maxPulse) {
			pulseReversed = true;
		} else if (pulseStage == -1*maxPulse) {
			pulseReversed = false;
		}
		
		if (pulseReversed) {
			pulseStage = pulseStage - 1;
		} else {
			pulseStage = pulseStage + 1;
		}
		
//		System.out.println(pulseStage);
		
		//spreadFlow();
	}
	
	
	public void calculateBaseFlow () {

//		System.out.println("calculateBaseFlow...");
		
		
		currentFlow = new ArrayList<Flow>();
		
		
		for (Direction d : Direction.DIAGONALS) {
			
			ArrayList<Point> pointsToSpreadFromInDir = new ArrayList<Point>();

			ArrayList<Point> pointsToSpreadFromInDirNext = new ArrayList<Point>();
			
			
			pointsToSpreadFromInDir.add(new Point(coords));
			
			
			int pulse = maxFlow;
			
			
			
			while (true) {
				
				pointsToSpreadFromInDirNext.add(getShiftedPointWithMapWrap(
						pointsToSpreadFromInDir.get(0).x, 0, pointsToSpreadFromInDir.get(0).y, d.y));
				
				
				for (Point p : pointsToSpreadFromInDir) {
					
					//Note: points along the axis are repeated on adjacent quadrants.
					//To avoid repeting them, we only add them when checking the DOWN_RIGHT and UP_LEFT dirs.
					
					
					if ((d.x > 0 && d.y > 0) || (d.x < 0 && d.y < 0) || (p.x != coords.x && p.y != coords.y)) {

						
						if (p.x == 160 && p.y == 165) {
							
//							System.out.println("Found!");
//							
//							System.out.println(getBaseFlowOnPoint(p, pulse, d).toString());
						}
						
						
						currentFlow.add(getBaseFlowOnPoint(p, pulse, d));
						
					}
					
					
					//new Point(p.x + d.x, p.y)
					
					pointsToSpreadFromInDirNext.add(getShiftedPointWithMapWrap(p.x, d.x, p.y, 0));
									
				}
				
				pulse -= 1;
				if (pulse <= 0) {
					break;
				}
				
				pointsToSpreadFromInDir.clear();
				pointsToSpreadFromInDir.addAll(pointsToSpreadFromInDirNext);
				pointsToSpreadFromInDirNext.clear();
				
			}
			
		}
		
	}


	public Flow getBaseFlowOnPoint(Point flowPoint, int pulse, Direction dir) {
		
		int deltaX = Math.abs(flowPoint.x - coords.x);
		int deltaY = Math.abs(flowPoint.y - coords.y);

		double delta = 0;
		
		if (deltaX == 0) {
			delta = deltaY;
			return new Flow(flowPoint.x, flowPoint.y, 0, pulse*dir.y);
					
		} else if (deltaY == 0) {
			delta = deltaX;

			return new Flow(flowPoint.x, flowPoint.y, pulse*dir.x, 0);
			
		} else {
			delta = StrictMath.hypot(deltaX, deltaY);
		}
		
		
		int pulseX = (int) Math.ceil((pulse * deltaX) / delta);
		int pulseY = (int) Math.ceil((pulse * deltaY) / delta);
		
		return new Flow(flowPoint.x, flowPoint.y, pulseX*dir.x, pulseY*dir.y);
	}
	
}
