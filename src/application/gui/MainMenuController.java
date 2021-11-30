package application.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MainMenuController {
	
	private SceneManager sceneManager;
	
	
	public void setSceneManager(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}
	
	public void newSim(ActionEvent e) throws IOException {
		
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
				
		//SceneManager sceneManager = new SceneManager();
		
		sceneManager.setSimulationScene(stage);

		sceneManager.setNewSimulation();
		
		sceneManager.setSimulationController();
		
		sceneManager.showSimulation(stage);
		
//		sceneManager.saveFirstSimulatorFrame();
	}
	
	
	public void loadSim(ActionEvent e) throws IOException {

		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		
		//SceneManager sceneManager = new SceneManager();
		
		sceneManager.setSimulationScene(stage);

		sceneManager.loadSimulation();

		if (!sceneManager.hasLoadedSimulator()) {
			return;
		}
		
		sceneManager.setSimulationController();
		
		sceneManager.showSimulation(stage);
		
	}
	
	
	public void settings(ActionEvent e) {
		
	}
	
	public void exit(ActionEvent e) {
		sceneManager.exitCleanup();
		System.exit(0);
	}
	
}
