package application.dynamic.factories;

import java.awt.Point;

import application.core.MapStateSingleton;
import application.dynamic.creatures.Creature;
import application.dynamic.creatures.Genome;

public class SexualReproductionCreatureFactory implements CreatureFactory {

	
	
	
	private Creature parentA;
	private Creature parentB;
	private Point spawnPoint;

	public Creature createCreature(long currentTick) {
		
//		SettingsSingleton settings = SettingsSingleton.getInstance();
//		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		Creature creature = new Creature(spawnPoint.x, spawnPoint.y);
		
		creature.setNextActivation(currentTick+1);
		
		
		Genome genome = parentA.genome.recombineWith(parentB.genome);
		
//		genome.exposeToMutation();
		
		if (genome.exposeToMutation()) {
			
			creature.mutated = true;
			
		}

				
		creature.setSpecies(parentA.species);
		
		creature.setGenome(genome);
		
		
		creature.setStrategiesFromGenome();
		
		
		creature.setAge(0);
		
		creature.setFood(creature.feedingStrategy.getStartingFoodStorage());

		creature.setFullHealth();
		
		creature.setFertility(false);
		
		
		return creature;
	}

	@Override
	public void setParents(Creature parentA, Creature parentB) {
		this.parentA = parentA;
		this.parentB = parentB;
		
	}

	@Override
	public void setSpawnPoint(Point spawnPoint) {
		this.spawnPoint = spawnPoint;
	}


}
