package application.dynamic;

import application.core.CloneableRandom;
import application.core.MapStateSingleton;
import application.core.SettingsSingleton;

public class PreexistingCreatureFactory implements CreatureFactory {

	
	
	
	public Creature createCreature(CloneableRandom randomizer) {
		
		SettingsSingleton settings = SettingsSingleton.getInstance();
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
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
		
		creature.setGenome(genome);
		
		
		creature.setMovementDecisionStrategy(new FloaterMovementDecision(creature));
		creature.setFeedingStrategy(new Photosynthesis(creature));

		
		creature.setAge(randomizer.nextInt(200));
		creature.setFood(creature.feedingStrategy.getStartingFoodStorage());
		
		return creature;
	}

}
