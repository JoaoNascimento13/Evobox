package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.awt.Point;

public class Simulator {
	
	public int frame;
	
	private ArrayList<Creature> creatures;
	private ArrayList<Integer> oldCreaturePositionsX;
	private ArrayList<Integer> oldCreaturePositionsY;

	private ArrayList<FlowGenerator> flowGenerators;
	
	private Renderer renderer;
	
	private boolean paused;
	
	private boolean recording;
	private boolean rendering;
	
//	private FileOutputStream simulationFile;

	private CloneableRandom randomizer;
	
	private int simNumber;
	
	
	
	
	private Settings settings;

	
	private Point[][] flowMap;
	
	public Simulator(Renderer renderer) {

		this.renderer = renderer;

		settings = new Settings(0, 320, 320);
		
		
		frame = 0;
		
		paused = true;
		recording = false;
		rendering = false;
		
		creatures = new ArrayList<Creature>();
		flowGenerators = new ArrayList<FlowGenerator>();
		
		oldCreaturePositionsX = new ArrayList<Integer>();
		oldCreaturePositionsY = new ArrayList<Integer>();
		
		
		randomizer = new CloneableRandom();
		
		flowMap = new Point[settings.mapCellsX][settings.mapCellsY];
		
		for (int i = 0; i < settings.mapCellsX; i++) {
			for (int j = 0; j < settings.mapCellsX; j++) {
				flowMap[i][j] = new Point(0, 0);
			}
		}
		
		
//		try {
//			
//			simulationFile = new FileOutputStream("simulations/simul");
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			System.exit(0);
//		}
	}

	
	
	public void populateWorld() {
		
		for (int i = 0; i < 15000; i++) {

//			creatures.add(new Creature(160, 160));
			
			creatures.add(new Creature(randomizer.nextInt(320), randomizer.nextInt(320)));

			//creatures.add(new Creature(100+randomizer.nextInt(120), 100+randomizer.nextInt(120)));
		}

		for (int i = 0; i < 20; i++) {

			flowGenerators.add(new PulsePointFlowGenerator(
					100, 
					1+randomizer.nextInt(5), 
					new Point(randomizer.nextInt(settings.mapCellsX), randomizer.nextInt(settings.mapCellsY)), 
					randomizer.nextInt(21)-10, 
					randomizer.nextBoolean(), 
					Direction.random(randomizer), 
					settings));
		}
		
		for (FlowGenerator f :flowGenerators) {
			f.addFlow(flowMap);
		}
		
		
	}
	
	public void setSimNumber() {

		int maxSimNumber = 0;
		
		File folder = new File("simulation");
		File[] listOfFiles = folder.listFiles();
		
		if (listOfFiles != null) {

			for (int i = 0; i < listOfFiles.length; i++) {
				
				String filename = listOfFiles[i].getName();
				
				int dashIndex = filename.indexOf('-');
				
				maxSimNumber = Math.max(maxSimNumber, Integer.valueOf(filename.substring(0, dashIndex)));

			}
		}
		
		simNumber = maxSimNumber + 1;
	}
	
	
	public void loadSimulation(SimulationState simulationToLoad) {
		
		this.simNumber = simulationToLoad.simulationNumber;
		this.creatures = simulationToLoad.creatures;
		this.flowGenerators = simulationToLoad.flowGenerators;
		this.randomizer = simulationToLoad.randomizer;
		this.frame = simulationToLoad.frame;
		

		this.settings = new Settings(0, 320, 320);
		for (FlowGenerator f : flowGenerators) {
			f.settings = this.settings;
			f.addFlow(flowMap);
		}
	}

	
	
