package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SceneManager {

	
	
	
	public void setMainMenuScene(Stage stage) {

		
		
		//BorderPane root = new BorderPane();
		
		try {
		
		Parent root = FXMLLoader.load(getClass().getResource("/views/MainMenuView.fxml"));
		
		
		Scene mainMenuScene = new Scene(root,1280,800);
		
		
		mainMenuScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		stage.setScene(mainMenuScene);
		stage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
