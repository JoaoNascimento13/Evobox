package application;
	
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			primaryStage.setTitle("Evobox");
			primaryStage.setWidth(1280);
			primaryStage.setHeight(800);
			primaryStage.setMinWidth(1280);
			primaryStage.setMinHeight(800);
			
			new SceneManager().setMainMenuScene(primaryStage);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
