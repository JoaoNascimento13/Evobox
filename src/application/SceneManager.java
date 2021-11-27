package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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
		
		Parent root = FXMLLoader.load(getClass().getResource("/views/SimulationView.fxml"));
		Scene simulationScene = new Scene(root,windowWidth,windowHeight);

		System.out.println(root);
		

		int canvasWidth = 640;
		int canvasHeight = 640;
		
		Canvas canvas = new Canvas(canvasWidth, canvasHeight);
		
		((GridPane)root).add(canvas, 1, 1);
		
		Renderer renderer = new Renderer(canvas, canvasWidth, canvasHeight);
		
		Simulator simulator = new Simulator(renderer);
		
		simulator.populateWorld();
		
		simulationScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		
		stage.setScene(simulationScene);
		stage.show();
		
		Thread thread = null;
		thread = new Thread(new Runnable() {
		    public void run() {
		    	simulator.animate();
		    }
		});
		thread.start();
		
		
		
	}
	
}
