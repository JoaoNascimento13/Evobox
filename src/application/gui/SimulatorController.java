package application.gui;

import java.util.ArrayList;

import application.core.Lock;
import application.core.MapStateSingleton;
import application.core.Renderer;
import application.core.Simulator;
import application.dynamic.creatures.Creature;
import application.dynamic.creatures.Species;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
	private VBox overviewSpeciesContainer;
	
//	@FXML
//	private VBox overviewSpeciesName;
//	@FXML
//	private VBox overviewSpeciesPercentage;

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
	
	@FXML
	private Label creatureDiet;
	@FXML
	private ProgressBar creatureSize;
	@FXML
	private ProgressBar creatureLifespan;
	@FXML
	private ProgressBar creatureSpeed;
	@FXML
	private ProgressBar creatureToughness;
	@FXML
	private ProgressBar creatureAttack;
	@FXML
	private ProgressBar creatureDefense;
	@FXML
	private ProgressBar creaturePerception;
	@FXML
	private ProgressBar creatureStealth;
	@FXML
	private ProgressBar creatureFertility;
	@FXML
	private ProgressBar creatureClutchSize;
	@FXML
	private ProgressBar creatureAggression;
	@FXML
	private ProgressBar creatureReactiveness;

	@FXML
	private Label creatureDietLabel;
	@FXML
	private Label creatureSizeLabel;
	@FXML
	private Label creatureLifespanLabel;
	@FXML
	private Label creatureSpeedLabel;
	@FXML
	private Label creatureToughnessLabel;
	@FXML
	private Label creatureAttackLabel;
	@FXML
	private Label creatureDefenseLabel;
	@FXML
	private Label creaturePerceptionLabel;
	@FXML
	private Label creatureStealthLabel;
	@FXML
	private Label creatureFertilityLabel;
	@FXML
	private Label creatureClutchSizeLabel;
	@FXML
	private Label creatureAggressionLabel;
	@FXML
	private Label creatureReactivenessLabel;
	
	@FXML
	private ProgressBar creatureEvolution;
	@FXML
	private Label creatureEvolutionLabel;
	
	
	public boolean modifyingOverview = false;

	
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
		
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		
		boolean forceGeneralView = false;
		if (mapState.focusedCreature == null && inCreatureView()) {
			forceGeneralView = true;
			showGeneralView();
		}
		
		
		if (inGeneralView() || forceGeneralView) {
			

			updateOverview();
			
		} else if (inSpeciesView()) {
			
			
		} else {
			
			boolean focusIsDead = false;
			for (long i : mapState.deadCreaturesToRemoveIds) {
				if (mapState.focusedCreature.id == i) {
					focusIsDead = true;
					break;
				}
			}
			if (!focusIsDead) {
				if (mapState.refreshFocusedCreature) {
					fillCreatureDetails(mapState.focusedCreature);
					mapState.refreshFocusedCreature = false;
				} else {
					fillDynamicCreatureDetails(mapState.focusedCreature);
				}
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

		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	
		    	fillGenomeDetails(creature);
		    	
		    	fillGenomeLabelDetails(creature);
		    }
		});
		
		
	}
	

	public void fillGenomeDetails(Creature creature) {

		creatureSpecies.setText(creature.species.name);
		creatureSpeciesNumber.setText(String.valueOf(creature.numberInSpecies));
		
		creatureDiet.setText(creature.genome.diet.name);

		creatureSize.setProgress(((double)creature.genome.getSize())/10);
		creatureLifespan.setProgress(((double)creature.genome.getAgeExpectancy())/10);
		creatureSpeed.setProgress(((double)creature.genome.getSpeed())/10);
		creatureToughness.setProgress(((double)creature.genome.getToughness())/10);
		creatureAttack.setProgress(((double)creature.genome.getAttackDamage())/10);
		creatureDefense.setProgress(((double)creature.genome.getDefenseDamage())/10);
		creaturePerception.setProgress(((double)creature.genome.getPerception())/10);
		creatureStealth.setProgress(((double)creature.genome.getStealth())/10);
		creatureFertility.setProgress(((double)creature.genome.getFertility())/10);
		creatureClutchSize.setProgress(((double)creature.genome.getClutchSize())/10);
		creatureAggression.setProgress(((double)creature.genome.getAgression())/10);
		creatureReactiveness.setProgress(((double)creature.genome.getReactiveness())/10);
		creatureEvolution.setProgress(((double)creature.genome.getUsedEvoPoints())/creature.genome.getMaxEvoPoints());
	}

	public void fillGenomeLabelDetails(Creature creature) {
		
		if (creature.genome.diet == creature.species.baseGenome.diet) {
			creatureDietLabel.setTextFill(Color.BLACK);
		} else {
			creatureDietLabel.setTextFill(Color.RED);
		}
		changeGenomeLabelColor(creatureSizeLabel, creature.genome.getSize(), creature.species.baseGenome.getSize());
		changeGenomeLabelColor(creatureLifespanLabel, creature.genome.getAgeExpectancy(), creature.species.baseGenome.getAgeExpectancy());
		changeGenomeLabelColor(creatureSpeedLabel, creature.genome.getSpeed(), creature.species.baseGenome.getSpeed());
		changeGenomeLabelColor(creatureToughnessLabel, creature.genome.getToughness(), creature.species.baseGenome.getToughness());
		changeGenomeLabelColor(creatureAttackLabel, creature.genome.getAttackDamage(), creature.species.baseGenome.getAttackDamage());
		changeGenomeLabelColor(creatureDefenseLabel, creature.genome.getDefenseDamage(), creature.species.baseGenome.getDefenseDamage());
		changeGenomeLabelColor(creaturePerceptionLabel, creature.genome.getPerception(), creature.species.baseGenome.getPerception());
		changeGenomeLabelColor(creatureStealthLabel, creature.genome.getStealth(), creature.species.baseGenome.getStealth());
		changeGenomeLabelColor(creatureFertilityLabel, creature.genome.getFertility(), creature.species.baseGenome.getFertility());
		changeGenomeLabelColor(creatureClutchSizeLabel, creature.genome.getClutchSize(), creature.species.baseGenome.getClutchSize());
		changeGenomeLabelColor(creatureAggressionLabel, creature.genome.getAgression(), creature.species.baseGenome.getAgression());
		changeGenomeLabelColor(creatureReactivenessLabel, creature.genome.getReactiveness(), creature.species.baseGenome.getReactiveness());
		
	}

	public void changeGenomeLabelColor(Label label, int creatureValue, int speciesValue) {
		if (creatureValue == speciesValue) {
			label.setTextFill(Color.BLACK);
		} else {
			if (creatureValue > speciesValue) {
				label.setTextFill(Color.GREEN);
			} else {
				label.setTextFill(Color.RED);
			}
		}
	}
	
	
	public void fillDynamicCreatureDetails(Creature creature) {

		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {

		    	try {
		    		
					foodBar.setProgress(((double)creature.food)/creature.getMaximumFoodStorage());
					ageBar.setProgress(((double)creature.age)/creature.genome.getMaxAge());
					healthBar.setProgress(((double)creature.health)/creature.genome.getMaxHealth());
					
					
				} catch (IllegalStateException e) {
					//Will throw an exception if creature is already dead.
					//Can be ignored, the focus will be cleared afterwards.
				}
		    }
		});
		
	}
	
	

	public void addSpeciesToOverview(Species originalSpecies) {

//		System.out.println("Set true addSpeciesToOverview");
		
		Label name = new Label(originalSpecies.name);
		int maxNumberOfCreaturesOfSameSpecies = MapStateSingleton.getInstance().getMaxNumberOfCreaturesOfSameSpecies();
		ProgressBar percentage = new ProgressBar(((double)originalSpecies.currentMembers)/maxNumberOfCreaturesOfSameSpecies);
//		name.setId("name"+originalSpecies.id);
		name.setPrefHeight(12);
		name.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
		name.setMinWidth(80);
		name.setPrefWidth(80);
		name.setMaxWidth(80);
//		percentage.setId("percentage"+originalSpecies.id);
		percentage.setPrefHeight(12);
		percentage.setPrefWidth(202);
		
		HBox speciesHbox = new HBox();
		speciesHbox.setId("species" + originalSpecies.id);
		speciesHbox.getChildren().add(name);
		speciesHbox.getChildren().add(percentage);
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {addSpeciesToOverviewSynch(speciesHbox);}
		});
	}
	public void addSpeciesToOverviewSynch(HBox speciesHbox) {
		synchronized (Lock.ACTIVESPECIES_LOCK) {
			overviewSpeciesContainer.getChildren().add(speciesHbox);
			overviewSpeciesContainer.layout();
		}
	}

	
	public void removeSpeciesFromOverview(Species extinctSpecies) {
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {removeSpeciesFromOverviewSynch(extinctSpecies);}
		});
	}
	public void removeSpeciesFromOverviewSynch(Species extinctSpecies) {
		synchronized (Lock.ACTIVESPECIES_LOCK) {
			ObservableList<Node> speciesNodes = overviewSpeciesContainer.getChildren();
			for (int i = 0; i < speciesNodes.size(); i++) {
				if (speciesNodes.get(i).getId().equals("species" + extinctSpecies.id)) {
					speciesNodes.remove(i);
					break;
				}
			}
		}
	}
	
	
	public void updateOverview() {
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {updateOverviewSynch();}
			});
	}
	
	public void updateOverviewSynch() {
		
		synchronized (Lock.ACTIVESPECIES_LOCK) {

			int maxNumberOfCreaturesOfSameSpecies = MapStateSingleton.getInstance().getMaxNumberOfCreaturesOfSameSpecies();

			ObservableList<Node> species = overviewSpeciesContainer.getChildren();
			ArrayList<Species> activeSpecies = MapStateSingleton.getInstance().activeSpecies;
			for (Species s : activeSpecies) {
				for (Node n : species) {
					if (((HBox) n).getId().equals("species" + s.id)) {
						
						((ProgressBar)((HBox) n).getChildren().get(1)).setProgress(
								
								Math.max(
								((double)s.currentMembers)/maxNumberOfCreaturesOfSameSpecies,
								0.05)
								
								);
						break;
					}
				}
			}
		}
		
		
//		ObservableList<Node> percentage = overviewSpeciesPercentage.getChildren();
//		ArrayList<Species> activeSpecies = MapStateSingleton.getInstance().activeSpecies;
//		for (Species s : activeSpecies) {
//			for (Node n : percentage) {
//				if (n.getId().equals("percentage"+s.id)) {
//					((ProgressBar)n).setProgress(((double)s.currentMembers)/maxNumberOfCreaturesOfSameSpecies);
//				}
//			}
//		}
		
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
