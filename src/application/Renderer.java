package application;

import java.awt.Point;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

public class Renderer {
	

    private final WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
    
	private Canvas canvas;
	private MapScrollPane mapScrollPane;
	//private GraphicsContext gc;
	private int[] buffer;
	
	
	int canvasWidth;
	int canvasHeight;
	
	

	private int plantColor = toInt(Color.GREEN);

	private int debugColorA = toInt(Color.PURPLE);
	
	private int backgroundColor = toInt(Color.web("#95E1D3"));
	

	public Renderer(Canvas canvas, MapScrollPane mapScrollPane, int canvasWidth, int canvasHeight) {
		
		this.canvas = canvas;
		this.mapScrollPane = mapScrollPane;

		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		
		
		setCanvas();
		
	}


	
	
	private void setCanvas() {
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
	    
	    buffer = new int[canvasWidth * canvasHeight];
	    
	    clearScreen();
	}
	
	
	public void clearScreen() {
		clearBuffer();

		GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter p = gc.getPixelWriter();
        p.setPixels(0, 0, canvasWidth, canvasHeight, pixelFormat, buffer, 0, canvasWidth);
	}

	public void clearBuffer() {
	    for (int i = 0; i < canvasWidth * canvasHeight; i++) {
        	buffer[i] = backgroundColor;
	    }
	}
	
	
	
	public void render(ArrayList<Creature> creatures, Point[][] flowMap,
			ArrayList<Integer> oldCreaturePositionsX, ArrayList<Integer> oldCreaturePositionsY) {
		
		int creatureSize = 2;
		
//		clearBuffer();
				
		
		
  
		
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
		
		
//		for (FlowGenerator g : flowGenerators) {
//
//			for (Flow f : g.currentFlow) {
//				
//
//	            for (int dx = 0; dx < creatureSize; dx++) {
//	                for (int dy = 0 ; dy < creatureSize; dy++) {
//	                	
////	                	if (f.x > 0) {
//
//		                	buffer[f.x*creatureSize + dx + canvasWidth * (f.y*creatureSize + dy)] = 
//		                			
//		                			//toInt(new Color((100+f.valX)/200, (100+f.valY)/200, 0.5, 1))
//		                			
//		                			toInt(new Color((100+f.valX)/200, 0.5, 0.5, 1))
//		                			;
//		                	
////	                	} else if (f.x < 0) {
////
////		                	buffer[f.x*creatureSize + dx + canvasWidth * (f.y*creatureSize + dy)] = debugColorB;
////	                	}
//	    			}
//				}
//			}
//		}
		
		
		for (Creature c : creatures) {

            for (int dx = 0; dx < creatureSize; dx++) {
                for (int dy = 0 ; dy < creatureSize; dy++) {
                	
                	buffer[c.x*creatureSize + dx + canvasWidth * (c.y*creatureSize + dy)] = plantColor;
    			}
			}
		}
		

		


		GraphicsContext gc = canvas.getGraphicsContext2D();
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
		mapScrollPane.zoomIn();
	}
	

	public void zoomOut() {
		mapScrollPane.zoomOut();
	}
	
	
	
	
    private int toInt(Color c) {
        return
                (                      255  << 24) |
                ((int) (c.getRed()   * 255) << 16) |
                ((int) (c.getGreen() * 255) << 8)  |
                ((int) (c.getBlue()  * 255));
    }
    
	
}
