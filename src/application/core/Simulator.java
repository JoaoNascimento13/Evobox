package application.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import application.dynamic.creatures.Creature;
import application.dynamic.creatures.Species;
import application.dynamic.factories.PreexistingCreatureFactory;
import application.dynamic.flow.FlowGenerator;
import application.dynamic.flow.PulsePointFlowGenerator;
import application.gui.SimulatorController;

import java.awt.Point;

public class Simulator {
	
	public long tick;
	
	
	
	private Renderer renderer;
	
	public boolean paused;

	public boolean running;
	private boolean recording;
	private boolean rendering;
	
	public int totalActiveCreatures;
	
//	private FileOutputStream simulationFile;

	private RandomizerSingleton randomizer;
	
	private int simNumber;
	
	public SimulatorController simulatorController;
	

	
	//private Point[][] flowMap;
	
	public Simulator() {

		
	}


	public void setupSimulator(Renderer renderer) {

		this.renderer = renderer;
		
		tick = 0;
		
		paused = true;
		recording = false;
		rendering = false;
		
		randomizer = RandomizerSingleton.getInstance();
		
		SettingsSingleton settings = SettingsSingleton.getInstance();
		
		settings.setMapSize(320, 320);
		settings.setPeriodicRecordings(0);
	    settings.setBirthsPerMutation(200);
	    settings.setMutationsPerDietChange(200);
	    settings.setMutationsPerSizeChange(200);

		MapStateSingleton.getInstance().initialize();
	}
	
	
	
	public void populateWorld() {

		SettingsSingleton settings = SettingsSingleton.getInstance();
		
		PreexistingCreatureFactory preexistingCreatureFactory = new PreexistingCreatureFactory();
		
		
		Species originalSpecies = new Species(settings.getStarterGenome());
		originalSpecies.parent = originalSpecies;
		
		preexistingCreatureFactory.setStarterSpecies(originalSpecies);
		for (int i = 0; i < 15000; i++) {
			populateWorldWithCreature(preexistingCreatureFactory.createCreature(0));
		}

		Species originalSpeciesB = new Species(settings.getStarterGenome());
		originalSpeciesB.parent = originalSpeciesB;
		
		
		preexistingCreatureFactory.setStarterSpecies(originalSpeciesB);
		for (int i = 0; i < 10; i++) {
			populateWorldWithCreature(preexistingCreatureFactory.createCreature(0));
		}
		
		
		ArrayList<FlowGenerator> flowGenerators = MapStateSingleton.getInstance().flowGenerators;
		
		for (int i = 0; i < 10; i++) {

			flowGenerators.add(new PulsePointFlowGenerator(
					200, 
					8+randomizer.nextInt(3), 
					new Point(randomizer.nextInt(settings.mapCellsX), randomizer.nextInt(settings.mapCellsY)), 
					randomizer.nextInt(21)-10, 
					randomizer.nextBoolean(), 
					Direction.random(randomizer)));
		}
		
		
		for (FlowGenerator f :flowGenerators) {
			f.addFlow();
		}
		
		simulatorController.updateSidePane();
	}
	

	public void populateWorldWithCreature(Creature creature) {
		MapStateSingleton.getInstance().registerCreature(creature);
		MapStateSingleton.getInstance().setCreatureInPoint(creature);
		
	}
	
	
	
