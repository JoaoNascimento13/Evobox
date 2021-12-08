package application.dynamic.creatures;

import java.io.Serializable;
import java.util.ArrayList;

import application.core.MapStateSingleton;
import application.core.RandomizerSingleton;
import application.gui.SceneManagerSingleton;
import application.gui.SimulatorController;
import javafx.scene.paint.Color;

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

	private int colorInt;
	
	public Species parent;
	public ArrayList<Species> children;
	
	
	
	public Species() {
		currentMembers = 0;
		currentMutatedMembers = 0;
		children = new ArrayList<Species>();
	}
	
	
	public void addChildren(Species children) {
		this.children.add(children);
	}
	
	

	public void setColor() {
		int red = 0;
		int green = 0;
		int blue = 0;
		int available;
		switch (baseGenome.diet) {
		case PHOTOSYNTHESIS:
			
			green = 120 + RandomizerSingleton.getInstance().nextInt(115);
			available = 80 + RandomizerSingleton.getInstance().nextInt(21);
			red = RandomizerSingleton.getInstance().nextInt(available);
			blue = available - red;
			break;
			
		case HERBIVOROUS:

			blue = 120 + RandomizerSingleton.getInstance().nextInt(115);
			available = 80 + RandomizerSingleton.getInstance().nextInt(21);
			red = RandomizerSingleton.getInstance().nextInt(available);
			green = available - red;
			break;
			
		case CARNIVOROUS:
			
			red = 120 + RandomizerSingleton.getInstance().nextInt(115);
			available = 80 + RandomizerSingleton.getInstance().nextInt(21);
			green = RandomizerSingleton.getInstance().nextInt(available);
			blue = available - green;
			break;
			
		default:
			break;
		}
		this.colorInt = calculateColorInt(Color.rgb(red, green, blue));
	}
	
	public int getColor() {
		return colorInt;
	}
	
	public int calculateColorInt(Color color) {
        return
                (                      255  << 24) |
                ((int) (color.getRed()   * 255) << 16) |
                ((int) (color.getGreen() * 255) << 8)  |
                ((int) (color.getBlue()  * 255));
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
