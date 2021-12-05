package application.dynamic.creatures;

import java.io.Serializable;

import application.core.RandomizerSingleton;

public class Species implements Serializable {
	private static final long serialVersionUID = 1L;

	public Genome baseGenome;
	
	public String name;
	
	public int id;
	public int currentMembers;
	public int currentMutatedMembers;
	public long totalMembers;
	
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
		this.baseGenome = founderCreature.genome.getBaseGenome();
	}
	public void setBaseGenome(Genome founderGenome) {
		this.baseGenome = founderGenome.getBaseGenome();
	}
	public Genome getGenomeFromBase() {
		return this.baseGenome.getBaseGenome();
	}
}
