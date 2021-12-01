package application.dynamic;

import java.io.Serializable;

public abstract class CreatureStrategy implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	public Creature creature;
	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
