package application;

import java.io.Serializable;

public class Creature implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public int x;
	public int y;
	
	public Creature (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
}