	public void animate() {
		
		while (!paused) {
			
			

			waitForRecordingIfNeeded();

			waitForRenderingIfNeeded();

			
			
			
			performFrameActions();

			frame++;
			
			
			

			if (frame % 50 == 0) {
				renderer.swapActiveCanvas();
				//renderer.clearScreen();
			}
			
			
			render();

			if (frame % 50 == 0) {
				renderer.updateVisibleCanvas();
				renderer.clearHiddenCanvas();
			}
			
			
			if (settings.periodicRecordings > 0 && frame % settings.periodicRecordings == 0) {
				
					
					try {
						
						record();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
			}
			
//			System.out.println(
//					"flowMap[160][165] = " + flowMap[160][165].x + " " + flowMap[160][165].y + 
//					" | flowMap[161][161] = " + flowMap[161][161].x + " " + flowMap[161][161].y + 
//					" | flowMap[180][180] = " + flowMap[180][180].x + " " + flowMap[180][180].y);
			
			
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
		
		
	}
	
	
	
	public void waitForRecordingIfNeeded() {
		while (recording) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void waitForRenderingIfNeeded() {
		while (rendering) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	public void render() {

		rendering = true;
		
		Thread thread = null;
		thread = new Thread(new Runnable() {
		    public void run() {
		    	renderer.render(creatures, flowMap, oldCreaturePositionsX, oldCreaturePositionsY);

				rendering = false;
		    }
		});
		thread.start();
	}
	
	
	public void record() throws IOException {
		
		recording = true;
		
		Thread thread = null;
		thread = new Thread(new Runnable() {
		    public void run() {

				try {
					
					FileOutputStream simulationFile = new FileOutputStream(
							"simulations/" + String.format("%04d", simNumber) + "-" + String.format("%09d", frame));
				
					ObjectOutputStream out = new ObjectOutputStream(simulationFile);
					out.writeObject(getFrameResult());
					out.close();
					simulationFile.close();
					recording = false;
					
				} catch (CloneNotSupportedException | IOException e) {
					e.printStackTrace();
				}
		    }
		});
		thread.start();
		
	}
	
	
	
	
	public SimulationState getFrameResult() throws CloneNotSupportedException {
		
		ArrayList<Creature> frameResultCreatures = new ArrayList<Creature>();
		for (Creature c : creatures) {
			frameResultCreatures.add((Creature) c.clone());
		}

		ArrayList<FlowGenerator> frameResultFlowGenerators = new ArrayList<FlowGenerator>();
		for (FlowGenerator f : flowGenerators) {
			frameResultFlowGenerators.add((FlowGenerator) f.clone());
		}
		
		CloneableRandom frameRandom = (CloneableRandom) randomizer.clone();
		
		SimulationState simulationState = new SimulationState(
				simNumber, frameResultCreatures, frameResultFlowGenerators, frameRandom, frame);
		
		return simulationState;
	}
	
	
	
	
	public void performFrameActions() {
		moveGenerators();
		moveCreatures();
		adjacentActions();
	}
	

	
	
	private void moveGenerators() {
		
		for (FlowGenerator g : flowGenerators) {
			g.tick(frame, randomizer, flowMap);
		}
		
	}
	
	private void moveCreatures() {
		
		int lastPixelX = 319;
		int lastPixelY = 319;
		
		oldCreaturePositionsX.clear();
		oldCreaturePositionsY.clear();
		
		for (Creature c : creatures) {
			
			
			if (randomizer.nextInt(1500) < Math.abs(flowMap[c.x][c.y].x)) {
				
				oldCreaturePositionsX.add(c.x);
				oldCreaturePositionsY.add(c.y);
				if (flowMap[c.x][c.y].x > 0) {
					c.x = Math.max(0,Math.min(lastPixelX, c.x + 1));
				} else if (flowMap[c.x][c.y].x < 0) {
					c.x = Math.max(0,Math.min(lastPixelX, c.x - 1));
				}
				
			}
			
			if (randomizer.nextInt(1500) < Math.abs(flowMap[c.x][c.y].y)) {
				
				oldCreaturePositionsX.add(c.x);
				oldCreaturePositionsY.add(c.y);
				
				if (flowMap[c.x][c.y].y > 0) {
					c.y = Math.max(0,Math.min(lastPixelY, c.y + 1));
				} else if (flowMap[c.x][c.y].y < 0) {
					c.y = Math.max(0,Math.min(lastPixelY, c.y - 1));
				}
			}
			
//			oldCreaturePositionsX.add(c.x);
//			oldCreaturePositionsY.add(c.y);
//			
//			c.x = Math.max(0,Math.min(lastPixelX, c.x + (randomizer.nextInt(3)-1)));
//			c.y = Math.max(0,Math.min(lastPixelY, c.y + (randomizer.nextInt(3)-1)));
		}
		
	}
	
	private void adjacentActions() {
		
	}



	public void pause() {
		paused = true;
	}



	public void unpause() {
		if (!paused) {
			return;
		}
		paused = false;
		launchAnimationThread();
	}


	public void playOrPause() {
		if (paused) {
			unpause();
		} else {
			pause();
		}
	}



	public void launchAnimationThread() {
		Simulator simulator = this;
		Thread thread = null;
		thread = new Thread(new Runnable() {
		    public void run() {
		    	simulator.animate();
		    }
		});
		thread.start();
	}



	public void renderInitialFrame() {
		renderer.render(creatures, flowMap, new ArrayList<Integer>(), new ArrayList<Integer>());
	}





}
