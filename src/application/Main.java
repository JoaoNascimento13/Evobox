package application;
	
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		
		
		try {
			
			configureMainStage(stage);
			
			new SceneManager().setMainMenuScene(stage);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	
	void configureMainStage(Stage stage) {
		stage.setTitle("Evobox");
		stage.setWidth(1280);
		stage.setHeight(800);
		stage.setMinWidth(1280);
		stage.setMinHeight(800);
	}
}
