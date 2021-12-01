package application.core;

import java.nio.IntBuffer;
import java.util.ArrayList;

import application.dynamic.Creature;
import application.gui.MapScrollPane;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
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

	private int debugColorA = toInt(Color.PURPLE);
	
	private int backgroundColor = toInt(Color.web("#95E1D3"));
	
	
	public Renderer(Canvas canvasA, Canvas canvasB, MapScrollPane mapScrollPane, int canvasWidth, int canvasHeight) {
		
		
//		this.canvasA = canvasA;
//		this.canvasB = canvasB;
		
		this.activeCanvas = canvasA;
		
		this.activeMapScrollPane = mapScrollPane;

		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		
		//ctiveCanvas
		
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
		
		
		GraphicsContext gc = ((Canvas) activeMapScrollPane.backup.getTarget()).getGraphicsContext2D();

		
        PixelWriter p = gc.getPixelWriter();
        p.setPixels(0, 0, canvasWidth, canvasHeight, pixelFormat, buffer, 0, canvasWidth);
		
		
		GridPane root = ((GridPane)activeMapScrollPane.getParent());
		
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {

				root.getChildren().remove(activeMapScrollPane);
				
				root.add(activeMapScrollPane.backup, 1, 1);
				
				root.layout();
				
				activeMapScrollPane = activeMapScrollPane.backup;

				activeCanvas = (Canvas) activeMapScrollPane.getTarget();
				

				((Canvas) activeMapScrollPane.backup.getTarget()).getGraphicsContext2D().
					clearRect(0, 0, activeCanvas.getWidth(), activeCanvas.getHeight());
		    }
		});
		
	}
	
	
	public void render() {
		
		int creatureSize = 2;

		MapStateSingleton mapState = MapStateSingleton.getInstance();
		
		OutdatedPositionsSingleton outdatedPositions = OutdatedPositionsSingleton.getInstance();
		ArrayList<Integer> oldCreaturePositionsX = outdatedPositions.getOutdatedCreaturesX();
		ArrayList<Integer> oldCreaturePositionsY = outdatedPositions.getOutdatedCreaturesY();
		
		
		for (int i = 0; i < oldCreaturePositionsX.size(); i++) {
			int x = oldCreaturePositionsX.get(i);
			int y = oldCreaturePositionsY.get(i);
			
            for (int dx = 0; dx < creatureSize; dx++) {
                for (int dy = 0 ; dy < creatureSize; dy++) {
                	
                	buffer[x*creatureSize + dx + canvasWidth * (y*creatureSize + dy)] = backgroundColor;
    			}
			}
		}
		
		
		
		
		
//		for (int i = 0; i < flowMap.length; i++) {
//			for (int j = 0; j < flowMap[0].length; j++) {
//				
//				
//				if (
//					Math.abs(flowMap[i][j].x) 
//					+
//					Math.abs(flowMap[i][j].y) 
//					
//					> 0) {
//
//		            for (int dx = 0; dx < creatureSize; dx++) {
//		                for (int dy = 0 ; dy < creatureSize; dy++) {
//		                	
//		                	buffer[i*creatureSize + dx + canvasWidth * (j*creatureSize + dy)] = 
//
//		                			debugColorA
//		                			
//		                			//toInt(new Color((flowMap[i][j].y)/100, 0.2, 0.8, 1))
//		                			
//		                			
//		                			
//		                			//toInt(new Color((100+flowMap[i][j].x)/200, (100+flowMap[i][j].y)/200, 0.5, 1))
//		                			;
//		                }
//		            }
//				}
//				
//	            
//			}
//		}
		

//		for (Creature c : mapState.creatures) {
//			for (Creature d : mapState.creatures) {
//				if (c.x == d.x && c.y == d.y && !(c == d)) {
//					System.out.println("ERROR");
//					System.out.println("c.id: " + c.id);
//					System.out.println("d.id: " + d.id);
//					System.out.println("coords: " + c.x + ", " + c.y);
//					int crash = 1/0;
//				}
//			}
//		}
		
		
		for (Creature c : mapState.creatures) {

            for (int dx = 0; dx < creatureSize; dx++) {
                for (int dy = 0 ; dy < creatureSize; dy++) {
                	
                	buffer[c.x*creatureSize + dx + canvasWidth * (c.y*creatureSize + dy)] = plantColor;
    			}
			}
		}
		

		


		GraphicsContext gc = activeCanvas.getGraphicsContext2D();

		
        PixelWriter p = gc.getPixelWriter();
        p.setPixels(0, 0, canvasWidth, canvasHeight, pixelFormat, buffer, 0, canvasWidth);
        
        
//        try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
	}
	
	
	
	
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
