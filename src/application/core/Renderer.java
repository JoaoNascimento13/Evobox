package application.core;

import java.nio.IntBuffer;
import java.util.ArrayList;

import application.dynamic.creatures.Creature;
import application.gui.MapScrollPane;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class Renderer {
	

    private final WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
    
//	private Canvas canvasA;
//	private Canvas canvasB;
	

	private Canvas activeCanvas;
	
	private MapScrollPane activeMapScrollPane;
	
	//private GraphicsContext gc;
	private int[] buffer;
	
	
	int canvasWidth;
	int canvasHeight;
	

	private int plantColor = toInt(Color.GREEN);
	private int focusColor = toInt(Color.ORANGE);

	private int debugColorA = toInt(Color.PURPLE);
	
	private int numberOfCanvasSwapsDelayed;
	

	private int backgroundColor = toInt(Color.web("#c4eeec"));
	
//	private int backgroundColor = toInt(Color.web("#BBFAF7"));
//	private int backgroundColor = toInt(Color.web("#95E1D3"));
	
	
	public Renderer(Canvas canvasA, Canvas canvasB, MapScrollPane mapScrollPane, int canvasWidth, int canvasHeight) {
		
		
//		this.canvasA = canvasA;
//		this.canvasB = canvasB;
		
		this.activeCanvas = canvasA;
		
		this.activeMapScrollPane = mapScrollPane;

		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		
		//ctiveCanvas
		
		this.numberOfCanvasSwapsDelayed = 0;
		
		setCanvas();

		
	}


	
	
	private void setCanvas() {
	    buffer = new int[canvasWidth * canvasHeight];
	    clearScreen();
	}
	
	
	
	
	public void clearScreen() {
		
		GraphicsContext gc = activeCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, activeCanvas.getWidth(), activeCanvas.getHeight());
		
		
		clearBuffer();

        PixelWriter p = gc.getPixelWriter();
        p.setPixels(0, 0, canvasWidth, canvasHeight, pixelFormat, buffer, 0, canvasWidth);
        
	}

	
	
	
	public void clearBuffer() {
	    for (int i = 0; i < canvasWidth * canvasHeight; i++) {
        	buffer[i] = backgroundColor;
	    }
	}
	
	

	public void changeVisibleMapScrollPane() {
		
		if (activeMapScrollPane.mouseBeingPressed) {
			
			if (numberOfCanvasSwapsDelayed > 10) {
				
				Event.fireEvent(activeMapScrollPane, new MouseEvent(MouseEvent.MOUSE_RELEASED, 0,
		                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
		                true, true, true, true, true, true, null));
				
			} else {

				numberOfCanvasSwapsDelayed++;
				return;
			}
			
		}
		
		numberOfCanvasSwapsDelayed = 0;
		
		GraphicsContext gc = ((Canvas) activeMapScrollPane.backup.getTarget()).getGraphicsContext2D();

		
        PixelWriter p = gc.getPixelWriter();
        p.setPixels(0, 0, canvasWidth, canvasHeight, pixelFormat, buffer, 0, canvasWidth);
		
        
		
		GridPane container = ((GridPane)activeMapScrollPane.getParent());
		

		activeMapScrollPane.backup.setHvalue(activeMapScrollPane.getHvalue());
		activeMapScrollPane.backup.setVvalue(activeMapScrollPane.getVvalue());
		
		
//        Pane container = ((Pane)activeMapScrollPane.getParent());
        
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {

		    	changeVisibleMapScrollPaneSynch(container);
		    }
		});
		
	}

	
	public void changeVisibleMapScrollPaneSynch(GridPane container) {

		synchronized(Lock.ACTIVESPECIES_LOCK) {

			container.getChildren().remove(activeMapScrollPane);
			
			container.getChildren().add(activeMapScrollPane.backup);
			
				
//			root.getChildren().remove(activeMapScrollPane);
//			
//			root.add(activeMapScrollPane.backup, 1, 1);
			
			container.layout();
			
			activeMapScrollPane = activeMapScrollPane.backup;

			activeCanvas = (Canvas) activeMapScrollPane.getTarget();
			

			((Canvas) activeMapScrollPane.backup.getTarget()).getGraphicsContext2D().
				clearRect(0, 0, activeCanvas.getWidth(), activeCanvas.getHeight());
		}
		
	}
	
	
	public void render() {

		synchronized(Lock.ACTIVESPECIES_LOCK) {

			int creaturePixels = 2;

			MapStateSingleton mapState = MapStateSingleton.getInstance();
			OutdatedPositionsSingleton outdatedPositions = OutdatedPositionsSingleton.getInstance();
			ArrayList<Integer> oldCreaturePositionsX = outdatedPositions.getOutdatedCreaturesX();
			ArrayList<Integer> oldCreaturePositionsY = outdatedPositions.getOutdatedCreaturesY();
			
			
			for (int i = 0; i < oldCreaturePositionsX.size(); i++) {
				int x = oldCreaturePositionsX.get(i);
				int y = oldCreaturePositionsY.get(i);
				
	            for (int dx = 0; dx < creaturePixels; dx++) {
	                for (int dy = 0 ; dy < creaturePixels; dy++) {
	                	
	                	buffer[x*creaturePixels + dx + canvasWidth * (y*creaturePixels + dy)] = backgroundColor;
	    			}
				}
			}
			
			int creatureColor;
			for (Creature c : mapState.activeCreatures) {
	            for (int dx = 0; dx < creaturePixels; dx++) {
	                for (int dy = 0 ; dy < creaturePixels; dy++) {
	                	buffer[c.x*creaturePixels + dx + canvasWidth * (c.y*creaturePixels + dy)] = c.species.getColor();
	    			}
				}
			}

			if (mapState.focusedCreature != null) {
		        for (int dx = 0; dx < creaturePixels; dx++) {
		            for (int dy = 0 ; dy < creaturePixels; dy++) {
		            	
		            	buffer[mapState.focusedCreature.x*creaturePixels + dx + canvasWidth * (mapState.focusedCreature.y*creaturePixels + dy)] = focusColor;
					}
				}
			}

			GraphicsContext gc = activeCanvas.getGraphicsContext2D();

			
	        PixelWriter p = gc.getPixelWriter();
	        
	        try {
				p.setPixels(0, 0, canvasWidth, canvasHeight, pixelFormat, buffer, 0, canvasWidth);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        
//        try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
        
	}
	

//	for (int i = 0; i < flowMap.length; i++) {
//		for (int j = 0; j < flowMap[0].length; j++) {
//			
//			
//			if (
//				Math.abs(flowMap[i][j].x) 
//				+
//				Math.abs(flowMap[i][j].y) 
//				
//				> 0) {
//
//	            for (int dx = 0; dx < creatureSize; dx++) {
//	                for (int dy = 0 ; dy < creatureSize; dy++) {
//	                	
//	                	buffer[i*creatureSize + dx + canvasWidth * (j*creatureSize + dy)] = 
//
//	                			debugColorA
//	                			
//	                			//toInt(new Color((flowMap[i][j].y)/100, 0.2, 0.8, 1))
//	                			
//	                			
//	                			
//	                			//toInt(new Color((100+flowMap[i][j].x)/200, (100+flowMap[i][j].y)/200, 0.5, 1))
//	                			;
//	                }
//	            }
//			}
//		}
//	}
	
	
	public void zoomIn() {
		activeMapScrollPane.zoomIn();
	}
	

	public void zoomOut() {
		activeMapScrollPane.zoomOut();
	}
	
	
	
	
    private int toInt(Color c) {
        return
                (                      255  << 24) |
                ((int) (c.getRed()   * 255) << 16) |
                ((int) (c.getGreen() * 255) << 8)  |
                ((int) (c.getBlue()  * 255));
    }
    
	
}
