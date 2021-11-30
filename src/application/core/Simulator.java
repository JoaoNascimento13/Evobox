package application.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import application.dynamic.Creature;
import application.dynamic.CreatureFactory;
import application.dynamic.FlowGenerator;
import application.dynamic.PreexistingCreatureFactory;
import application.dynamic.PulsePointFlowGenerator;

import java.awt.Point;

public class Simulator {
	
	public long tick;
	
	private ArrayList<Creature> creatures;
	
	

	private ArrayList<FlowGenerator> flowGenerators;
	
	private Renderer renderer;
	
	private boolean paused;
	
	private boolean recording;
	private boolean rendering;
	
//	private FileOutputStream simulationFile;

	private CloneableRandom randomizer;
	
	private int simNumber;
	
	
	

	
	//private Point[][] flowMap;
	
	public Simulator(Renderer renderer) {

		this.renderer = renderer;

//		settings = new SettingsSingleton(0, 320, 320);
		
		
		tick = 0;
		
		paused = true;
		recording = false;
		rendering = false;
		
		creatures = new ArrayList<Creature>();
		flowGenerators = new ArrayList<FlowGenerator>();
		
		
		
		randomizer = new CloneableRandom();
		
		SettingsSingleton settings = SettingsSingleton.getInstance();
		

		settings.setMapSize(320, 320);
	   
		settings.setPeriodicRecordings(0);
	   

		MapStateSingleton.getInstance().initialize();
		
		
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
		
		CreatureFactory preexistingCreatureFactory = new PreexistingCreatureFactory();
		
		for (int i = 0; i < 15000; i++) {
			populateWorldWithCreature(preexistingCreatureFactory.createCreature(randomizer));
		}
		
		
		SettingsSingleton settings = SettingsSingleton.getInstance();
		
		for (int i = 0; i < 20; i++) {

			flowGenerators.add(new PulsePointFlowGenerator(
					100, 
					1+randomizer.nextInt(5), 
					new Point(randomizer.nextInt(settings.mapCellsX), randomizer.nextInt(settings.mapCellsY)), 
					randomizer.nextInt(21)-10, 
					randomizer.nextBoolean(), 
					Direction.random(randomizer)));
		}
		
		
		for (FlowGenerator f :flowGenerators) {
			f.addFlow();
		}
	}
	

	public void populateWorldWithCreature(Creature creature) {
		creatures.add(creature);
		MapStateSingleton.getInstance().setCreature(creature);
	}
	
	
	public void setSimNumber() {

		int maxSimNumber = 0;
		
		File folder = new File("simulation");
		File[] listOfFiles = folder.listFiles();
		
		if (listOfFiles != null) {

			for (int i = 0; i < listOfFiles.length; i++) {
				
				String filename = listOfFiles[i].getName();
				
				int dashIndex = filename.indexOf('-');
				
				System.out.println(filename.substring(0, dashIndex) + " -> " + Integer.valueOf(filename.substring(0, dashIndex)));
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
		this.tick = simulationToLoad.tick;
		

//		this.settings = new SettingsSingleton(0, 320, 320);
		for (FlowGenerator f : flowGenerators) {
			f.addFlow();
		}
	}

	
	
	public void animate() {
		

		SettingsSingleton settings = SettingsSingleton.getInstance();
		
		while (!paused) {
			
			

			waitForRecordingIfNeeded();

			waitForRenderingIfNeeded();

			
			performTickActions();
			
			tick++;
			
			if (tick % 50 == 0) {
				renderer.swapActiveCanvas();
			}
			
			render();

			if (tick % 50 == 0) {
				renderer.updateVisibleCanvas();
				renderer.clearHiddenCanvas();
			}
			
			
			if (settings.periodicRecordings > 0 && tick % settings.periodicRecordings == 0) {
					
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
		    	renderer.render(creatures);

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
							"simulations/" + String.format("%04d", simNumber) + "-" + String.format("%09d", tick));
				
					ObjectOutputStream out = new ObjectOutputStream(simulationFile);
					out.writeObject(getStateOnTick());
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
	
	
	
	
	public SimulationState getStateOnTick() throws CloneNotSupportedException {
		
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
				simNumber, frameResultCreatures, frameResultFlowGenerators, frameRandom, tick);
		
		return simulationState;
	}
	
	
	
	
	public void performTickActions() {
		moveGenerators();
		animateCreatures();
		adjacentActions();
	}
	

	
	
	private void moveGenerators() {
		
		for (FlowGenerator g : flowGenerators) {
			g.tick(tick, randomizer);
		}
		
	}

	private void animateCreatures() {
		
		OutdatedPositionsSingleton.getInstance().clearPositions();
		
		for (Creature c : creatures) {
			c.actOrPostpone(tick, randomizer);
		}
	}
	
//	private void moveCreatures() {
//		
//		int lastPixelX = 319;
//		int lastPixelY = 319;
//		
//		OutdatedPositionsSingleton outdatedPositions = OutdatedPositionsSingleton.getInstance();
//		ArrayList<Integer> oldCreaturePositionsX = outdatedPositions.getOutdatedCreaturesX();
//		ArrayList<Integer> oldCreaturePositionsY = outdatedPositions.getOutdatedCreaturesY();
//		
//		for (Creature c : creatures) {
//			
//			
//			
//			if (randomizer.nextInt(1500) < Math.abs(flowMap[c.x][c.y].x)) {
//				
//				oldCreaturePositionsX.add(c.x);
//				oldCreaturePositionsY.add(c.y);
//				if (flowMap[c.x][c.y].x > 0) {
//					c.x = Math.max(0,Math.min(lastPixelX, c.x + 1));
//				} else if (flowMap[c.x][c.y].x < 0) {
//					c.x = Math.max(0,Math.min(lastPixelX, c.x - 1));
//				}
//				
//			}
//			
//			if (randomizer.nextInt(1500) < Math.abs(flowMap[c.x][c.y].y)) {
//				
//				oldCreaturePositionsX.add(c.x);
//				oldCreaturePositionsY.add(c.y);
//				
//				if (flowMap[c.x][c.y].y > 0) {
//					c.y = Math.max(0,Math.min(lastPixelY, c.y + 1));
//				} else if (flowMap[c.x][c.y].y < 0) {
//					c.y = Math.max(0,Math.min(lastPixelY, c.y - 1));
//				}
//			}
//			
//			
//		}
//		
//	}
	
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
		renderer.render(creatures);
	}





}
