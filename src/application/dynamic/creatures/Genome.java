package application.dynamic.creatures;

import java.io.Serializable;

import application.core.RandomizerSingleton;
import application.core.SettingsSingleton;

public class Genome implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Diet diet;
	public Species specificDiet;
	
	private int size;
	private int speed;
	private int attack;
	private int defense;
	private int toughness;
	private int perception;
	private int stealth;
	private int aggression;
	private int reactiveness;
	private int ageExpectancy;
	private int fertility;
	private int clutchSize;

	private int freeEvoPoints;
	private int maxEvoPoints;

	private transient int valueToAvoidChanging;
	private transient boolean valueChanged;
	
	public void setDiet(Diet diet) {
		this.diet = diet;
	}
	public void setSpecificDiet(Species specificDiet) {
		this.specificDiet = specificDiet;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public void setAttackDamage(int attackDamage) {
		this.attack = attackDamage;
	}
	public void setDefenseDamage(int defenseDamage) {
		this.defense = defenseDamage;
	}
	public void setToughness(int toughness) {
		this.toughness = toughness;
	}
	public void setPerception(int perception) {
		this.perception = perception;
	}
	public void setStealth(int stealth) {
		this.stealth = stealth;
	}
	public void setAggression(int agression) {
		this.aggression = agression;
	}
	public void setReactiveness(int reactiveness) {
		this.reactiveness = reactiveness;
	}
	public void setAgeExpectancy(int ageExpectancy) {
		this.ageExpectancy = ageExpectancy;
	}
	public void setFertility(int fertility) {
		this.fertility = fertility;
	}
	public void setClutchSize(int clutchSize) {
		this.clutchSize = clutchSize;
	}
	
	
	
	
	
	
	public Genome getCopyOfGenome() {
		Genome baseGenome = new Genome();
		baseGenome.copyFrom(this);
		return baseGenome;
	}
	
	private void copyFrom(Genome genome) {
		this.diet = genome.diet;
		this.specificDiet = genome.specificDiet;
		this.size = genome.size;
		this.speed = genome.speed;
		this.attack = genome.attack;
		this.defense = genome.defense;
		this.toughness = genome.toughness;
		this.perception = genome.perception;
		this.stealth = genome.stealth;
		this.aggression = genome.aggression;
		this.reactiveness = genome.reactiveness;
		this.ageExpectancy = genome.ageExpectancy;
		this.fertility = genome.fertility;
		this.clutchSize = genome.clutchSize;
		this.freeEvoPoints = genome.freeEvoPoints;
		this.maxEvoPoints = genome.maxEvoPoints;
	}
	
	
	
	public void setStrategiesFromGenome(Creature creature) {
		setMovementStrategyFromGenome(creature);
		setFeedingStrategyFromGenome(creature);
		setReproductionStrategyFromGenome(creature);
		
	}
	
	public void setMovementStrategyFromGenome(Creature creature) {
		if (this.diet == Diet.PHOTOSYNTHESIS) {
			creature.setMovementDecisionStrategy(new PlantMovementDecision(creature));
		} else {
			creature.setMovementDecisionStrategy(new AnimalMovementDecision(creature));
		}
	}

	public void setFeedingStrategyFromGenome(Creature creature) {
		if (this.diet == Diet.PHOTOSYNTHESIS) {
			creature.setFeedingStrategy(new Photosynthesis(creature));
		} else {
			creature.setFeedingStrategy(new Heterotrophy(creature));
		}
	}
	
	public void setReproductionStrategyFromGenome(Creature creature) {
		creature.setReproductionStrategy(new SexualReproduction(creature));
		//Eventually add other strategies. Self-cloning?
	}
	
	

	public Genome recombineWith(Genome partnerGenome, Genome baseGenome) {
		
		//Note: Genome recombination only occurs when both creatures belong to the same species.
		//Creatures from different (parent/children) species, when reproducing, get the genome of either of them.
		
		Genome newGenome = new Genome();
		newGenome.setDietFromParents(this);
		newGenome.setSizeFromParents(this, partnerGenome, baseGenome);
		newGenome.averageNumericalsFromParents(this, partnerGenome);
		newGenome.calculateAvailableEvoPoints();
		return newGenome;
	}
	
	public void setDietFromParents(Genome parentGenome) {
		
		//Note: Diet changes always result in a new species, and can never vary within species.
		
		this.diet = parentGenome.diet;
		this.specificDiet = parentGenome.specificDiet;
	}
	
	public void setSizeFromParents(Genome genomeA, Genome genomeB, Genome baseGenome) {
		
		//Note: Size is handled differently than onther numerical values.
		//Because a single innate Size deviation is enough to create a new species,
		//we impose that the children of a size-mutated and a non-size-mutated creature of the same species
		//is always non-size-mutated.
		
		if (genomeA.size == genomeB.size) {
			this.size = genomeA.size;
		} else if (genomeA.size == baseGenome.size || genomeB.size == baseGenome.size) {
			this.size = baseGenome.size;
		} else {
			this.size = randomlyAverageValue(genomeA.size, genomeB.size);
		}
	}
	
	public void averageNumericalsFromParents(Genome genomeA, Genome genomeB) {
		this.speed = randomlyAverageValue(genomeA.speed, genomeB.speed);
		this.attack = randomlyAverageValue(genomeA.attack, genomeB.attack);
		this.defense = randomlyAverageValue(genomeA.defense, genomeB.defense);
		this.toughness = randomlyAverageValue(genomeA.toughness, genomeB.toughness);
		this.perception = randomlyAverageValue(genomeA.perception, genomeB.perception);
		this.stealth = randomlyAverageValue(genomeA.stealth, genomeB.stealth);
		this.aggression = randomlyAverageValue(genomeA.aggression, genomeB.aggression);
		this.reactiveness = randomlyAverageValue(genomeA.reactiveness, genomeB.reactiveness);
		this.ageExpectancy = randomlyAverageValue(genomeA.ageExpectancy, genomeB.ageExpectancy);
		this.fertility = randomlyAverageValue(genomeA.fertility, genomeB.fertility);
		this.clutchSize = randomlyAverageValue(genomeA.clutchSize, genomeB.clutchSize);
	}
	
	public int randomlyAverageValue(int valA, int valB) {
		if (valA == valB) {
			return valA;
		}
		int maxVal = Math.max(valA, valB);
		int minVal = Math.min(valA, valB);
		int delta = maxVal - minVal;
		if (delta % 2 == 0) {
			return (minVal + (delta/2));
		} else {
			if (RandomizerSingleton.getInstance().nextBoolean()) {
				return (minVal + (delta/2));
			} else {
				return (minVal + (delta/2) + 1);
			}
		}
	}
	
	
	public boolean checkForMutationAndNewSpecies(Creature creature, Species originalSpecies) {
		int maxDeviationRate = 1;
		int deviationRate = getDeviationRate(originalSpecies.baseGenome);
		if (deviationRate > 0) {
			creature.mutated = true;
		}
		return (deviationRate > maxDeviationRate);
	}
	
	public int getDeviationRate(Genome baseGenome) {
		int deviationRate = 0;
		if (this.diet != baseGenome.diet) {
			deviationRate += 99;
		}
		if (this.specificDiet != null || baseGenome.specificDiet != null) {
			if (this.specificDiet == null || baseGenome.specificDiet == null) {
				deviationRate += 50;
			} else if (this.specificDiet.id != baseGenome.specificDiet.id) {
				deviationRate += 50;
			}
		}
		if (this.size != baseGenome.size) {
			deviationRate += 10;
		}
		int maxStatDeviation = 0;
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.speed - baseGenome.speed));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.attack - baseGenome.attack));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.defense - baseGenome.defense));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.toughness - baseGenome.toughness));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.perception - baseGenome.perception));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.stealth - baseGenome.stealth));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.aggression - baseGenome.aggression));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.reactiveness - baseGenome.reactiveness));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.ageExpectancy - baseGenome.ageExpectancy));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.fertility - baseGenome.fertility));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.clutchSize - baseGenome.clutchSize));
		deviationRate += maxStatDeviation;
		return deviationRate;
	}
	
	

