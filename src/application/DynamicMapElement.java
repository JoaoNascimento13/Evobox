package application;

import java.io.Serializable;

public abstract class DynamicMapElement implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	
	
	protected int speed;
	
	protected boolean activatesThisFrame(int frame) {
		return (frame % getActivationRemainderValue() == 0);
	}
	
	public int getActivationRemainderValue() {
		return (11-speed);
	}

	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
}
