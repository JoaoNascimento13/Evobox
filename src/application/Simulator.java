package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Simulator {
	
	public int frame;
	
	private ArrayList<Creature> creatures;
	private ArrayList<Integer> oldCreaturePositionsX;
	private ArrayList<Integer> oldCreaturePositionsY;
	
	private Renderer renderer;
	
	private boolean paused;
	private boolean recording;
	
//	private FileOutputStream simulationFile;

	private CloneableRandom randomizer;
	
	private int simNumber;
	
	int periodicRecordings = 0;
	
	
	public Simulator(Renderer renderer) {

		this.renderer = renderer;
		
		this.frame = 0;
		
		paused = true;
		recording = false;
		
		creatures = new ArrayList<Creature>();
		oldCreaturePositionsX = new ArrayList<Integer>();
		oldCreaturePositionsY = new ArrayList<Integer>();
		
		
		randomizer = new CloneableRandom();
		
		

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

			creatures.add(new Creature(160, 160));
			//creatures.add(new Creature(randomizer.nextInt(320), randomizer.nextInt(320)));
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
		this.randomizer = simulationToLoad.randomizer;
		this.frame = simulationToLoad.frame;
	}

	
	
	public void animate() {
		
		while (!paused) {
			
			

			waitForRecordingIfNeeded();
			
			
			
			performFrameActions();

			frame++;
			
			
			
			
			renderer.render(creatures, oldCreaturePositionsX, oldCreaturePositionsY);
			
			
			if (periodicRecordings > 0 && frame % periodicRecordings == 0) {
				

				try {
					

//					SimulationState frameResult = getFrameResult();

					waitForRecordingIfNeeded();
					
					record();
					
				} catch (IOException e) {
					recording = false;
					e.printStackTrace();
				}
				
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

	
	public void record() throws IOException {
		
		
		try {
			
			
			FileOutputStream simulationFile = new FileOutputStream(
					"simulations/" + String.format("%04d", simNumber) + "-" + String.format("%09d", frame));
		
			ObjectOutputStream out = new ObjectOutputStream(simulationFile);
			out.writeObject(getFrameResult());
			out.close();
			simulationFile.close();
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	
//	public void recordFirstFrame() throws IOException {
//		
//		try {
//
//			ObjectOutputStream out = new ObjectOutputStream(simulationFile);
//			out.writeObject(getFrameResult());
//			
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//	}

	
//	public void recordSubsequentFrame(SimulationState simulationState) {
//
//		recording = true;
//		
//		Thread thread = new Thread(new Runnable() {
//		    public void run() {
//				try {
//					AppendingObjectOutputStream out = getStream();
//					out.writeObject(simulationState);
//					
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				recording = false;
//		    }
//		});
//		thread.start();
//	}
	
	
//	public AppendingObjectOutputStream getStream() throws IOException {
//		return new AppendingObjectOutputStream(simulationFile);
//	}
	
	
	public SimulationState getFrameResult() throws CloneNotSupportedException {
		
		ArrayList<Creature> frameResultCreatures = new ArrayList<Creature>();
		for (Creature c : creatures) {
			frameResultCreatures.add((Creature) c.clone());
		}
		
		CloneableRandom frameRandom = (CloneableRandom) randomizer.clone();
		
		SimulationState simulationState = new SimulationState(simNumber, frameResultCreatures, frameRandom, frame);
		
		return simulationState;
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
			
			c.x = Math.max(0,Math.min(lastPixelX, c.x + (randomizer.nextInt(3)-1)));
			c.y = Math.max(0,Math.min(lastPixelY, c.y + (randomizer.nextInt(3)-1)));
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
		renderer.render(creatures, new ArrayList<Integer>(), new ArrayList<Integer>());
	}





}
