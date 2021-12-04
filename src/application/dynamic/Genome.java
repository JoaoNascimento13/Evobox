package application.dynamic;

import java.io.Serializable;

import application.core.RandomizerSingleton;

public class Genome implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Diet diet;
	public Species specificDiet;
	public Size size;
	
	public int speed;
	public int attackDamage;
	public int defenseDamage;
	public int toughness;
	public int perception;
	public int stealth;
	public int agression;
	public int reactiveness;
	public int ageExpectancy;
	public int fertility;
	public int clutchSize;
	

	
	public void setDiet(Diet diet) {
		this.diet = diet;
	}
	public void setSoecificDiet(Species specificDiet) {
		this.specificDiet = specificDiet;
	}
	public void setSize(Size size) {
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
		newGenome.diet = this.diet;
		newGenome.specificDiet = this.specificDiet;
		newGenome.size = this.size;
		
		newGenome.speed = randomlyAverageValue(this.speed, partnerGenome.speed);
		newGenome.attackDamage = randomlyAverageValue(this.attackDamage, partnerGenome.attackDamage);
		newGenome.defenseDamage = randomlyAverageValue(this.defenseDamage, partnerGenome.defenseDamage);
		newGenome.toughness = randomlyAverageValue(this.toughness, partnerGenome.toughness);
		newGenome.perception = randomlyAverageValue(this.perception, partnerGenome.perception);
		newGenome.stealth = randomlyAverageValue(this.stealth, partnerGenome.stealth);
		newGenome.agression = randomlyAverageValue(this.agression, partnerGenome.agression);
		newGenome.reactiveness = randomlyAverageValue(this.reactiveness, partnerGenome.reactiveness);
		newGenome.ageExpectancy = randomlyAverageValue(this.ageExpectancy, partnerGenome.ageExpectancy);
		newGenome.fertility = randomlyAverageValue(this.fertility, partnerGenome.fertility);
		newGenome.clutchSize = randomlyAverageValue(this.clutchSize, partnerGenome.clutchSize);
		
		return newGenome;
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
	

	
	public int getDeviationRate(Genome baseGenome) {
		
		int deviationRate = 0;
		
		if (this.diet != baseGenome.diet) {
			deviationRate += 99;
		}
		if (this.specificDiet.id != baseGenome.specificDiet.id) {
			deviationRate += 50;
		}
		if (this.size != baseGenome.size) {
			deviationRate += 50;
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
	
}


