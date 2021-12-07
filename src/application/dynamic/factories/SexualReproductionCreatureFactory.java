package application.dynamic.factories;

import java.awt.Point;

import application.core.MapStateSingleton;
import application.core.RandomizerSingleton;
import application.dynamic.creatures.Creature;
import application.dynamic.creatures.Genome;
import application.dynamic.creatures.Species;

public class SexualReproductionCreatureFactory implements CreatureFactory {

	
	
	
	private Creature parentA;
	private Creature parentB;
	private Point spawnPoint;

	public Creature createCreature(long currentTick) {
		
//		SettingsSingleton settings = SettingsSingleton.getInstance();
//		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		Creature creature = new Creature(spawnPoint.x, spawnPoint.y);
		
		creature.setNextActivation(currentTick+1);
		
		
		Genome genome = null;
		
		

		if (parentA.species.id == parentB.species.id) {
			
			genome = parentA.genome.recombineWith(parentB.genome);
			creature.setSpecies(parentA.species);
			
		} else {

			if (RandomizerSingleton.getInstance().nextBoolean()) {
				genome = parentA.genome.getCopyOfGenome();
				creature.setSpecies(parentA.species);
			} else {
				genome = parentB.genome.getCopyOfGenome();
				creature.setSpecies(parentB.species);
			}
			
		}
		
		
		if (genome.checkForMutationAndNewSpecies(creature) && 
			parentA.species.id == parentB.species.id) {
			
			//New species occur when the BASE genome (after recombination, but before mutations)
			//differs enough from the original species genome.

			//Note: We decided to not allow the children of creatures from different species 
			//      to become the first ones of that species.
			//      This is because these creatures get a complete copy of one of the parents
			//      (in order to allow newly-formed species to mate with their parent species
			//		and still maintain their genetic traits).
			//		However that could also cause a mutation to pass directly to the offspring
			//      without recombination, originating a new species that shouldn't exist.
			
			
			creature.setSpecies(new Species(genome));
			
			//If species A and B are the same, it doesn't matter which one we pick.
			
			creature.species.parent = parentA.species;
			
			creature.species.parent.children.add(creature.species);
			
			System.out.println("New species: " + creature.species.name);
			
		} else {
			
			genome.exposeToMutation();
				
				//Genome has a chance to mutate from parents.
				//This can only happen if this isn't the first creature of the species.
				//That means, the first creature of the species always has the same genome as the base genome for the species.
				//Note that even if it doesn't mutate, the creature could still have mutations from the base species
				//(inherited from its parents).
		}
		
		
				
		
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
