package application.gui;

import application.core.MapStateSingleton;
import application.core.Renderer;
import application.core.Simulator;
import application.dynamic.Creature;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SimulatorController {
	
	Simulator simulator;
	Renderer renderer;
	
	@FXML
	private Button playOrPause;
	
	@FXML
	private StackPane overviewPane;
	@FXML
	private VBox generalView;
	@FXML
	private VBox speciesView;
	@FXML
	private VBox creatureView;
	
	@FXML
	private ProgressBar healthBar;
	@FXML
	private ProgressBar foodBar;
	@FXML
	private ProgressBar ageBar;
	
	
	
	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	
//	public void pause() {
//		simulator.pause();
//	}
//	
//	public void play() {
//		simulator.unpause();
//	}

	public void playOrPause() {
		if (simulator.paused) {
			playOrPause.setText("Pause");
			simulator.unpause();
		} else {
			playOrPause.setText("Play");
			simulator.pause();
		}
//		simulator.playOrPause();
	}
	

	public void zoomIn() {
		renderer.zoomIn();
	}
	
	public void zoomOut() {
		renderer.zoomOut();
	}
	
	
	
	public void fillCreatureDetails(Creature creature) {
		
    	System.out.println("Clicked on creature: " + creature + " on " + creature.x + ", " + creature.y);
    	
    	fillDynamicCreatureDetails(creature);

    	showCreatureView();
    	
    	MapStateSingleton.getInstance().setFocusedCreature(creature);
    	
    	simulator.render();
	}
	
	
	
	public void fillDynamicCreatureDetails(Creature creature) {
		
    	try {
    		
			foodBar.setProgress(((double)creature.food)/creature.feedingStrategy.getMaximumFoodStorage());
			
			ageBar.setProgress(((double)creature.age)/creature.genome.ageExpectancy);
			
		} catch (IllegalStateException e) {
			
			//Will throw an exception if creature is already dead.
			
			//Can be ignored, the focus will be cleared afterwards.
			
		}
	}
	

	public void showGeneralView() {
		

		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	generalView.toFront();
		    }
		});
	}
	public void showSpeciesView() {
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	speciesView.toFront();
		    }
		});
	}
	public void showCreatureView() {
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	creatureView.toFront();
		    }
		});
	}
	
	public boolean inCreatureView() {
		if (overviewPane.getChildren().get(overviewPane.getChildren().size()-1) == creatureView) {
			return true;
		} else {
			return false;
		}
	}
	public boolean inSpeciesView() {
		if (overviewPane.getChildren().get(overviewPane.getChildren().size()-1) == speciesView) {
			return true;
		} else {
			return false;
		}
	}
	public boolean inGeneralView() {
		if (overviewPane.getChildren().get(overviewPane.getChildren().size()-1) == generalView) {
			return true;
		} else {
			return false;
		}
	}
	
}
