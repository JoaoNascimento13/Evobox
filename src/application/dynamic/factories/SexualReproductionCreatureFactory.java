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
			
			genome = parentA.genome.recombineWith(parentB.genome, parentA.species.baseGenome);
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
			
			createNewSpeciesOrAddToEquivalent(creature, genome);
			
			
		} else {
			
			genome.exposeToMutation(parentA.genome.diet == parentB.genome.diet);
				
			//Genome has a chance to mutate from parents.
			//This can only happen if this isn't the first creature of the species.
			//That means, the first creature of the species always has the same genome as the base genome for the species.
			//Note that even if it doesn't mutate, the creature could still have mutations from the base species
			//(inherited from its parents).
			
			
			//Note: Diet changes immediately result in a new creature, unlike other changes
			//		which need to be inherited.
			
			if (genome.diet != creature.species.baseGenome.diet) {
				createNewSpeciesOrAddToEquivalent(creature, genome);
			}
			
		}
		
		
		creature.setGenome(genome);
		
		
		creature.setStrategiesFromGenome();
		
		
		creature.setAge(0);
		
		creature.setFood(creature.feedingStrategy.getStartingFoodStorage());

		creature.setFullHealth();
		
		creature.setFertility(false);
		

		
		return creature;
	}

	
	
	public void createNewSpeciesOrAddToEquivalent(Creature creature, Genome genome) {

		boolean speciesAlreadyExists = false;
		for (Species s : MapStateSingleton.getInstance().activeSpecies) {

			//Note: If a new species spawns from 2 parents, any further "new species" spawned from either of these parents
			//		should be considered part of the first new species.
			//		Otherwise, a strongly-mutated member could reproduce with non-mutated members
			//		and produce a lot of "new species", all equal to each other.
			
			if (s.originalParentIdA == parentA.id || s.originalParentIdA == parentB.id || 
				s.originalParentIdB == parentA.id || s.originalParentIdB == parentB.id) {
				
				creature.setSpecies(s);
				speciesAlreadyExists = true;
				break;
			}
			
			//Note: Aditionally to the above, if this new species descends from the same parent species 
			//		and has the exact same genome as a different species, then this creature is also considered
			//		to be part of that first new species, and not a different one.
			
			if (s.parent.id == parentA.species.id && s.baseGenome.sameAs(genome)) {
				
				creature.setSpecies(s);
				speciesAlreadyExists = true;
				break;
			}
		}
		
		if (!speciesAlreadyExists) {
			
			creature.setSpecies(new SpeciesFactory().createSpecies(genome, parentA.species, parentA.id, parentB.id));
			
			//If species A and B must be the same, it doesn't matter which one we pick.
			
			System.out.println("New species: " + creature.species.name);
			System.out.println("Parent species: " + creature.species.parent.name);
			
//			System.out.println("Parent species genomes: ");
//			System.out.println(creature.species.parent.baseGenome.toString());
//			System.out.println("Parent A: " + parentA);
//			System.out.println("Parent A genome: ");
//			System.out.println(parentA.genome.toString());
//			System.out.println("Parent B: " + parentB);
//			System.out.println("Parent B genome: ");
//			System.out.println(parentB.genome.toString());
//			System.out.println("This genome: ");
			System.out.println(creature.species.baseGenome.toString());
			System.out.println();
		}
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