//	public void exposeToDietMutation() {
//		RandomizerSingleton randomizer = RandomizerSingleton.getInstance();
//		SettingsSingleton settings = SettingsSingleton.getInstance();
//		
//		if (randomizer.nextInt(settings.birthsPerMutation * settings.mutationsPerDietChange) == 0) {
//			
//			valueToAvoidChanging = -1;
//			
//			mutateDiet();
//		}
//	}
	
	
	public Genome getPossiblyMutatedGenome(boolean canMutateDiet) {
		RandomizerSingleton randomizer = RandomizerSingleton.getInstance();
		SettingsSingleton settings = SettingsSingleton.getInstance();
		
		Genome possiblyMutatedGenome = this.getCopyOfGenome();
		
		int birthsPerMutation;
		if (diet == Diet.PHOTOSYNTHESIS) {
			birthsPerMutation = settings.plantBirthsPerMutation;
		} else {
			birthsPerMutation = settings.animalBirthsPerMutation;
		}
		
		if (randomizer.nextInt(birthsPerMutation) == 0) {
			
			valueToAvoidChanging = -1;
			possiblyMutatedGenome.increaseRandomStat(canMutateDiet);

			possiblyMutatedGenome.reduceStatsInNeeded();
		}
		return possiblyMutatedGenome;
	}
	
	
	public void increaseRandomStat(boolean canMutateDiet) {

		RandomizerSingleton randomizer = RandomizerSingleton.getInstance();
		SettingsSingleton settings = SettingsSingleton.getInstance();

		int mutationsPerDietChange;
		int mutationsPerSizeChange;
		if (diet == Diet.PHOTOSYNTHESIS) {
			mutationsPerDietChange = settings.plantMutationsPerDietChange;
			mutationsPerSizeChange = settings.plantMutationsPerSizeChange;
		} else {
			mutationsPerDietChange = settings.animalMutationsPerDietChange;
			mutationsPerSizeChange = settings.animalMutationsPerSizeChange;
		}
		
		
		if (canMutateDiet && randomizer.nextInt(mutationsPerDietChange) == 0) {
			
			mutateDiet();
			
		} else if (randomizer.nextInt(mutationsPerSizeChange) == 0) {
			
			mutateSize();
			
		} else {
			
			int valueChanged = changeRandomValueBy(1);
			valueToAvoidChanging = valueChanged;
		}
	}
	

	public void mutateSize() {
		if (RandomizerSingleton.getInstance().nextBoolean()) {
			
			valueChanged = false;
			size = addToValueIfPossible(size, 1);
			if (valueChanged) {
				toughness++;
			}
		} else {
			valueChanged = false;
			size = addToValueIfPossible(size, -1);
			if (valueChanged) {
				if (toughness > 1) {
					toughness--;
				}
				if (attack > 1) {
					attack--;
				}
				if (defense > 1) {
					defense--;
				}
				reduceStatsInNeeded();
			}
		}
	}
	
	public void mutateDiet() {
		switch (diet) {
		case CARNIVOROUS:
			diet = Diet.HERBIVOROUS;
			reduceStatsInNeeded();
			break;
		case HERBIVOROUS:
			if (RandomizerSingleton.getInstance().nextBoolean()) {
				diet = Diet.CARNIVOROUS;
				upgradeStatsToCarnivorous();
				reduceStatsInNeeded();
			} else {
				diet = Diet.PHOTOSYNTHESIS;
				convertStatsToPlant();
				reduceStatsInNeeded();
			}
			break;
		case PHOTOSYNTHESIS:
			diet = Diet.HERBIVOROUS;
			convertStatsToAnimal();
			reduceStatsInNeeded();
			break;
		default:
			break;
		}
	}
	

	public void convertStatsToPlant() {
		setSpeed(0);
		setPerception(0);
		setAggression(0);
		setReactiveness(0);
		setAttackDamage(0);
	}
	
	public void convertStatsToAnimal() {
		setSpeed(Math.max(speed, 1));
		setPerception(Math.max(perception, 1));
		setAggression(Math.max(aggression, 1));
		setReactiveness(Math.max(reactiveness, 1));
		setAttackDamage(Math.max(attack, 1));
	}
	public void upgradeStatsToCarnivorous() {
		setSpeed(Math.min(speed+1, 10));
		setPerception(Math.min(perception+1, 10));
		setAggression(Math.min(aggression+1, 10));
		setAttackDamage(Math.min(attack+1, 10));
	}
	
	
	public void reduceStatsInNeeded() {
		calculateAvailableEvoPoints();
		while (freeEvoPoints < 0) {
			changeRandomValueBy(-1);
			calculateAvailableEvoPoints();
		}
	}
	
	
	public int changeRandomValueBy(int amountToChange) {
		
		int maxStatPosition;
		if (diet == Diet.PHOTOSYNTHESIS) {
			maxStatPosition = 6;
		} else {
			maxStatPosition = 11;
		}
		
		int valueToIncrease = RandomizerSingleton.getInstance().nextInt(maxStatPosition);
		valueChanged = false;
		
		while (valueChanged == false) {
			
			while (valueToAvoidChanging == valueToIncrease) {
				valueToIncrease = RandomizerSingleton.getInstance().nextInt(maxStatPosition);
			}
			
			switch (valueToIncrease) {
			case 0:
				defense = addToValueIfPossible(defense, amountToChange);
				break;
			case 1:
				toughness = addToValueIfPossible(toughness, amountToChange);
				break;
			case 2:
				stealth = addToValueIfPossible(stealth, amountToChange);
				break;
			case 3:
				ageExpectancy = addToValueIfPossible(ageExpectancy, amountToChange);
				break;
			case 4:
				fertility = addToValueIfPossible(fertility, amountToChange);
				break;
			case 5:
				clutchSize = addToValueIfPossible(clutchSize, amountToChange);
				break;
			case 6:
				speed = addToValueIfPossible(speed, amountToChange);
				break;
			case 7:
				perception = addToValueIfPossible(perception, amountToChange);
				break;
			case 8:
				attack = addToValueIfPossible(attack, amountToChange);
				break;
			case 9:
				aggression = addToValueIfPossible(aggression, amountToChange);
				break;
			case 10:
				reactiveness = addToValueIfPossible(reactiveness, amountToChange);
				break;
			default:
				break;
			}
		}
		
		return valueToIncrease;
	}
	
	

	public int addToValueIfPossible(int originalValue, int amountToChange) {
		if (originalValue + amountToChange > 0 && originalValue + amountToChange < 11) {
			valueChanged = true;
			return (originalValue + amountToChange);
		}
		return originalValue;
	}
	
	
	public void calculateAvailableEvoPoints() {
		
		int pointsPerSizeLevel = 5;
		
		int maxEvoPoints = diet.getEvoPointsGranted() + (size * pointsPerSizeLevel);
		if (specificDiet != null) {
			maxEvoPoints += 10;
		}
		this.maxEvoPoints = maxEvoPoints;
		
		int freeEvoPoints = maxEvoPoints;
		
		freeEvoPoints -= speed;
		freeEvoPoints -= attack;
		freeEvoPoints -= defense;
		freeEvoPoints -= toughness;
		freeEvoPoints -= perception;
		freeEvoPoints -= stealth;
		freeEvoPoints -= fertility;
		freeEvoPoints -= clutchSize;
		freeEvoPoints -= aggression;
		freeEvoPoints -= reactiveness;
		freeEvoPoints -= ageExpectancy;
		
		this.freeEvoPoints = freeEvoPoints;
	}
	
	
	
	


	public int getFoodNeededPerTick() {
		return size+1;
	}
	public int getActivationSpeed() {
		return speed;
	}
	public int getMaxHealth() {
		return toughness;
	}
	public int getMaxAge() {
		return (ageExpectancy*1500);
	}
	public int getTotalChildren() {
		return ((fertility+1)*diet.getFertilityCoef());
	}
	public int getChildrenPerBirth() {
		return clutchSize+1;
	}
	

	public Diet getDiet() {
		return diet;
	}
	public Species getSpecificDiet() {
		return specificDiet;
	}
	public int getSize() {
		return size;
	}
	public int getSpeed() {
		return speed;
	}
	public int getAttack() {
		return attack;
	}
	public int getDefense() {
		return defense;
	}
	public int getToughness() {
		return toughness;
	}
	public int getPerception() {
		return perception;
	}
	public int getStealth() {
		return stealth;
	}
	public int getAggression() {
		return aggression;
	}
	public int getReactiveness() {
		return reactiveness;
	}
	public int getAgeExpectancy() {
		return ageExpectancy;
	}
	public int getFertility() {
		return fertility;
	}
	public int getClutchSize() {
		return clutchSize;
	}
	public int getFreeEvoPoints() {
		return freeEvoPoints;
	}
	public int getMaxEvoPoints() {
		return maxEvoPoints;
	}
	public int getUsedEvoPoints() {
		return (maxEvoPoints-freeEvoPoints);
	}
	

	public String getSizeDescription() {
		if (size < 3) {
			return "Tiny ";
		} else if (size < 5) {
			return "Small ";
		} else if (size < 7) {
			return "Medium ";
		} else if (size < 9) {
			return "Large ";
		} else {
			return "Gigantic ";
		}
	}
	
	
	@Override
	public String toString() {
		return (
				"Diet " + diet + 
				"; Size " + size + 
				"; Speed " + speed + 
				"; Toughness " + toughness + 
				"; Attack " + attack + 
				"; Defense " + defense + 
				"; Perception " + perception + 
				"; Stealth " + stealth + 
				"; Age Expectancy " + ageExpectancy + 
				"; Fertility " + fertility + 
				"; Clutch Size " + clutchSize + 
				"; Agression " + aggression + 
				"; Reactiveness " + reactiveness);
	}
	
	public boolean sameAs(Genome otherGenome) {
		if (
			size == otherGenome.size &&
			speed == otherGenome.speed &&
			attack == otherGenome.attack &&
			defense == otherGenome.defense &&
			toughness == otherGenome.toughness &&
			perception == otherGenome.perception &&
			stealth == otherGenome.stealth &&
			ageExpectancy == otherGenome.ageExpectancy &&
			fertility == otherGenome.fertility &&
			clutchSize == otherGenome.clutchSize &&
			aggression == otherGenome.aggression &&
			reactiveness == otherGenome.reactiveness &&
			diet == otherGenome.diet &&
			((specificDiet == null && otherGenome.specificDiet == null) || 
			(specificDiet != null && otherGenome.specificDiet != null && specificDiet.id == otherGenome.specificDiet.id))
			) {
			return true;
		} else {
			return false;
		}
	}
}


