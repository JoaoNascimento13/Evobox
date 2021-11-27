package application;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SceneManager {

	
	

	private int windowWidth = 1280;
	private int windowHeight = 800; 
	
	
	
	
	public void setMainMenuScene(Stage stage) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/views/MainMenuView.fxml"));
		Scene mainMenuScene = new Scene(root,windowWidth,windowHeight);
		
		mainMenuScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		
		
		stage.setScene(mainMenuScene);
		stage.show();
	}

	
	public void setSimulationScene(Stage stage) throws IOException {
		
		
		URL viewURL = getClass().getResource("/views/SimulationView.fxml");
		
		//Parent root = FXMLLoader.load(viewURL);
		
		
		FXMLLoader loader = new FXMLLoader(viewURL);
		
		Parent root = loader.load();
				
		
		
		Scene simulationScene = new Scene(root,windowWidth,windowHeight);

		System.out.println(root);
		

		
		
		int canvasWidth = 640;
		int canvasHeight = 640;
		
		Canvas canvas = new Canvas(canvasWidth, canvasHeight);
		
		
		//((GridPane)root).setGridLinesVisible(true);
		
		MapScrollPane mapScrollPane = new MapScrollPane(canvas);
		
		
		//ScrollPane scrollPane = (ScrollPane) ((GridPane)root).getChildren().get(0);
		
		
		//scrollPane.setContent(canvas);
		
		((GridPane)root).add(mapScrollPane, 1, 1);
		
		Renderer renderer = new Renderer(canvas, mapScrollPane, canvasWidth, canvasHeight);
		
		

		
		
		Simulator simulator = new Simulator(renderer);
		
		simulator.populateWorld();
		
		
		
		
		
		simulationScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		
		stage.setScene(simulationScene);
		stage.show();
		
		


		SimulatorController controller = loader.getController();
		
		
		//System.out.println(controller);
		
		controller.setSimulator(simulator);
		controller.setRenderer(renderer);
		
		
		
		simulator.launchAnimationThread();
		
		
		
		
		
	}
	
}
