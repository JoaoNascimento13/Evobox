package application.dynamic.flow;

import java.util.ArrayList;
import java.util.Random;

import application.core.Direction;
import application.core.SettingsSingleton;

import java.awt.Point;
import java.io.Serializable;


public abstract class FlowGenerator  implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private int chanceOfDirectionChange = 2;

	private int slowdownFactor = 4;
	
	protected Point coords;
	protected Direction direction;
	
	protected ArrayList<Flow> currentFlow;

	protected int speed;
	

	public int getActivationRemainderValue() {
		return (11-speed)*slowdownFactor;
	}
	public int getFlowRemainderValue() {
		return (11-speed)*slowdownFactor*slowdownFactor;
	}

	
	protected boolean activatesThisFrame(long tick) {
		return (tick % getActivationRemainderValue() == 0);
	}
	public boolean flowsThisFrame(long tick) {
		return (tick % getFlowRemainderValue() == 0);
	}
	
	public void tick(long tick, Random randomizer) {
		
		if (activatesThisFrame(tick)) {
			
			tryDirectionChange(randomizer);

			removeFlow();
			
			if (flowsThisFrame(tick)) {
				moveGenerator(direction);
			}

			modifyFlow();
			
			addFlow();
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

	public void removeFlow() {
	}
	public void moveGenerator(Direction dir) {
	}
	public void modifyFlow() {
	}
	public void addFlow() {
	}
	
	
	public Point getShiftedPointWithMapWrap(int pX, int deltaX, int pY, int deltaY) {
		int newX = pX + deltaX;
		SettingsSingleton settings = SettingsSingleton.getInstance();
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

	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
