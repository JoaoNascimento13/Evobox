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
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();

		RandomizerSingleton randomizer = RandomizerSingleton.getInstance();

		if (parentA.species.id == parentB.species.id) {
			
			genome = parentA.genome.recombineWith(parentB.genome, parentA.species.baseGenome);
			

			Genome possiblyMutatedGenome = genome.getPossiblyMutatedGenome(parentA.genome.diet == parentB.genome.diet);
			
			//creature.setSpecies(parentA.species);
			
			boolean equalToExistingSpecies = false;
			
			
			if (possiblyMutatedGenome.checkForMutationAndNewSpecies(creature, parentA.species)) {
				
				
				for (Species s : mapState.activeSpecies) {
					
					if (s.baseGenome.sameAs(possiblyMutatedGenome)) {
						
						equalToExistingSpecies = true;
						
						//Note: In this case, the mutated genome turned out to be similar to an already existing species.
						//		This is natural and expected, but would overly crowd the simulation 
						//		with too many identical species.
						//		Instead, we just reject this mutation, and keep the recombined, non-mutated genome.
						
						break;
					}
				}
				
				
				int maxTurnsToReduceSpeciesChance = 10000;
				
				long lastTurnForSpeciesChanceReduction = mapState.turn - maxTurnsToReduceSpeciesChance;
				
				Species s;
				for (int i = mapState.extinctSpecies.size()-1; i > -1; i--) {	
					s = mapState.extinctSpecies.get(i);
					
					if (s.extinctionTurn > lastTurnForSpeciesChanceReduction &&
						s.baseGenome.sameAs(possiblyMutatedGenome)) {

						//Note: In this case, the mutated genome turned out to be similar to an extinct species.
						//		To keep unsustainable species from reappearing and going extinct over and over,
						//		we reduce the chance of them reappearing right after extinction.
						//
						// Species creation chance, based on turns since extinction:	
						//
						//	Turns since extinction             Chance
						//
						//	0                              --- 0
						//
						//	mapState.turn - extinctionTurn --- ?
						//		
						//	maxTurnsToReduceSpeciesChance  --- 1000
						//
						//We start from the end of the extinct species list, so more recently extinct species are checked first.
						//We stop after the first match, regardless of result.
						
						int chanceOfSkippingPermilage = (int) ((1000*(mapState.turn - s.extinctionTurn))/maxTurnsToReduceSpeciesChance);
						
						if (randomizer.nextInt(1000) >= chanceOfSkippingPermilage) {
							equalToExistingSpecies = true;
						}
						break;
					}
				}
				
				
				
				if (!equalToExistingSpecies) {

					//The mutation caused a unique new species, so we keep it.

					creature.setSpecies(new SpeciesFactory().createSpecies(possiblyMutatedGenome, parentA.species, parentA.id, parentB.id));
					creature.setGenome(possiblyMutatedGenome);
					
					System.out.println("New species: " + creature.species.name);
					System.out.println("Parent species: " + creature.species.parent.name);
					
//					System.out.println("Parent species genomes: ");
//					System.out.println(creature.species.parent.baseGenome.toString());
//					System.out.println("Parent A: " + parentA);
//					System.out.println("Parent A genome: ");
//					System.out.println(parentA.genome.toString());
//					System.out.println("Parent B: " + parentB);
//					System.out.println("Parent B genome: ");
//					System.out.println(parentB.genome.toString());
//					System.out.println("This genome: ");
					System.out.println(creature.species.baseGenome.toString());
					System.out.println();
					
				} else {
					
					//The mutation would have cause a new species identical to an existing one, so we ignore it.

					creature.setSpecies(parentA.species);
					creature.setGenome(genome);
					
				}

				
			} else {
				
				//The (possible) mutation did not cause a new species

				creature.setSpecies(parentA.species);
				creature.setGenome(possiblyMutatedGenome);
				
			}
			
			
		} else {

			//The parents were from different species, so no recombination or mutation happens, 
			//the new creature is a copy of one of them.
			//Since one species will always be a descendent of another, we pick the descendent species.

			if (parentA.species.parent.id == parentB.species.id) {
				creature.setGenome(parentA.genome.getCopyOfGenome());
				creature.setSpecies(parentA.species);
			} else {
				creature.setGenome(parentB.genome.getCopyOfGenome());
				creature.setSpecies(parentB.species);
			}
			
//			if (RandomizerSingleton.getInstance().nextBoolean()) {
//				creature.setGenome(parentA.genome.getCopyOfGenome());
//				creature.setSpecies(parentA.species);
//			} else {
//				creature.setGenome(parentB.genome.getCopyOfGenome());
//				creature.setSpecies(parentB.species);
//			}
		}
		
		
