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
	private int attackDamage;
	private int defenseDamage;
	private int toughness;
	private int perception;
	private int stealth;
	private int agression;
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
		this.attackDamage = attackDamage;
	}
	public void setDefenseDamage(int defenseDamage) {
		this.defenseDamage = defenseDamage;
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
	public void setAgression(int agression) {
		this.agression = agression;
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
	
	
	
	
	
	
	public Genome getBaseGenome() {
		Genome baseGenome = new Genome();
		baseGenome.copyFrom(this);
		return baseGenome;
	}
	
	private void copyFrom(Genome genome) {
		this.diet = genome.diet;
		this.specificDiet = genome.specificDiet;
		this.size = genome.size;
		this.speed = genome.speed;
		this.attackDamage = genome.attackDamage;
		this.defenseDamage = genome.defenseDamage;
		this.toughness = genome.toughness;
		this.perception = genome.perception;
		this.stealth = genome.stealth;
		this.agression = genome.agression;
		this.reactiveness = genome.reactiveness;
		this.ageExpectancy = genome.ageExpectancy;
		this.fertility = genome.fertility;
		this.clutchSize = genome.clutchSize;
	}
	
	
	
	public void setStrategiesFromGenome(Creature creature) {
		setMovementStrategyFromGenome(creature);
		setFeedingStrategyFromGenome(creature);
		setReproductionStrategyFromGenome(creature);
		
	}
	
	public void setMovementStrategyFromGenome(Creature creature) {
		if (this.diet == Diet.PHOTOSYNTHESIS) {
			creature.setMovementDecisionStrategy(new FloaterMovementDecision(creature));
		}
		//TODO: ELSE: other strategies.
	}

	public void setFeedingStrategyFromGenome(Creature creature) {
		if (this.diet == Diet.PHOTOSYNTHESIS) {
			creature.setFeedingStrategy(new Photosynthesis(creature));
		}
		//TODO: ELSE: other strategies.
	}
	
	public void setReproductionStrategyFromGenome(Creature creature) {
		creature.setReproductionStrategy(new SexualReproduction(creature));
		//Eventually add other strategies. Self-cloning?
	}
	
	

	public Genome recombineWith(Genome partnerGenome) {
		Genome newGenome = new Genome();
		newGenome.setNonNumericalsFromParents(this, partnerGenome);
		newGenome.averageNumericalsFromParents(this, partnerGenome);
		newGenome.calculateAvailableEvoPoints();
		return newGenome;
	}
	
	public void setNonNumericalsFromParents(Genome genomeA, Genome genomeB) {
		this.diet = genomeA.diet;
		this.specificDiet = genomeA.specificDiet;
	}
	
	public void averageNumericalsFromParents(Genome genomeA, Genome genomeB) {
		this.size = randomlyAverageValue(genomeA.size, genomeB.size);
		this.speed = randomlyAverageValue(genomeA.speed, genomeB.speed);
		this.attackDamage = randomlyAverageValue(genomeA.attackDamage, genomeB.attackDamage);
		this.defenseDamage = randomlyAverageValue(genomeA.defenseDamage, genomeB.defenseDamage);
		this.toughness = randomlyAverageValue(genomeA.toughness, genomeB.toughness);
		this.perception = randomlyAverageValue(genomeA.perception, genomeB.perception);
		this.stealth = randomlyAverageValue(genomeA.stealth, genomeB.stealth);
		this.agression = randomlyAverageValue(genomeA.agression, genomeB.agression);
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
		int maxInnateDeviationRate = 1000; //TODO
		int innateDeviationRate = getDeviationRate(originalSpecies.baseGenome);
		if (innateDeviationRate > 0) {
			creature.mutated = true;
		}
		return (innateDeviationRate > maxInnateDeviationRate);
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
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.attackDamage - baseGenome.attackDamage));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.defenseDamage - baseGenome.defenseDamage));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.toughness - baseGenome.toughness));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.perception - baseGenome.perception));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.stealth - baseGenome.stealth));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.agression - baseGenome.agression));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.reactiveness - baseGenome.reactiveness));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.ageExpectancy - baseGenome.ageExpectancy));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.fertility - baseGenome.fertility));
		maxStatDeviation = Math.max(maxStatDeviation, Math.abs(this.clutchSize - baseGenome.clutchSize));
		deviationRate += maxStatDeviation;
		return deviationRate;
	}
	
	
	public boolean exposeToMutation() {
		
		boolean mutated = exposeToStatAdjustment();
		if (mutated) {
			reduceStatsInNeeded();
		}
		
		return mutated;
	}

	
	
	public boolean exposeToStatAdjustment() {
		RandomizerSingleton randomizer = RandomizerSingleton.getInstance();
		SettingsSingleton settings = SettingsSingleton.getInstance();
		
		if (randomizer.nextInt(settings.birthsPerMutation) == 0) {
			
			valueToAvoidChanging = -1;
			increaseRandomStat();
			
			return true;
			
		} else {
			return false;
		}
		
	}
	
	
	public void increaseRandomStat() {

		RandomizerSingleton randomizer = RandomizerSingleton.getInstance();
		SettingsSingleton settings = SettingsSingleton.getInstance();
		
		if (randomizer.nextInt(settings.mutationsPerDietChange) == 0) {
			
			//TODO: Diet changes require the proper movement, attack, defense, and feeding methods
			
		} else if (randomizer.nextInt(settings.mutationsPerSizeChange) == 0) {
			
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
				if (attackDamage > 1) {
					attackDamage--;
				}
				if (defenseDamage > 1) {
					defenseDamage--;
				}
				reduceStatsInNeeded();
			}
		}
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
				defenseDamage = addToValueIfPossible(defenseDamage, amountToChange);
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
				attackDamage = addToValueIfPossible(attackDamage, amountToChange);
				break;
			case 9:
				agression = addToValueIfPossible(agression, amountToChange);
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
		freeEvoPoints -= attackDamage;
		freeEvoPoints -= defenseDamage;
		freeEvoPoints -= toughness;
		freeEvoPoints -= perception;
		freeEvoPoints -= stealth;
		freeEvoPoints -= fertility;
		freeEvoPoints -= clutchSize;
		freeEvoPoints -= agression;
		freeEvoPoints -= reactiveness;
		freeEvoPoints -= ageExpectancy;
		
		this.freeEvoPoints = freeEvoPoints;
	}
	
	
	
	


	public int getFoodNeededPerTick() {
		return this.size;
	}
	public int getActivationSpeed() {
		return this.speed;
	}
	public int getMaxHealth() {
		return this.toughness;
	}
	public int getMaxAge() {
		return (this.ageExpectancy*500);
	}
	public int getTotalChildren() {
		return ((this.fertility*2)+1);
	}
	public int getChildrenPerBirth() {
		return this.clutchSize;
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
	public int getAttackDamage() {
		return attackDamage;
	}
	public int getDefenseDamage() {
		return defenseDamage;
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
	public int getAgression() {
		return agression;
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
}


