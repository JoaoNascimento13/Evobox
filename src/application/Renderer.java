package application;

import java.nio.IntBuffer;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

public class Renderer {
	

    private final WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
    
	private Canvas canvas;
	private GraphicsContext gc;
	private int[] buffer;
	
	
	int canvasWidth;
	int canvasHeight;

	public Renderer(Canvas canvas, int canvasWidth, int canvasHeight) {
		
		this.canvas = canvas;

		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		
		setCanvas();
	}


	
	
	private void setCanvas() {
		
	    gc = canvas.getGraphicsContext2D();
	    
	    buffer = new int[canvasWidth * canvasHeight];
		
	}
	

	public void render(ArrayList<Creature> creatures, 
			ArrayList<Integer> oldCreaturePositionsX, ArrayList<Integer> oldCreaturePositionsY) {
		
		int creatureSize = 2;
		
		//Color background = new Color().
				
				
				//new Color(#95E1D3);
		
		int plantColor = toInt(Color.GREEN);
		 
		int backgroundColor = toInt(Color.web("#95E1D3"));
				
				
//		String backgroundHex = "#95E1D3";
//		
//		Color background = Color.rgb(
//              Integer.valueOf(backgroundHex.substring( 1, 3 ), 16 ),
//              Integer.valueOf(backgroundHex.substring( 3, 5 ), 16 ),
//              Integer.valueOf(backgroundHex.substring( 5, 7 ), 16 ));
  
		
		for (int i = 0; i < oldCreaturePositionsX.size(); i++) {
			int x = oldCreaturePositionsX.get(i);
			int y = oldCreaturePositionsY.get(i);
			
            for (int dx = 0; dx < creatureSize; dx++) {
                for (int dy = 0 ; dy < creatureSize; dy++) {
                	
                	buffer[x*creatureSize + dx + canvasWidth * (y*creatureSize + dy)] = backgroundColor;
    			}
			}
		}
		
		for (Creature c : creatures) {

            for (int dx = 0; dx < creatureSize; dx++) {
                for (int dy = 0 ; dy < creatureSize; dy++) {
                	
                	buffer[c.x*creatureSize + dx + canvasWidth * (c.y*creatureSize + dy)] = plantColor;
    			}
			}
		}
		
		
//        for (int i = 0; i < canvasWidth; i += creatureSize) {
//            for (int j = 0; j < canvasHeight; j += creatureSize) {
//            	
//                for (int dx = 0; dx < creatureSize; dx++) {
//                    for (int dy = 0 ; dy < creatureSize; dy++) {
//                    	
//                        buffer[i + dx + canvasWidth * (j + dy)] = plantColor;
//                        
//                    }
//                }
//            }
//        }
		

        PixelWriter p = gc.getPixelWriter();
        p.setPixels(0, 0, canvasWidth, canvasHeight, pixelFormat, buffer, 0, canvasWidth);
        
//        try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
//        System.out.println("Rendered");
        
	}
	
	
    private int toInt(Color c) {
        return
                (                      255  << 24) |
                ((int) (c.getRed()   * 255) << 16) |
                ((int) (c.getGreen() * 255) << 8)  |
                ((int) (c.getBlue()  * 255));
    }
    
//    private Color hex2Rgb(String colorStr) {
//    	return new Color(0, 0, 0);
////        return new 
	
}
