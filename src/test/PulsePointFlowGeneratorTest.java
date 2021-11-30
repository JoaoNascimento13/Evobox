package test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import application.core.Direction;
import application.dynamic.Flow;
import application.dynamic.PulsePointFlowGenerator;

class PulsePointFlowGeneratorTest {

	Point coords;
	
	@Test
	void testGetFlowOnPoint() {
		
		Point flowPoint = new Point(104, 103);
		
		Point coords = new Point(100, 100);
		
		Flow flow = new PulsePointFlowGenerator(0, 0, coords, 0, false, null).getBaseFlowOnPoint(flowPoint, 10, Direction.DOWN_RIGHT);
		
		assertEquals(8, flow.valX);
		
	}

}
