package application;
import java.io.Serializable;
import java.util.ArrayList;

public class SimulationState implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	
	public ArrayList<Creature> creatures;
	public CloneableRandom randomizer;
	public int frame;
	public int simulationNumber;
	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
	public SimulationState(int simulationNumber, ArrayList<Creature> creatures, CloneableRandom randomizer, int frame) {
		this.simulationNumber = simulationNumber;
		this.creatures = creatures;
		this.randomizer = randomizer;
		this.frame = frame;
	}
	
}
