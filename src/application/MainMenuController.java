package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MainMenuController {
	
	
	public void newSim(ActionEvent e) throws IOException {
		
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
				
		new SceneManager().setSimulationScene(stage);
	}
	
	public void loadSim(ActionEvent e) {
		
	}
	
	public void settings(ActionEvent e) {
		
	}
	
	public void exit(ActionEvent e) {
		System.exit(0);
	}
	
}
