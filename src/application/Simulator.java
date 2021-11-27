package application;

import java.util.ArrayList;
import java.util.Random;

public class Simulator {
	
	public int frame;
	public boolean breakSimulation;
	
	private ArrayList<Creature> creatures;
	private ArrayList<Integer> oldCreaturePositionsX;
	private ArrayList<Integer> oldCreaturePositionsY;
	
	private Renderer renderer;
	
	
	public Simulator(Renderer renderer) {

		this.renderer = renderer;
		
		this.frame = 0;
		breakSimulation = false;
		creatures = new ArrayList<Creature>();
		oldCreaturePositionsX = new ArrayList<Integer>();
		oldCreaturePositionsY = new ArrayList<Integer>();
	}

	
	
	public void populateWorld() {
		
		for (int i = 0; i < 15000; i++) {
			creatures.add(new Creature(new Random().nextInt(320), new Random().nextInt(320)));
		}
	}
	
	
	
	public void animate() {
		
		while (!breakSimulation) {
			
			performFrameActions();
			
			
			renderer.render(creatures, oldCreaturePositionsX, oldCreaturePositionsY);
			
			frame++;
			
			if (frame > 5000) {
				break;
			}
			
		}
	}
	
	
	
	public void performFrameActions() {
		moveGenerators();
		moveCreatures();
		adjacentActions();
	}
	

	
	
	private void moveGenerators() {
		
	}
	
	private void moveCreatures() {
		
		int lastPixelX = 319;
		int lastPixelY = 319;
		
		oldCreaturePositionsX.clear();
		oldCreaturePositionsY.clear();
		
		for (Creature c : creatures) {
			
			oldCreaturePositionsX.add(c.x);
			oldCreaturePositionsY.add(c.y);
			
			c.x = Math.max(0,Math.min(lastPixelX, c.x + (new Random().nextInt(3)-1)));
			c.y = Math.max(0,Math.min(lastPixelY, c.y + (new Random().nextInt(3)-1)));
		}
		
	}
	
	private void adjacentActions() {
		
	}
}