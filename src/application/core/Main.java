package application.core;
	
import application.gui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		
		try {
			
			configureMainStage(stage);
			
			SceneManager sceneManager = new SceneManager();
			
			sceneManager.setMainMenuScene(stage, sceneManager);
			
			stage.setOnCloseRequest(e -> sceneManager.exitCleanup());
			
			setUserAgentStylesheet(STYLESHEET_CASPIAN);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	
	void configureMainStage(Stage stage) {
		stage.setTitle("Evobox");
		stage.setWidth(1024);
		stage.setHeight(780);
		stage.setMinWidth(1024);
		stage.setMinHeight(780);
	}
}
