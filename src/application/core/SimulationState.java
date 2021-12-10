package application.core;
import java.io.Serializable;
import java.util.ArrayList;

import application.dynamic.creatures.Creature;
import application.dynamic.flow.FlowGenerator;

public class SimulationState implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	
	public ArrayList<Creature> creatures;
	public ArrayList<FlowGenerator> flowGenerators;
	public RandomizerSingleton randomizer;
	public int simulationNumber;
	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
	public SimulationState(int simulationNumber, 
			ArrayList<Creature> creatures, 
			ArrayList<FlowGenerator> flowGenerators, 
			RandomizerSingleton randomizer) {
		this.simulationNumber = simulationNumber;
		this.creatures = creatures;
		this.flowGenerators = flowGenerators;
		this.randomizer = randomizer;
	}
	
}
