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

public class SceneManagerSingleton {

	
	

	private int windowWidth = 1024;
	private int windowHeight = 760; 
	
	private Scene simulationScene;
	private FXMLLoader simulationLoader;
	public Renderer renderer;
	public Simulator simulator;
	public SimulatorController simulatorController;
	
	private static final SceneManagerSingleton sceneManager = new SceneManagerSingleton();
	
	private SceneManagerSingleton() {}
	
	public static SceneManagerSingleton getInstance() {
		return sceneManager;
	}
	
	public void setMainMenuScene(Stage stage, SceneManagerSingleton sceneManager) throws IOException {

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


		simulatorController = (SimulatorController) simulationLoader.getController();
		
		simulationScene = new Scene(root,windowWidth,windowHeight);
		
		
		
		int canvasWidth = 640;
		int canvasHeight = 640;
		
		Canvas canvasA = new Canvas(canvasWidth, canvasHeight);

		Canvas canvasB = new Canvas(canvasWidth, canvasHeight);
		
		MapScrollPane mapScrollPaneA = new MapScrollPane(canvasA, simulatorController);

		MapScrollPane mapScrollPaneB = new MapScrollPane(canvasB, simulatorController);

		mapScrollPaneA.backup = mapScrollPaneB;
		
		mapScrollPaneB.backup = mapScrollPaneA;
		
		simulatorController.setMapScrollPane(mapScrollPaneA);

		mapScrollPaneA.setStyle("-fx-focus-color: transparent;");
		mapScrollPaneB.setStyle("-fx-focus-color: transparent;");
		
		
		renderer = new Renderer(canvasA, canvasB, mapScrollPaneA, canvasWidth, canvasHeight);

		simulatorController.setGeneralMouseListeners();
		
		simulationScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		
	}
	
	
	
	public void setNewSimulation() {
		
		simulator = new Simulator();
		simulator.setupSimulator(renderer);
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
		

		simulator = new Simulator();
		
		simulator.setupSimulator(renderer);
		
		simulator.loadSimulation(simulationToLoad);
		
		
	}
	

	
	
	public void setSimulationController() {
		simulatorController.setSimulator(simulator);
		simulatorController.setRenderer(renderer);
		
		simulator.setController(simulatorController);
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