//		if (parentA.species.id == parentB.species.id && 
//			genome.checkForMutationAndNewSpecies(creature)) {
//			
//			//New species occur when the BASE genome (after recombination, but before mutations)
//			//differs enough from the original species genome.
//
//			//Note: We decided to not allow the children of creatures from different species 
//			//      to become the first ones of that species.
//			//      This is because these creatures get a complete copy of one of the parents
//			//      (in order to allow newly-formed species to mate with their parent species
//			//		and still maintain their genetic traits).
//			//		However that could also cause a mutation to pass directly to the offspring
//			//      without recombination, originating a new species that shouldn't exist.
//			
//			createNewSpeciesOrAddToEquivalent(creature, genome);
//			
//			
//		} else {
//			
//			genome.exposeToMutation(parentA.genome.diet == parentB.genome.diet);
//				
//			//Genome has a chance to mutate from parents.
//			//This can only happen if this isn't the first creature of the species.
//			//That means, the first creature of the species always has the same genome as the base genome for the species.
//			//Note that even if it doesn't mutate, the creature could still have mutations from the base species
//			//(inherited from its parents).
//			
//			
//			//Note: Diet changes immediately result in a new creature, unlike other changes
//			//		which need to be inherited.
//			
//			if (genome.diet != creature.species.baseGenome.diet) {
//				createNewSpeciesOrAddToEquivalent(creature, genome);
//			}
//			
//		}
		
		
		
		
		creature.setStrategiesFromGenome();
		
		
		creature.setAge(0);
		
		creature.setFood(creature.feedingStrategy.getStartingFoodStorage());

		creature.setFullHealth();
		
		creature.setFertility(false);
		

		
		return creature;
	}

	

	public void setChildAsCopyOfEitherParent(Creature creature) {
		if ( RandomizerSingleton.getInstance().nextBoolean()) {
			creature.species = parentA.species;
		} else {
			creature.species = parentB.species;
		}
	}
	
	
//	public void createNewSpeciesOrAddToEquivalent(Creature creature, Genome genome) {
//		
//		//Note: Here we do 2 checks to avoid create a vast number of functionally equal species.
//		
//		boolean speciesAlreadyExists = false;
//		for (Species s : MapStateSingleton.getInstance().activeSpecies) {
//
//			//      If a new species spawns from 2 parents, any further "new species" spawned from either of these parents
//			//		should be considered part of the first new species.
//			//		Otherwise, a strongly-mutated member could reproduce with non-mutated members
//			//		and produce a lot of "new species", all equal to each other.
//			
//			if (s.originalParentIdA == parentA.id || s.originalParentIdA == parentB.id || 
//				s.originalParentIdB == parentA.id || s.originalParentIdB == parentB.id) {
//				
//				creature.setSpecies(s);
//				speciesAlreadyExists = true;
//				break;
//			}
//			
//			//		If the species is functionally equal to another species, we just don't allow the 
//			//
//			//
//			//
//			//		DEPRECATED: If this new species descends from the same parent species 
//			//		and has the exact same genome as a different species, then this creature is also considered
//			//		to be part of that first new species, and not a different one.
//			
//			if (s.parent.id == parentA.species.id && s.baseGenome.sameAs(genome)) {
//				
//				creature.setSpecies(s);
//				speciesAlreadyExists = true;
//				break;
//			}
//		}
//		
//		if (!speciesAlreadyExists) {
//			
//			creature.setSpecies(new SpeciesFactory().createSpecies(genome, parentA.species, parentA.id, parentB.id));
//			
//			//If species A and B must be the same, it doesn't matter which one we pick.
//			
//			System.out.println("New species: " + creature.species.name);
//			System.out.println("Parent species: " + creature.species.parent.name);
//			
////			System.out.println("Parent species genomes: ");
////			System.out.println(creature.species.parent.baseGenome.toString());
////			System.out.println("Parent A: " + parentA);
////			System.out.println("Parent A genome: ");
////			System.out.println(parentA.genome.toString());
////			System.out.println("Parent B: " + parentB);
////			System.out.println("Parent B genome: ");
////			System.out.println(parentB.genome.toString());
////			System.out.println("This genome: ");
//			System.out.println(creature.species.baseGenome.toString());
//			System.out.println();
//		}
//	}
	
	
	
	
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
