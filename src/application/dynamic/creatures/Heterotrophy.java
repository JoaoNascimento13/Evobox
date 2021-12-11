package application.dynamic.creatures;

import application.core.Direction;
import application.core.MapStateSingleton;
import application.core.RandomizerSingleton;

public class Heterotrophy extends FeedingStrategy  {
	private static final long serialVersionUID = 1L;
	
	public Heterotrophy (Creature creature) {
		this.creature = creature;
	}
	
	@Override
	public void exposeToFeeding() {
		
		if (creature.goal != CreatureGoal.FEED || 
			creature.targetCreature == null ||
			creature.food >= getMaximumFoodStorage() ||
			Math.abs(creature.x - creature.targetCreature.x) > 1 || 
			Math.abs(creature.y - creature.targetCreature.y) > 1) {
			return;
		}
		
		
		attack();
		
		if (creature.targetCreature.health > 0) {
			
			takeReturnDamage();
			
		} else {
			
			if (RandomizerSingleton.getInstance().nextBoolean()) {
				takeReturnDamage();
			}
			consumeTarget();
			clearTargetAndGoal();
		}
	}

	

	private void attack() {
		creature.targetCreature.takeDamage(creature.genome.getAttack());
	}


	private void takeReturnDamage() {
		creature.takeDamage(creature.targetCreature.genome.getDefense());
	}
	

	private void consumeTarget() {
		creature.food += 200 * creature.targetCreature.genome.getSize();
	}

	private void clearTargetAndGoal() {
		creature.goal = null;
		creature.targetCreature = null;
		creature.nextGoalChange = MapStateSingleton.getInstance().tick + 1;
	}
	
	
	@Override
	public int getFoodNeededPerTickModifier() {
		return creature.genome.getSize();
	}

	@Override
	public int getStartingFoodStorage() {
		return 250 * creature.genome.getSize();
	}
	
	@Override
	public int getMaximumFoodStorage() {
		//TODO: should take into account creature size, once implemented;
		return 500 * creature.genome.getSize();
	}
	

	
	
	
	
}
