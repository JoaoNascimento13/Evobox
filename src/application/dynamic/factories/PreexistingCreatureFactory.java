package application.dynamic.factories;

import java.awt.Point;

import application.core.MapStateSingleton;
import application.core.RandomizerSingleton;
import application.core.SettingsSingleton;
import application.dynamic.creatures.Creature;
import application.dynamic.creatures.Species;

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
		
		
		creature.setSpecies(species);
		creature.setGenome(species.getGenomeFromBase());
		
		creature.calculateAvailableEvoPoints();
		creature.setStrategiesFromGenome();
		
		
		creature.setAge(randomizer.nextInt(1000));
		creature.setFood(
				((randomizer.nextInt(100)+50) * creature.feedingStrategy.getStartingFoodStorage())/100
				);
		creature.setFullHealth();
		creature.setFertility(false);
		creature.setRandomFertilityCooldown();
		
		return creature;
	}
	
	
	public void setStarterSpecies(Species species) {
		this.species = species;
	}
	
	
	@Override
	public void setParents(Creature parentA, Creature parentB) {
	}

	@Override
	public void setSpawnPoint(Point spawnPoint) {
	}


}
