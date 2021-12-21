package application.gui;

import java.util.ArrayList;
import java.util.Random;

import application.core.Lock;
import application.core.MapStateSingleton;
import application.core.OutdatedPositionsSingleton;
import application.core.Renderer;
import application.core.Simulator;
import application.dynamic.creatures.Creature;
import application.dynamic.creatures.Species;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseButton;
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
	private Label turnLabel;
	@FXML
	private VBox overviewSpeciesContainer;

	@FXML
	private CheckBox speciesHighlight;
	@FXML
	private Button speciesViewRandom;
	
	
	@FXML
	private Label speciesName;
	@FXML
	private Label speciesDescription;
	
	@FXML
	private Label speciesMembersAlive;
	@FXML
	private Label speciesMembersTotal;
	
	@FXML
	private Label speciesDiet;
	@FXML
	private ProgressBar speciesSize;
	@FXML
	private ProgressBar speciesLifespan;
	@FXML
	private ProgressBar speciesSpeed;
	@FXML
	private ProgressBar speciesToughness;
	@FXML
	private ProgressBar speciesAttack;
	@FXML
	private ProgressBar speciesDefense;
	@FXML
	private ProgressBar speciesPerception;
	@FXML
	private ProgressBar speciesStealth;
	@FXML
	private ProgressBar speciesFertility;
	@FXML
	private ProgressBar speciesClutchSize;
	@FXML
	private ProgressBar speciesAggression;
	@FXML
	private ProgressBar speciesReactiveness;

	@FXML
	private Label speciesDietLabel;
	@FXML
	private Label speciesSizeLabel;
	@FXML
	private Label speciesLifespanLabel;
	@FXML
	private Label speciesSpeedLabel;
	@FXML
	private Label speciesToughnessLabel;
	@FXML
	private Label speciesAttackLabel;
	@FXML
	private Label speciesDefenseLabel;
	@FXML
	private Label speciesPerceptionLabel;
	@FXML
	private Label speciesStealthLabel;
	@FXML
	private Label speciesFertilityLabel;
	@FXML
	private Label speciesClutchSizeLabel;
	@FXML
	private Label speciesAggressionLabel;
	@FXML
	private Label speciesReactivenessLabel;
	
	@FXML
	private ProgressBar speciesEvolution;
	@FXML
	private Label speciesEvolutionLabel;
	
	

	@FXML
	private Label creatureSpecies;
	@FXML
	private Label creatureSpeciesNumber;

	@FXML
	private Label creatureGoal;
	@FXML
	private ProgressBar healthBar;
	@FXML
	private ProgressBar foodBar;
	@FXML
	private ProgressBar ageBar;
	

	@FXML
	private CheckBox creatureTrack;
	@FXML
	private Button creatureLocate;
	
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
		renderer.activeMapScrollPane.zoomIn();;
	}
	
	public void zoomOut() {
		renderer.activeMapScrollPane.zoomOut();
	}
	
	
	public void viewRandomMemberOfSpecies() {
		
		synchronized(Lock.MAINLOCK) {

			ArrayList<Creature> creatures = MapStateSingleton.getInstance().activeCreatures;
			int creaturesSize = creatures.size();
			int startFrom = new Random().nextInt(creaturesSize);
			int i = startFrom;
			long targetSpeciesId = MapStateSingleton.getInstance().getFocusedSpecies().id;
			Creature c;
			Creature chosenCreature = null;
			while (true) {
				c = creatures.get(i);
				if (c.species.id == targetSpeciesId && c.isActive()) {
					chosenCreature = c;
					break;
				}
				i++;
				if (i == creaturesSize) {
					i = 0;
				}
				if (i == startFrom) {
					break;
				}
			}
			if (chosenCreature != null) {
		    	MapStateSingleton.getInstance().setFocusedCreature(chosenCreature);
				if (speciesHighlight.isSelected()) {
					toggleSpeciesHighlight();
				}
				locateCreature();
				selectAndViewCreature(chosenCreature);
			}
		}
		
	}
	
	public void toggleSpeciesHighlight() {
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		speciesHighlight.setSelected(mapState.toggleSpeciesHighlight());
		if (mapState.getFocusedCreature() != null && mapState.getFocusedCreature().species.id != mapState.getFocusedSpecies().id) {
			mapState.clearFocusedCreature();
//			OutdatedPositionsSingleton.getInstance().addCreaturePosition(focusedCreature.x, focusedCreature.y);
		}
		renderer.render();
	}
	

	public void toggleCreatureTracking() {
		creatureTrack.setSelected(MapStateSingleton.getInstance().toggleCreatureTracking());
		if (creatureTrack.isSelected()) {
			Creature c = MapStateSingleton.getInstance().getFocusedCreature();
			try {
				SceneManagerSingleton.getInstance().renderer.activeMapScrollPane.centerIn(c.x, c.y);
			} catch (NullPointerException e) {
			}
		}
		
	}

	public void locateCreature() {
		Creature c = MapStateSingleton.getInstance().getFocusedCreature();
		for (int i = 0; i < 13; i++) {
			renderer.activeMapScrollPane.zoomIn();
		}
		renderer.activeMapScrollPane.centerIn(c.x, c.y);
	}
	
	
	public void updateSidePane() {

		synchronized(Lock.MAINLOCK) {
			

			MapStateSingleton mapState = MapStateSingleton.getInstance();
			
			boolean forceGeneralView = false;
			boolean forceSpeciesView = false;
			if (mapState.getFocusedCreature() == null && inCreatureView()) {
				
				if (mapState.getFocusedSpecies() == null) {
					forceGeneralView = true;
					showGeneralView();
				} else {
					forceSpeciesView = true;
					showSpeciesView();
				}
			} else if (mapState.getFocusedSpecies() == null && inSpeciesView()) {
				forceGeneralView = true;
				showGeneralView();
			}
			
			
			if (inGeneralView() || forceGeneralView) {
				updateOverview();
				
			} else if (inSpeciesView() || forceSpeciesView) {
				fillSpeciesDynamicDetails(mapState.getFocusedSpecies());
				
			} else {
				
				boolean focusIsDead = false;
				for (long i : mapState.deadCreaturesToRemoveIds) {
					if (mapState.getFocusedCreature().id == i) {
						focusIsDead = true;
						break;
					}
				}
				if (!focusIsDead) {
					if (mapState.refreshFocusedCreature) {
						selectAndViewCreature(mapState.getFocusedCreature());
						mapState.refreshFocusedCreature = false;
					} else {
						fillDynamicCreatureDetails(mapState.getFocusedCreature());
					}
				} else {
					mapState.setFocusedSpecies(mapState.getFocusedCreature().species);
					mapState.clearFocusedCreature();;
					fillSpeciesPane();
					showSpeciesView();
				}
			}
			
			generalView.getParent().layout();
		}
	}
	
	
	public void selectAndViewCreature(Creature creature) {
    	
    	fillStaticCreatureDetails(creature);
    	fillDynamicCreatureDetails(creature);
    	updateCreatureTrackingStatus(creature);
    	showCreatureView();

    	MapStateSingleton mapState = MapStateSingleton.getInstance();
    	mapState.disableSpeciesHighlight();
    	mapState.setFocusedSpecies(null);
    	mapState.setFocusedCreature(creature);
    	
    	simulator.render();
	}
	
	public void updateCreatureTrackingStatus(Creature creature) {
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		if (mapState.getCreatureTracking() && mapState.getFocusedCreatureId() != creature.id) {
			mapState.toggleCreatureTracking();
			creatureTrack.setSelected(false);
		}
	}

	public void fillStaticCreatureDetails(Creature creature) {

		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	
		    	fillCreatureGenomeDetails(creature);
		    	
		    	fillCreatureGenomeLabelDetails(creature);
		    }
		});
	}
	

	public void fillCreatureGenomeDetails(Creature creature) {

		creatureSpecies.setText(creature.species.name);
		creatureSpeciesNumber.setText(String.valueOf(creature.numberInSpecies));
		
		creatureDiet.setText(creature.genome.diet.name);

		creatureSize.setProgress(((double)creature.genome.getSize())/10);
		creatureLifespan.setProgress(((double)creature.genome.getAgeExpectancy())/10);
		creatureSpeed.setProgress(((double)creature.genome.getSpeed())/10);
		creatureToughness.setProgress(((double)creature.genome.getToughness())/10);
		creatureAttack.setProgress(((double)creature.genome.getAttack())/10);
		creatureDefense.setProgress(((double)creature.genome.getDefense())/10);
		creaturePerception.setProgress(((double)creature.genome.getPerception())/10);
		creatureStealth.setProgress(((double)creature.genome.getStealth())/10);
		creatureFertility.setProgress(((double)creature.genome.getFertility())/10);
		creatureClutchSize.setProgress(((double)creature.genome.getClutchSize())/10);
		creatureAggression.setProgress(((double)creature.genome.getAggression())/10);
		creatureReactiveness.setProgress(((double)creature.genome.getReactiveness())/10);
		creatureEvolution.setProgress(((double)creature.genome.getUsedEvoPoints())/creature.genome.getMaxEvoPoints());
	}

	public void fillCreatureGenomeLabelDetails(Creature creature) {
		
		if (creature.genome.diet == creature.species.baseGenome.diet) {
			creatureDietLabel.setTextFill(Color.BLACK);
		} else {
			creatureDietLabel.setTextFill(Color.RED);
		}
		changeGenomeLabelColor(creatureSizeLabel, creature.genome.getSize(), creature.species.baseGenome.getSize());
		changeGenomeLabelColor(creatureLifespanLabel, creature.genome.getAgeExpectancy(), creature.species.baseGenome.getAgeExpectancy());
		changeGenomeLabelColor(creatureSpeedLabel, creature.genome.getSpeed(), creature.species.baseGenome.getSpeed());
		changeGenomeLabelColor(creatureToughnessLabel, creature.genome.getToughness(), creature.species.baseGenome.getToughness());
		changeGenomeLabelColor(creatureAttackLabel, creature.genome.getAttack(), creature.species.baseGenome.getAttack());
		changeGenomeLabelColor(creatureDefenseLabel, creature.genome.getDefense(), creature.species.baseGenome.getDefense());
		changeGenomeLabelColor(creaturePerceptionLabel, creature.genome.getPerception(), creature.species.baseGenome.getPerception());
		changeGenomeLabelColor(creatureStealthLabel, creature.genome.getStealth(), creature.species.baseGenome.getStealth());
		changeGenomeLabelColor(creatureFertilityLabel, creature.genome.getFertility(), creature.species.baseGenome.getFertility());
		changeGenomeLabelColor(creatureClutchSizeLabel, creature.genome.getClutchSize(), creature.species.baseGenome.getClutchSize());
		changeGenomeLabelColor(creatureAggressionLabel, creature.genome.getAggression(), creature.species.baseGenome.getAggression());
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
		    		try {
						creatureGoal.setText(creature.goal.description);
					} catch (Exception e) {
						creatureGoal.setText(creature.movementDecisionStrategy.getStartingGoal().description);
					}
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
	
	

	public void fillSpeciesPane() {
		Species species = MapStateSingleton.getInstance().getFocusedSpecies();
		fillSpeciesStaticDetails(species);
		fillSpeciesDynamicDetails(species);
		
    	showSpeciesView();
    	
//    	MapStateSingleton.getInstance().setFocusedSpecies(species);
    	
//    	simulator.render();
	}

	public void fillSpeciesDynamicDetails(Species species) {
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
				speciesMembersAlive.setText(String.valueOf(species.currentMembers));
				speciesMembersTotal.setText(String.valueOf(species.totalMembers));
		    }
		});
	}

	public void fillSpeciesStaticDetails(Species species) {

		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {

				speciesName.setText(species.name);
				speciesDescription.setText(species.baseGenome.getSizeDescription() + species.baseGenome.diet.name.toLowerCase() + " species"); 
				
				speciesDiet.setText(species.baseGenome.diet.name);

				speciesSize.setProgress(((double)species.baseGenome.getSize())/10);
				speciesLifespan.setProgress(((double)species.baseGenome.getAgeExpectancy())/10);
				speciesSpeed.setProgress(((double)species.baseGenome.getSpeed())/10);
				speciesToughness.setProgress(((double)species.baseGenome.getToughness())/10);
				speciesAttack.setProgress(((double)species.baseGenome.getAttack())/10);
				speciesDefense.setProgress(((double)species.baseGenome.getDefense())/10);
				speciesPerception.setProgress(((double)species.baseGenome.getPerception())/10);
				speciesStealth.setProgress(((double)species.baseGenome.getStealth())/10);
				speciesFertility.setProgress(((double)species.baseGenome.getFertility())/10);
				speciesClutchSize.setProgress(((double)species.baseGenome.getClutchSize())/10);
				speciesAggression.setProgress(((double)species.baseGenome.getAggression())/10);
				speciesReactiveness.setProgress(((double)species.baseGenome.getReactiveness())/10);
				speciesEvolution.setProgress(((double)species.baseGenome.getUsedEvoPoints())/species.baseGenome.getMaxEvoPoints());
		    }
		});
		
	}
	
	
	
	

	public void addSpeciesToOverview(Species originalSpecies) {

		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {addSpeciesToOverviewSynch(originalSpecies);}
		});
	}
	
	

	
	public void viewSpeciesFromOverview(Species originalSpecies) {

		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {

		    	updateSpeciesHighlight(originalSpecies);
		        fillSpeciesPane();
		    }
		});
	}
	
	
	private void updateSpeciesHighlight(Species species) {

		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		if (speciesHighlight.isSelected() && 
			mapState.getFocusedSpecies() != null && 
			mapState.getFocusedSpecies().id != species.id
			) {
			
			speciesHighlight.setSelected(mapState.toggleSpeciesHighlight());
			if (mapState.getFocusedCreature() != null && mapState.getFocusedCreature() .species.id != species.id) {
				mapState.clearFocusedCreature();
			}
			renderer.render();
		}
        MapStateSingleton.getInstance().setFocusedSpecies(species);
	}
	
	
	private void updateSpeciesHighlight() {

		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		if (speciesHighlight.isSelected() && 
			mapState.getFocusedSpecies() != null) {
			
			speciesHighlight.setSelected(mapState.highlightSpecies);
			if (mapState.getFocusedCreature() != null && 
				mapState.getFocusedCreature().species.id != mapState.getFocusedSpecies().id) {
				mapState.clearFocusedCreature();
			}
			renderer.render();
		}
	}
	
	
	public void addSpeciesToOverviewSynch(Species originalSpecies) {
		
		synchronized (Lock.MAINLOCK) {

			Label name = new Label(originalSpecies.name);
			int maxNumberOfCreaturesOfSameSpecies = MapStateSingleton.getInstance().getMaxNumberOfCreaturesOfSameSpecies();
			ProgressBar percentage = new ProgressBar(((double)originalSpecies.currentMembers)/maxNumberOfCreaturesOfSameSpecies);
//			name.setId("name"+originalSpecies.id);
			name.setPrefHeight(12);
			name.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
			name.setMinWidth(80);
			name.setPrefWidth(80);
			name.setMaxWidth(80);
//			percentage.setId("percentage"+originalSpecies.id);
			percentage.setPrefHeight(12);
			percentage.setPrefWidth(210);
			
			percentage.setStyle(originalSpecies.baseGenome.diet.getBarColorString());
			
			
			
			HBox speciesHbox = new HBox();
			speciesHbox.setId("species" + originalSpecies.id);
			speciesHbox.getChildren().add(name);
			speciesHbox.getChildren().add(percentage);
			
			name.setOnMouseClicked(e -> {
				if (e.getButton() == MouseButton.PRIMARY) {
		            e.consume();
		            viewSpeciesFromOverview(originalSpecies);
//		            System.out.println("Clicked on species: " + originalSpecies.name);
//		            MapStateSingleton.getInstance().setFocusedSpecies(originalSpecies);
//		            fillSpeciesPane();
				}
	        });
			percentage.setOnMouseClicked(e -> {
				if (e.getButton() == MouseButton.PRIMARY) {
		            e.consume();
		            viewSpeciesFromOverview(originalSpecies);
				}
	        });


			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
					overviewSpeciesContainer.getChildren().add(speciesHbox);
					overviewSpeciesContainer.layout();
			    }
			});
		}
	}

	
	public void removeSpeciesFromOverview(Species extinctSpecies) {
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {removeSpeciesFromOverviewSynch(extinctSpecies);}
		});
	}
	public void removeSpeciesFromOverviewSynch(Species extinctSpecies) {
		synchronized (Lock.MAINLOCK) {
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
		
		synchronized (Lock.MAINLOCK) {

			int maxNumberOfCreaturesOfSameSpecies = MapStateSingleton.getInstance().getMaxNumberOfCreaturesOfSameSpecies();

			turnLabel.setText(String.valueOf(MapStateSingleton.getInstance().turn));

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
	}
	
	
	
	public void setGeneralMouseListeners() {

		speciesView.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				synchronized(Lock.MAINLOCK) {
		            e.consume();
		            updateOverview();
		            showGeneralView();	
				}		
			}
		});

		creatureView.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {

				synchronized(Lock.MAINLOCK) {
		            e.consume();
		            MapStateSingleton.getInstance().setFocusedSpecies(MapStateSingleton.getInstance().getFocusedCreature().species);
		        	fillSpeciesPane();
		        	showSpeciesView();	
				}		
			}
		});
	}
	
	
	public void disableHighlightOnNewSpeciesView() {
		MapStateSingleton mapState = MapStateSingleton.getInstance();
		if (mapState.getSpeciesHighlight() && 
			mapState.getFocusedSpecies() != mapState.getFocusedCreature().species) {
			
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {mapState.toggleSpeciesHighlight();}
				});
			
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

		    	updateSpeciesHighlight();
		    	
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
	}
	public void removeMapScrollPane() {
		this.mapContainer.getChildren().clear();
	}
	
}
