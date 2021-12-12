package application.dynamic.factories;

import application.core.MapStateSingleton;
import application.dynamic.creatures.Genome;
import application.dynamic.creatures.Species;
import application.gui.SceneManagerSingleton;

public class SpeciesFactory {
	
	
	
	public Species createSpecies(Genome genome, Species parentSpecies, long originalParentIdA, long originalParentIdB) {

		Species species = new Species();

		species.setName();
		species.setBaseGenome(genome);
		species.setColor();

		
		MapStateSingleton.getInstance().registerSpecies(species);
		SceneManagerSingleton.getInstance().simulatorController.addSpeciesToOverview(species);
		
		species.parent = parentSpecies;
		
		if (parentSpecies != null) {
			parentSpecies.addChildren(species);
		}
		
//		species.originalParentIdA = originalParentIdA;
//		species.originalParentIdB = originalParentIdB;
		
		return species;
	}
}
