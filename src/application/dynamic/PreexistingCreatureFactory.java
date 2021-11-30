package application.dynamic;

import application.core.CloneableRandom;
import application.core.SettingsSingleton;

public class PreexistingCreatureFactory implements CreatureFactory {

	
	
	
	public Creature createCreature(CloneableRandom randomizer) {
		
		SettingsSingleton settings = SettingsSingleton.getInstance();
		
		Creature creature = new Creature(randomizer.nextInt(settings.mapCellsX), randomizer.nextInt(settings.mapCellsY));
		
		creature.setNextActivation((long)randomizer.nextInt(10));
		
		creature.setMoveStrategy(new FloaterMoveStrategy(creature));
		
		return creature;
	}

}
