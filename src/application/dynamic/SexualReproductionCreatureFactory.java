package application.dynamic;

import java.awt.Point;
import application.core.CloneableRandom;
import application.core.MapStateSingleton;
import application.core.SettingsSingleton;

public class SexualReproductionCreatureFactory implements CreatureFactory {

	
	
	
	private Creature parentA;
	private Creature parentB;
	private Point spawnPoint;

	public Creature createCreature(CloneableRandom randomizer, long currentTick) {
		
//		SettingsSingleton settings = SettingsSingleton.getInstance();
//		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		Creature creature = new Creature(spawnPoint.x, spawnPoint.y);
		
		creature.setNextActivation(currentTick+1);
		
		
		
		Genome genome = new Genome();

		
		//TODO: all this should be set based on parents' genomes and mutation
				
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
				creature.setReproductionStrategy(new SexualReproduction(creature));
				
				creature.setAge(0);
				
				creature.setFood(
						((randomizer.nextInt(100)+50) * creature.feedingStrategy.getStartingFoodStorage())/100
						);
				
//				creature.setFood(creature.feedingStrategy.getStartingFoodStorage());
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
