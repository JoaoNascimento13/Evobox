package application.dynamic.creatures;

import java.io.Serializable;
import java.util.ArrayList;

import application.core.MapStateSingleton;
import application.core.RandomizerSingleton;
import application.gui.SceneManagerSingleton;
import application.gui.SimulatorController;

public class Species implements Serializable {
	private static final long serialVersionUID = 1L;

	public Genome baseGenome;
	
	public String name;
	
	public int id;
	public int currentMembers;
	public int currentMutatedMembers;
	public long totalMembers;
	

	public long originalParentIdA;
	public long originalParentIdB;
	
	
	public Species parent;
	public ArrayList<Species> children;
	
	public Species(Creature founderCreature) {
		setName();
		setBaseGenome(founderCreature);
		currentMembers = 0;
		currentMutatedMembers = 0;
	}
	public Species(Genome founderGenome) {
		
		setName();
		setBaseGenome(founderGenome);
		currentMembers = 0;
		currentMutatedMembers = 0;
		children = new ArrayList<Species>();
		
		MapStateSingleton.getInstance().registerSpecies(this);
		SceneManagerSingleton.getInstance().simulatorController.addSpeciesToOverview(this);
	}
	
	
	
	public void setName() {
		String start = generateNameStart();
		String end = generateNameEnd();
		String mid = "";
		int lengthWithoutMid = start.length() + end.length();
		if (lengthWithoutMid < 6 ||
			(lengthWithoutMid < 10 && RandomizerSingleton.getInstance().nextBoolean())) {
			mid = generateNameMid();
		}
		name = start + mid + end;
	}
	
	

	public String generateNameStart() {
		String[] start = {
				"Arth",
				"Bri",
				"Bar",
				"Bi",
				"Cea",
				"Char",
				"Dipho",
				"Dul",
				"Epso",
				"Filli",
				"Gar",
				"Hepto",
				"Hoxo",
				"Ikto",
				"Ise",
				"Khor",
				"Laga",
				"Lern",
				"Methe",
				"Mur",
				"Neo",
				"Nilo",
				"Occe",
				"Plen",
				"Quar",
				"Rith",
				"Rae",
				"Stru",
				"Squil",
				"Tetra",
				"Uni",
				"Uda",
				"Var",
				"Xeno",
				"Wor",
				"Zaco",
				"Zira"		
		};
		return start[RandomizerSingleton.getInstance().nextInt(start.length)];
	}
	

	public String generateNameEnd() {
		String[] end = {
				"nid",
				"mon",
				"dae",
				"line",
				"ron",
				"rot",
				"thious",
				"nade",
				"nine",
				"rid",
				"noid",
				"sian",
				"ster",
				"ther",
				"lun",
				"tid",
				"phian",
				"nian",
				"dine",
				"vese",
				"kant",
				"moth"
		};
		return end[RandomizerSingleton.getInstance().nextInt(end.length)];
	}

	public String generateNameMid() {
		String[] mid = {
				"no",
				"kor",
				"si",
				"ra",
				"mia",
				"ven",
				"ma",
				"kan",
				"ren",
				"ku",
				"mas",
				"zar",
				"ne",
				"zo"	
		};
		return mid[RandomizerSingleton.getInstance().nextInt(mid.length)];
	}
	
	public void setBaseGenome(Creature founderCreature) {
		this.baseGenome = founderCreature.genome.getCopyOfGenome();
	}
	public void setBaseGenome(Genome founderGenome) {
		this.baseGenome = founderGenome.getCopyOfGenome();
	}
	public Genome getGenomeFromBase() {
		return this.baseGenome.getCopyOfGenome();
	}
	
	public boolean isDirectRelative(Species otherSpecies) {
		if (this.id == otherSpecies.parent.id || this.parent.id == otherSpecies.id) {
			return true;
		} else {
			return false;
		}
	}
}
