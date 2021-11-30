package application.dynamic;

import java.io.Serializable;

import application.core.CloneableRandom;

public class Creature implements Cloneable, Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	public int x;
	public int y;
	
	public int speed;
	
	public long nextActivation;
	
	public Creature (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	private MoveStrategy moveStrategy;
	
	
	
	
	public void actOrPostpone(long currentTick, CloneableRandom randomizer) {
		if (this.nextActivation == currentTick) {
			act(randomizer);
			nextActivation = currentTick + (11-this.speed);
		}
	}
	

	public void act(CloneableRandom randomizer) {
		move(randomizer);
	}
	
	
	
	
	
	public void move(CloneableRandom randomizer) {
		this.moveStrategy.move(randomizer);
	}




	public void setNextActivation(long nextActivation) {
		this.nextActivation = nextActivation;
	}
	
	
	public void setMoveStrategy(MoveStrategy moveStrategy) {
		this.moveStrategy = moveStrategy;
	}

	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
