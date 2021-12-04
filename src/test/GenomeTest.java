package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import application.dynamic.Genome;

class GenomeTest {

	@Test
	void testRandomlyAverageValue() {
		
		int valueA = 3;
		int valueB = 6;
		
		int newValue = new Genome().randomlyAverageValue(valueA, valueB);
		
		System.out.println(newValue);

		assertTrue("Result should be 4 or 5", newValue == 4 || newValue == 5);
		
		boolean gotDifferentValues = false;
		
		int previousValue = newValue;
		for (int i = 0; i < 100; i++) {
			newValue = new Genome().randomlyAverageValue(valueA, valueB);
			if (newValue != previousValue) {
				gotDifferentValues = true;
				break;
			}
		}
		
		assertTrue("Result should give two different results at random", gotDifferentValues);
	}

}
