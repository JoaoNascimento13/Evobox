package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import application.Temp;

class TempTest {

	@Test
	void testAdd() {
		assertEquals(3, new Temp().add(1, 2));
	}

}
