package application.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;

import application.core.Renderer;
import application.core.SimulationState;
import application.core.Simulator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class SceneManager {

	
	

	private int windowWidth = 1024;
	private int windowHeight = 760; 
	
	private Scene simulationScene;
	private FXMLLoader simulationLoader;
	private Renderer renderer;
	public Simulator simulator;
	
	
	public void setMainMenuScene(Stage stage, SceneManager sceneManager) throws IOException {

		URL mainMenuViewURL = getClass().getResource("/views/MainMenuView.fxml");
		
		FXMLLoader mainMenuLoader = new FXMLLoader(mainMenuViewURL);
		
		Parent root = mainMenuLoader.load();
		
		Scene mainMenuScene = new Scene(root,windowWidth,windowHeight);
		
		mainMenuScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		
		

		MainMenuController controller = mainMenuLoader.getController();
		
		controller.setSceneManager(sceneManager);
		
		
		stage.setScene(mainMenuScene);
		stage.show();
	}

	
	
	public void setSimulationScene(Stage stage) throws IOException {
		
		
		URL viewURL = getClass().getResource("/views/SimulationView.fxml");
		
		
		simulationLoader = new FXMLLoader(viewURL);
		
		Parent root = simulationLoader.load();
		

		SimulatorController simulatorController = (SimulatorController) simulationLoader.getController();
		
		simulationScene = new Scene(root,windowWidth,windowHeight);
		
		
		
		int canvasWidth = 640;
		int canvasHeight = 640;
		
		Canvas canvasA = new Canvas(canvasWidth, canvasHeight);

		Canvas canvasB = new Canvas(canvasWidth, canvasHeight);
		
		MapScrollPane mapScrollPaneA = new MapScrollPane(canvasA, simulatorController);

		MapScrollPane mapScrollPaneB = new MapScrollPane(canvasB, simulatorController);

		mapScrollPaneA.backup = mapScrollPaneB;
		
		mapScrollPaneB.backup = mapScrollPaneA;
		
//		mapScrollPane.setStyle("-fx-background-color: red");
		
//		((GridPane)root).add(mapScrollPaneA, 1, 1);
		
		simulatorController.setMapScrollPane(mapScrollPaneA);

		
//		GridPane rootTest = ((GridPane)mapScrollPaneA.getParent());
//		System.out.println("Nodes: ");
//		for (Node n : rootTest.getChildren()) {
//			System.out.println(n);
//		}
		
		
		
		
//		canvas.setStyle("-fx-background-color: red");
		
		renderer = new Renderer(canvasA, canvasB, mapScrollPaneA, canvasWidth, canvasHeight);
		
		
		simulationScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
	}
	
	
	
	public void setNewSimulation() {
		
		simulator = new Simulator(renderer);
		
		simulator.setSimNumber();
		
	}
	

	public void loadSimulation() {
		
    	SimulationState simulationToLoad = null;
    	
        try {

    		File folder = new File("simulations");
    		File[] listOfFiles = folder.listFiles();
    		
    		if (listOfFiles == null) {
    			System.out.println("No files to load.");
    			return;
    		}
			
			
    		String path = listOfFiles[listOfFiles.length-1].getPath();
            
    		FileInputStream simulationFile = new FileInputStream(path);
    		
    		ObjectInputStream in = new ObjectInputStream(simulationFile);
    		
    		
    		simulationToLoad = (SimulationState)in.readObject();
    		
    		
    		in.close();
    		simulationFile.close();
            
    		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		simulator = new Simulator(renderer);
		
		
		simulator.loadSimulation(simulationToLoad);
		
		
	}
	

	
	
	public void setSimulationController() {

		SimulatorController controller = simulationLoader.getController();
		
		controller.setSimulator(simulator);
		controller.setRenderer(renderer);
		
		simulator.setController(controller);
		
	}
	
	
	
	public void showSimulation(Stage stage) {
		
		simulator.renderInitialFrame();
		
		stage.setScene(simulationScene);
		stage.show();
	}



	public void exitCleanup() {
		if (simulator != null) {
			try {
				simulator.record();
				
				simulator.waitForRecordingIfNeeded();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}



	public boolean hasLoadedSimulator() {
		if (simulator == null) {
			return false;
		}
		return true;
	}
	
}
