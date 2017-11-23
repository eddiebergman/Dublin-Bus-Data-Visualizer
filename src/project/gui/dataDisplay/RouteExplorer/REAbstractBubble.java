package project.gui.dataDisplay.RouteExplorer;


import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * Class from which any form of intractable bubble stems
 * @author Eddie
 */
abstract class REAbstractBubble extends REInteractiveObject {

    //---Constants---
    private static final int RADIUS_CHANGE_SPEED = 2;

    //---Variables---
    protected int radiusCurrent;
    protected int radiusDest;

    /**
     *
     * @param processing PApllet to use
     * @param screen screen to draw on
     * @param label label to display in bubble
     * @param x x center of circle
     * @param y y center of circle
     * @param radius radius of circle
     */
    REAbstractBubble(PApplet processing , PGraphics screen , String label, int x, int y, int radius){
        super(processing,screen,label,x,y);
        this.radiusCurrent = radius;
        this.radiusDest = radius;
    }

    /**
     * draws this bubble
     */
    public void draw(){
        screen.textAlign(PConstants.CENTER,PConstants.CENTER);
        screen.ellipse(xCurrent, yCurrent, radiusCurrent*2, radiusCurrent*2);
        int prevFill = screen.fillColor;
        screen.fill(0);
        screen.text(label,xCurrent,yCurrent);
        screen.fill(prevFill);
    }

    /**
     * updates this bubbles logic
     */
    public void update(){
        isMouseOver = isMouseOver();
        if(xCurrent != xDest || yCurrent != yDest){
            moveTo(xDest,yDest);
        }
        if(radiusCurrent != radiusDest){
            changeSizeTo(radiusDest);
        }
    }

    /**
     * @return if mouse is over this bubble
     */
    public boolean isMouseOver(){
        return processing.dist(processing.mouseX, processing.mouseY, xCurrent, yCurrent) <= this.radiusCurrent;
    }

    /**
     * give this bubble a new size to grow towards each update()
     * @param radiusDestination new radius
     */
    protected void newSize(int radiusDestination){
        radiusDest = radiusDestination;
    }

    /**
     * changes the size of this bubble to try reach towards radius passed
     * @param radiusToChangeTo radius to grow too
     */
    private void changeSizeTo(int radiusToChangeTo){
        if(radiusCurrent < radiusToChangeTo){
            radiusCurrent = (radiusCurrent >= radiusToChangeTo-RADIUS_CHANGE_SPEED)?radiusToChangeTo: radiusCurrent+RADIUS_CHANGE_SPEED;
        }else if(radiusCurrent > radiusToChangeTo){
            radiusCurrent = (radiusCurrent <= radiusToChangeTo+RADIUS_CHANGE_SPEED)?radiusToChangeTo: radiusCurrent-RADIUS_CHANGE_SPEED;
        }
    }

}
