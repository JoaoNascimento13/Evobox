package application.dynamic;

import java.awt.Point;
import java.awt.geom.Point2D;

import application.core.RandomizerSingleton;
import application.core.MapStateSingleton;
import application.core.SettingsSingleton;

public class PreexistingCreatureFactory implements CreatureFactory {

	
	private Species species;

	@Override
	public Creature createCreature(long currentTick) {
		
		SettingsSingleton settings = SettingsSingleton.getInstance();
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();

		RandomizerSingleton randomizer = RandomizerSingleton.getInstance();
		
		int x = -1;
		int y = -1;
		
		while (true) {
			x = randomizer.nextInt(settings.mapCellsX);
			y = randomizer.nextInt(settings.mapCellsY);
			if (!mapState.hasCreature(x, y)) {
				break;
			}
		}
		
		Creature creature = new Creature(x, y);
		
		creature.setNextActivation((long)randomizer.nextInt(10));
		
		
		
		Genome genome = new Genome();

		genome.setDiet(Diet.PHOTOSYNTHESIS);
		
		genome.setSpeed(0);
		genome.setPerception(0);
		genome.setStealth(0);
		genome.setAgression(0);
		genome.setReactiveness(0);

		genome.setAttackDamage(0);
		genome.setDefenseDamage(0);
		genome.setToughness(1);
		
		genome.setAgeExpectancy(1000);
		genome.setFertility(8);
		genome.setClutchSize(2);
		
		creature.setSpecies(species);
		creature.setGenome(genome);
		
		
		creature.setMovementDecisionStrategy(new FloaterMovementDecision(creature));
		creature.setFeedingStrategy(new Photosynthesis(creature));
		creature.setReproductionStrategy(new SexualReproduction(creature));
		
		
		creature.setAge(randomizer.nextInt(1000));
		creature.setFood(
				((randomizer.nextInt(100)+50) * creature.feedingStrategy.getStartingFoodStorage())/100
				);
		creature.setFertility(false);
		

		System.out.println("creature species: " + creature.species);
		
		return creature;
	}
	
	
	public void setStarterSpecies(Species species) {
		System.out.println("species set for factory: " + species);
		this.species = species;
	}
	
	
	@Override
	public void setParents(Creature parentA, Creature parentB) {
	}

	@Override
	public void setSpawnPoint(Point spawnPoint) {
	}


}