	public void animate() {
		

		SettingsSingleton settings = SettingsSingleton.getInstance();
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		while (!paused) {
			
			waitForRecordingIfNeeded();

			waitForRenderingIfNeeded();

			running = true;
			
			performTickSimulations();
			
			simulatorController.updateSidePane(); 
			//Needs to be done BEFORE unregisterDeadCreatures(), otherwise the selected point won't get cleared.
			
			mapState.unregisterDeadCreatures();
			
			mapState.registerBornCreatures();
			

			running = false;
			
			
			int creatureNumber = mapState.activeCreatures.size();
			
//			System.out.println(creatureNumber + " creatures");
			
//			for (Species s : mapState.activeSpecies) {
//				System.out.println("Species " + s.name + ": " + (100*((double)s.currentMutatedMembers)/s.currentMembers));
//			}
			
			
			
			if (creatureNumber == 0) {
				paused = true;
			}
			
			tick++;


			
			if (tick % 50 == 0) {
				renderer.changeVisibleMapScrollPane();
			}
			
			render();

			
			if (settings.periodicRecordings > 0 && tick % settings.periodicRecordings == 0) {
					
					try {
						
						record();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
			}
			
			
		}
		
		
	}
	

//	public void requestOverviewUpdateIfNeeded() {
//		
//		if (simulatorController.inCreatureView()) {
//
//			MapStateSingleton mapState = MapStateSingleton.getInstance();
//			
//			boolean focusIsDead = false;
//			for (long i : mapState.deadCreaturesToRemoveIds) {
//				if (mapState.focusedCreature.id == i) {
//					focusIsDead = true;
//					break;
//				}
//			}
//			if (!focusIsDead) {
//				simulatorController.fillDynamicCreatureDetails(mapState.focusedCreature);
//			} else {
//				mapState.focusedCreature = null;
//				simulatorController.showGeneralView();
//				
//			}
//		}
//	}
	
	
	public void waitForRunningIfNeeded() {
		while (running) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
//	public void waitForSwappingCanvasIfNeeded() {
//		while (swappingCanvas) {
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	
//	private boolean swappingCanvas;
//	
//	public void swapVisibleCanvas() {
//		
//		renderer.changeVisibleMapScrollPane();
////		swappingCanvas = true;
////		Thread thread = new Thread(new Runnable() {
////		    public void run() {
////				renderer.updateVisibleCanvas();
////				renderer.clearHiddenCanvas();
////				
////				swappingCanvas = false;
////		    }
////		});
////		thread.setPriority(10);
////		thread.start();
//	}
	
	

	public void render() {

		rendering = true;
		
		Thread thread = new Thread(new Runnable() {
		    public void run() {
		    	renderer.render();

				rendering = false;
		    }
		});
		thread.start();
	}
	
	
	
	
	public void record() throws IOException {
		
		recording = true;
		
		boolean unpauseWhenDone = false;
		if (!paused) {
			pause();
			unpauseWhenDone = true;
		}
		
		final boolean unpauseWhenDoneFinal = unpauseWhenDone;
		
		waitForRunningIfNeeded();
		
		Thread thread = new Thread(new Runnable() {
		    public void run() {

				try {
					FileOutputStream simulationFile = new FileOutputStream(
							"simulations/" + String.format("%04d", simNumber) + "-" + String.format("%09d", tick));
					ObjectOutputStream out = new ObjectOutputStream(simulationFile);
					out.writeObject(getStateOnTick());
					out.close();
					simulationFile.close();
					recording = false;
					
					if (unpauseWhenDoneFinal) {
						unpause();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		});
		thread.start();
		
	}
	
	
	
	
	public SimulationState getStateOnTick() {
		
		ArrayList<Creature> frameResultCreatures = new ArrayList<Creature>();

		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		for (Creature c : mapState.activeCreatures) {
			frameResultCreatures.add(c);
//			frameResultCreatures.add((Creature) c.clone());
		}

		ArrayList<FlowGenerator> frameResultFlowGenerators = new ArrayList<FlowGenerator>();
		for (FlowGenerator f : mapState.flowGenerators) {
			frameResultFlowGenerators.add(f);
//			frameResultFlowGenerators.add((FlowGenerator) f.clone());
		}

		RandomizerSingleton frameRandom = randomizer;
		
//		CloneableRandom frameRandom = (CloneableRandom) randomizer.clone();
		
		SimulationState simulationState = new SimulationState(
				simNumber, frameResultCreatures, frameResultFlowGenerators, frameRandom, tick);
		
		return simulationState;
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

		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		this.simNumber = simulationToLoad.simulationNumber;
		this.randomizer = simulationToLoad.randomizer;
		this.tick = simulationToLoad.tick;
		

		mapState.activeCreatures = simulationToLoad.creatures;
		mapState.flowGenerators = simulationToLoad.flowGenerators;
		
		for (FlowGenerator f : mapState.flowGenerators) {
			f.addFlow();
		}
		

//		for (Creature c : mapState.creatures) {
//			for (Creature d : mapState.creatures) {
//				if (c.id == d.id && !(c == d)) {
//					System.out.println("ID ERROR!");
//				}
//				if (c.x == d.x && c.y == d.y && !(c == d)) {
//					System.out.println("LOCATION ERROR!");
//				}
//			}
//		}

		
		for (Creature c : mapState.activeCreatures) {
			mapState.setCreatureInPoint(c);
		}
		
		
	}

	
	
	
	
	
	public void performTickSimulations() {

		moveGenerators();
		animateCreatures();
		adjacentActions();
	}
	

	
	
	private void moveGenerators() {

		MapStateSingleton mapState = MapStateSingleton.getInstance();
		for (FlowGenerator g : mapState.flowGenerators) {
			g.tick(tick, randomizer);
		}
		
	}

	private void animateCreatures() {
		
		OutdatedPositionsSingleton.getInstance().clearPositions();
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		for (Creature c : mapState.activeCreatures) {
			c.exposeToActivation(tick);
		}
		
		
		Creature focusedCreature = MapStateSingleton.getInstance().focusedCreature;
		
		if (focusedCreature != null) {
			simulatorController.fillDynamicCreatureDetails(focusedCreature);
		} else {
			simulatorController.showGeneralView();
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


//	public void playOrPause() {
//	}



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
		renderer.render();
	}



	public void setController(SimulatorController simulatorController) {
		this.simulatorController = simulatorController;
	}





}
