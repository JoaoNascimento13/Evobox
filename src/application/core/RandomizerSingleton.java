package application.core;

import java.util.Random;

public class RandomizerSingleton extends Random {
	private static final long serialVersionUID = 1L;

	static private RandomizerSingleton randomizer = new RandomizerSingleton();
	
	private RandomizerSingleton() {}
	
	static public RandomizerSingleton getInstance() {
		return randomizer;
	}
}
