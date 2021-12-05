package application.dynamic.flow;


import java.awt.Point;
import java.io.Serializable;

import application.core.Direction;
import application.core.MapStateSingleton;
import application.core.SettingsSingleton;

public class Flow implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	
	public int x;
	public int y;
	public int valX;
	public int valY;
	
	public Flow(int x, int y, int valX, int valY) {
		this.x = x;
		this.y = y;
		this.valX = valX;
		this.valY = valY;
	}
	
	
	public void moveInMap (Direction dir, SettingsSingleton settings) {
		
		removeFromMap();
		
		moveFlowCoords(dir);
		
		addToMap();
	}
	
	public void removeFromMap() {
		Point[][] flowMap = MapStateSingleton.getInstance().getFlowMap();
		flowMap[x][y].x -= valX;
		flowMap[x][y].y -= valY;
	}
	public void removeFromMapWithCoef(int percent) {
		Point[][] flowMap = MapStateSingleton.getInstance().getFlowMap();
		flowMap[x][y].x -= (valX * percent)/100;
		flowMap[x][y].y -= (valY * percent)/100;
	}
	
	public void moveFlowCoords(Direction dir) {
		SettingsSingleton settings = SettingsSingleton.getInstance();
		this.x += dir.x;
		if (this.x >= settings.mapCellsX) {
			this.x -= settings.mapCellsX;
		} else if (this.x < 0) {
			this.x += settings.mapCellsX;
		}
		this.y += dir.y;
		if (this.y >= settings.mapCellsY) {
			this.y -= settings.mapCellsY;
		} else if (this.y < 0) {
			this.y += settings.mapCellsY;
		}
	}
	
	public void addToMap() {
		Point[][] flowMap = MapStateSingleton.getInstance().getFlowMap();
		flowMap[x][y].x += valX;
		flowMap[x][y].y += valY;
	}
	public void addToMapWithCoef(int percent) {
		Point[][] flowMap = MapStateSingleton.getInstance().getFlowMap();
		flowMap[x][y].x += (valX * percent)/100;
		flowMap[x][y].y += (valY * percent)/100;
	}
	
	@Override
    public String toString() {
        return ("Flow on (" + x + ", " + y + ") -> (" + valX + ", " + valY + ")");
    }

	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}