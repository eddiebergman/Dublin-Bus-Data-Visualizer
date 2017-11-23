package project.gui.dataDisplay.RouteExplorer;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * Class for any rectangle intractable
 * @author Eddie
 */
abstract class REAbstractRect extends REInteractiveObject {

    //---Constants---
    private static final int WIDTH_CHANGE_SPEED = 2;
    private static final int HEIGHT_CHANGE_SPEED = 1;

    //---variables
    protected int widthCurrent;
    protected int widthDest;
    protected int heightCurrent;
    protected int heightDest;
    protected int roundingValue;

    //---Constructor---
    /**
     *
     * @param processing PApllet to use
     * @param screen screen to draw on
     * @param label label to display in bubble
     * @param x x left of rect
     * @param y y top of rect
     * @param width width of rect
     * @param height height of rect
     */
    REAbstractRect(PApplet processing , PGraphics screen , String label, int x, int y, int width , int height){
        super(processing,screen,label,x,y);
        this.widthCurrent = width;
        this.widthDest = width;
        this.heightCurrent = height;
        this.heightDest = height;
        this.roundingValue = 0;
    }

    /**
     *
     * @param processing PApllet to use
     * @param screen screen to draw on
     * @param label label to display in bubble
     * @param x x left of rect
     * @param y y top of rect
     * @param width width of rect
     * @param height height of rect
     * @param roundingValue how many edges of rectangle to round
     */
    REAbstractRect(PApplet processing , PGraphics screen , String label, int x, int y, int width , int height , int roundingValue){
        this(processing,screen,label,x,y,width,height);
        this.roundingValue = roundingValue;
    }


    /**
     * draws the rectangle to the screen
     */
    public void draw(){
        screen.textAlign(PConstants.CENTER,PConstants.CENTER);
        int prevRectMode = screen.rectMode;
        screen.rectMode(PConstants.CENTER);
        screen.rect(xCurrent, yCurrent, widthCurrent, heightCurrent , roundingValue);
        int prevFill = screen.fillColor;
        screen.fill(0);
        screen.text(label,xCurrent,yCurrent);
        screen.fill(prevFill);
        screen.rectMode(prevRectMode);
    }

    /**
     * handles all logic for this rectangle intractable
     */
    public void update(){
        isMouseOver = isMouseOver();
        if(xCurrent != xDest || yCurrent != yDest){
            moveTo(xDest,yDest);
        }
        if(widthCurrent != widthDest || heightCurrent != heightDest){
            changeSizeTo(widthDest,heightDest);
        }
    }

    /**
     * changes the size towards passed params
     * @param widthDest new width to aim for
     * @param heightDest new height to aim for
     */
    private void changeSizeTo(int widthDest , int heightDest){
        if(widthCurrent < widthDest) {
            widthCurrent = (widthCurrent >= widthDest - WIDTH_CHANGE_SPEED) ? widthDest : widthCurrent + WIDTH_CHANGE_SPEED;
        }else if(widthCurrent > widthDest) {
            widthCurrent = (widthCurrent <= widthDest + WIDTH_CHANGE_SPEED) ? widthDest : widthCurrent - WIDTH_CHANGE_SPEED;
        }
        if(heightCurrent < heightDest) {
            heightCurrent = (heightCurrent >= heightDest - HEIGHT_CHANGE_SPEED) ? heightDest : heightCurrent + HEIGHT_CHANGE_SPEED;
        }else if(heightCurrent > heightDest) {
            heightCurrent = (heightCurrent <= heightDest + HEIGHT_CHANGE_SPEED) ? heightDest : heightCurrent - HEIGHT_CHANGE_SPEED;
        }

    }

    /**
     * @return if mouse is over
     */
    public boolean isMouseOver(){
        return processing.mouseX > xCurrent-widthCurrent/2 && processing.mouseX < xCurrent + widthCurrent/2
                && processing.mouseY > yCurrent-heightCurrent/2 && processing.mouseY < yCurrent + heightCurrent/2;
    }

    /**
     * new size to try aim for each update()
     * @param widthDestination new widht
     * @param heightDestination new height
     */
    public void newSize(int widthDestination,int heightDestination){
        this.widthDest = widthDestination;
        this.heightDest = heightDestination;
    }
}
