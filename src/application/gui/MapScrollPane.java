package application.gui;

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

//    private Node backup;
//    private Node zoomNode;
    
    public MapScrollPane backup;
    
    public MapScrollPane(Node target) {
        super();
        this.target = target;
//        this.backup = backup;
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
    
    
    
//    public void updateTarget (Node newTarget) {
//    	this.backup = this.target;
//    	this.target = newTarget;
//    	//onScroll(0, new Point2D(1, 1));
//    }
    
    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getTextDeltaY(), new Point2D(e.getX(), e.getY()));
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
    
    
    private void onScroll(double wheelDelta, Point2D mousePoint) {
    	
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

        backup.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        backup.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }
}