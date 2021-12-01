package application.dynamic;

import java.io.Serializable;

public class Genome implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	
//	public Size size;
	
	public Diet diet;
//	public Species specificDiet;
	
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
	
	

	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
