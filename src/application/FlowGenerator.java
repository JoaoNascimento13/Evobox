package application;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;


public abstract class FlowGenerator extends DynamicMapElement {
	private static final long serialVersionUID = 1L;

	
	private int chanceOfDirectionChange = 2;

	private int slowdownFactor = 4;
	
	protected Point coords;
	protected Direction direction;
	
	protected ArrayList<Flow> currentFlow;
	
	transient protected Settings settings;

	public int getActivationRemainderValue() {
		return (11-speed)*slowdownFactor;
	}
	public int getFlowRemainderValue() {
		return (11-speed)*slowdownFactor*slowdownFactor;
	}
	
	public boolean flowsThisFrame(int frame) {
		return (frame % getFlowRemainderValue() == 0);
	}
	
	public void tick(int frame, Random randomizer, Point[][] flowMap) {
		
		if (activatesThisFrame(frame)) {
			
			tryDirectionChange(randomizer);

			removeFlow(flowMap);
			
			if (flowsThisFrame(frame)) {
				moveGenerator(direction);
			}

			modifyFlow();
			
			addFlow(flowMap);
		}
	}
	
	
	public void tryDirectionChange(Random randomizer) {
		
		if (randomizer.nextInt(100) < chanceOfDirectionChange) {
			if (randomizer.nextBoolean()) {
				direction = direction.clockwise();
			} else {
				direction = direction.counterClockwise();
			}
		}
	}

	public void removeFlow(Point[][] flowMap) {
	}
	public void moveGenerator(Direction dir) {
	}
	public void modifyFlow() {
	}
	public void addFlow(Point[][] flowMap) {
	}
	
	
	public Point getShiftedPointWithMapWrap(int pX, int deltaX, int pY, int deltaY) {
		int newX = pX + deltaX;
		if (newX >= settings.mapCellsX) {
			newX -= settings.mapCellsX;
		} else if (newX < 0) {
			newX += settings.mapCellsX;
		}
		int newY = pY + deltaY;
		if (newY >= settings.mapCellsY) {
			newY -= settings.mapCellsY;
		} else if (newY < 0) {
			newY += settings.mapCellsY;
		}
		return new Point(newX, newY);
	}

}
