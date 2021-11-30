package application.core;

import java.util.Random;

public class CloneableRandom extends Random implements Cloneable {
	private static final long serialVersionUID = 1L;

	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
