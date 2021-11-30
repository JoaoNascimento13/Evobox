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
		
		creature.setMoveStrategy(new FloaterMoveStrategy(creature));
		
		return creature;
	}

}
