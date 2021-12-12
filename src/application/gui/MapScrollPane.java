package application.gui;

import application.core.Direction;
import application.core.Lock;
import application.core.MapStateSingleton;
import application.dynamic.creatures.Creature;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MapScrollPane extends ScrollPane {
	
    private double scaleValue = 1;
    private double maxScale = 10;
    
    private double wheelZoomIntensity = 0.1;
    private double buttonZoomIntensity = 2;
    
    private int viewAreaCenterX = 320;
    private int viewAreaCenterY = 320;
    
    private Node target;
    private Node zoomNode;

    private SimulatorController simulatorController;
    
    public boolean mouseBeingPressed;
    
    
    public MapScrollPane backup;
    
    public MapScrollPane(Node target, SimulatorController simulatorController) {
        super();
        this.target = target;
        this.simulatorController = simulatorController;
        
        this.mouseBeingPressed = false;
        
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));

        setPannable(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        setFitToHeight(true);
        setFitToWidth(true);

        updateScale();
    }
    

	public Node getTarget () {
		return target;
	}
    
    
    
    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getTextDeltaY(), new Point2D(e.getX(), e.getY()));
        });

        outerNode.setOnMouseClicked(e -> {
            e.consume();
            onClick(new Point2D(e.getX(), e.getY()));
        });
        outerNode.setOnMousePressed(e -> {
            onMousePressed();
        });
        outerNode.setOnMouseReleased(e -> {
            onMouseReleased();
        });
        return outerNode;
    }
    
    
    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private void updateScale() {
        target.setScaleX(scaleValue);
        target.setScaleY(scaleValue);
    }

    

    public void zoomToCenterOfViewArea(double intensity) {

        Bounds innerBounds = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();
        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());
    	
    	onScroll(intensity, new Point2D(valX + viewAreaCenterX, valY + viewAreaCenterY));
    }
    
    public void zoomIn() {
    	zoomToCenterOfViewArea(buttonZoomIntensity);
    }

    public void zoomOut() {
    	zoomToCenterOfViewArea(-1*buttonZoomIntensity);
    }


	public void moveInDir(Direction movementDir) {

		int tileSize = 2;

        Bounds innerBounds = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();
        
		this.setHvalue(this.getHvalue() + ((double)movementDir.x*tileSize*scaleValue)/
											(innerBounds.getWidth() - viewportBounds.getWidth()));
		
		this.setVvalue(this.getVvalue() + ((double)movementDir.y*tileSize*scaleValue)/
											(innerBounds.getHeight() - viewportBounds.getHeight()));
		
		backup.setHvalue(this.getHvalue());
		backup.setVvalue(this.getVvalue());
	}


	public void centerIn(int x, int y) {

		synchronized(Lock.MAINLOCK) {

			int tileSize = 2;
			
	        Bounds innerBounds = zoomNode.getLayoutBounds();
	        Bounds viewportBounds = getViewportBounds();
	        
	        double screenStartX = this.getHvalue() * ((innerBounds.getWidth() - viewportBounds.getWidth()) / (tileSize * scaleValue));
	        double screenStartY = this.getVvalue() * ((innerBounds.getHeight() - viewportBounds.getHeight()) / (tileSize *scaleValue));
	        
	        double screenCenterX = screenStartX + viewportBounds.getWidth()/(2*tileSize*scaleValue);
	        double screenCenterY = screenStartY + viewportBounds.getHeight()/(2*tileSize*scaleValue);
			
	        
			this.setHvalue(this.getHvalue() + ((double)tileSize*scaleValue*(x-screenCenterX))/
												(innerBounds.getWidth() - viewportBounds.getWidth()));
			
			this.setVvalue(this.getVvalue() + ((double)tileSize*scaleValue*(y-screenCenterY))/
												(innerBounds.getHeight() - viewportBounds.getHeight()));
			
			backup.setHvalue(this.getHvalue());
			backup.setVvalue(this.getVvalue());
		}
	}
	
    private void onMousePressed() {
    	mouseBeingPressed = true;
    	backup.mouseBeingPressed = true;
    }
    
    private void onMouseReleased() {
    	mouseBeingPressed = false;
    	backup.mouseBeingPressed = false;
    }
    
    
    private void onClick(Point2D mousePoint) {
//    	System.out.println("Click on: " + (mousePoint.getX()/(scaleValue))/2 + ", " + (mousePoint.getY()/(scaleValue))/2);
    	
    	Creature creature = MapStateSingleton.getInstance().getCreature(
    			(int)((mousePoint.getX()/(scaleValue))/2), 
    			(int)((mousePoint.getY()/(scaleValue))/2));
    	if (creature != null) {
    		synchronized(Lock.MAINLOCK) {
        		simulatorController.selectAndViewCreature(creature);
    		}
    	}
    }
    
    
    
    private void onScroll(double wheelDelta, Point2D mousePoint) {

		synchronized(Lock.MAINLOCK) {

	        double zoomFactor = Math.exp(wheelDelta * wheelZoomIntensity);
	        
	        //Limits zoomOut so that scale is never less than 1
	        
	        if (zoomFactor < 1) {
	        	
	            if (scaleValue * zoomFactor < 1) {
	            	
	            	if (scaleValue <= 1) {
	            		return;
	            	} else {
	            		zoomFactor = (1/scaleValue);
	                    scaleValue = 1;
	            	}
	            	
	            } else {
	                scaleValue = scaleValue * zoomFactor;
	            }
	            

	        //Limits zoomOut so that scale is never less than maxScale
	            
	        } else {
	        	
	            if (scaleValue * zoomFactor > maxScale) {
	            	
	            	if (scaleValue >= maxScale) {
	            		return;
	            	} else {
	            		 zoomFactor = (maxScale/scaleValue);
	                    scaleValue = maxScale;
	            	}
	            	
	            } else {
	                scaleValue = scaleValue * zoomFactor;
	            }
	        }

	    	backup.scaleValue = scaleValue;
	    	
	            
	        Bounds innerBounds = zoomNode.getLayoutBounds();
	        Bounds viewportBounds = getViewportBounds();

	        // calculate pixel offsets from [0, 1] range
	        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
	        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());
	        
	        
	        updateScale();
	        backup.updateScale();
	        
	        // refresh ScrollPane scroll positions & target bounds
	        this.layout();
	        backup.layout(); 

	        // convert target coordinates to zoomTarget coordinates
	        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));

	        // calculate adjustment of scroll position (pixels)
	        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

	        
	        // convert back to [0, 1] range
	        // (too large/small values are automatically corrected by ScrollPane)
	        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
	        
	        this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
	        this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));

			backup.setHvalue(this.getHvalue());
			backup.setVvalue(this.getVvalue());
		}
    }

}