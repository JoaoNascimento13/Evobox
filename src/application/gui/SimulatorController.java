package application.gui;

import java.util.ArrayList;

import application.core.MapStateSingleton;
import application.core.Renderer;
import application.core.Simulator;
import application.dynamic.Creature;
import application.dynamic.Species;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
	private GridPane mapContainer;
	
	
	
	@FXML
	private VBox overviewSpeciesName;
	@FXML
	private VBox overviewSpeciesPercentage;

	@FXML
	private Label creatureSpecies;
	@FXML
	private Label creatureSpeciesNumber;
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
	
	
	
	
	public void updateSidePane() {
		
		if (inGeneralView()) {
			
			updateOverview();
			
		} else if (inSpeciesView()) {
			
			
		} else {
			
			MapStateSingleton mapState = MapStateSingleton.getInstance();
			
			boolean focusIsDead = false;
			for (long i : mapState.deadCreaturesToRemoveIds) {
				if (mapState.focusedCreature.id == i) {
					focusIsDead = true;
					break;
				}
			}
			if (!focusIsDead) {
				fillDynamicCreatureDetails(mapState.focusedCreature);
			} else {
				mapState.focusedCreature = null;
				showGeneralView();
			}
		}
	}
	
	
	public void fillCreatureDetails(Creature creature) {
		
    	System.out.println("Clicked on creature: " + creature + " on " + creature.x + ", " + creature.y);
    	
    	fillStaticCreatureDetails(creature);
    	fillDynamicCreatureDetails(creature);

    	showCreatureView();
    	
    	MapStateSingleton.getInstance().setFocusedCreature(creature);
    	
    	simulator.render();
	}
	

	public void fillStaticCreatureDetails(Creature creature) {

		creatureSpecies.setText(creature.species.name);
		creatureSpeciesNumber.setText(String.valueOf(creature.numberInSpecies));
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
	
	

	public void addSpeciesToOverview(Species originalSpecies) {
		Label name = new Label(originalSpecies.name);
		int maxNumberOfCreaturesOfSameSpecies = MapStateSingleton.getInstance().getMaxNumberOfCreaturesOfSameSpecies();
		ProgressBar percentage = new ProgressBar(((double)originalSpecies.currentMembers)/maxNumberOfCreaturesOfSameSpecies);
		name.setId("name"+originalSpecies.id);
		percentage.setId("percentage"+originalSpecies.id);
		percentage.setPrefHeight(16);
		percentage.setPrefWidth(202);
		percentage.setPadding(new Insets(3, 0, -1, 0));
		overviewSpeciesName.getChildren().add(name);
		overviewSpeciesPercentage.getChildren().add(percentage);
	}

	
	public void updateOverview() {
		
		int maxNumberOfCreaturesOfSameSpecies = MapStateSingleton.getInstance().getMaxNumberOfCreaturesOfSameSpecies();
		ObservableList<Node> percentage = overviewSpeciesPercentage.getChildren();
		ArrayList<Species> activeSpecies = MapStateSingleton.getInstance().activeSpecies;
		for (Species s : activeSpecies) {
			for (Node n : percentage) {
				if (n.getId().equals("percentage"+s.id)) {
					((ProgressBar)n).setProgress(((double)s.currentMembers)/maxNumberOfCreaturesOfSameSpecies);
				}
			}
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
	public void setMapScrollPane(MapScrollPane mapScrollPane) {
		this.mapContainer.add(mapScrollPane, 0, 0);
//		this.mapContainer.getChildren().add(mapScrollPane);
	}
	public void removeMapScrollPane() {
		this.mapContainer.getChildren().clear();
	}
	
}
