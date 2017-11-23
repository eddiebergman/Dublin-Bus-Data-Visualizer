package project.gui.dataDisplay.RouteExplorer;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Class used to represent something interactive and moveable
 * @author  Eddie
 */
abstract class REInteractiveObject implements REInteractiveObjectI {

    //---Constants---
    private static final float EASING_STOP = 0.05f;

    //---variables---
    protected int xOriginal;
    protected int yOriginal;
    protected int xDest;
    protected int yDest;
    protected int xCurrent;
    protected int yCurrent;
    protected boolean pressed;
    protected boolean isMouseOver = false;
    protected boolean selected = false;
    protected String label;
    protected PGraphics screen;
    protected PApplet processing;

    /**
     *
     * @param processing PApllet to use
     * @param screen screen to draw on
     * @param label label to associate with
     * @param x its x pos
     * @param y its y pos
     */
    protected REInteractiveObject(PApplet processing , PGraphics screen , String label, int x, int y) {
        this.xOriginal = x;
        this.yOriginal = y;
        this.xDest = x;
        this.yDest = y;
        this.xCurrent = x;
        this.yCurrent = y;
        this.label = label;
        this.screen = screen;
        this.processing = processing;
        this.pressed = false;
    }

    /**
     * changes the x/y towards the passed x/y
     * @param destX
     * @param destY
     */
    public void moveTo(int destX , int destY){
        if (xCurrent != destX) {
            if (xCurrent < destX - 2) {
                xCurrent = xCurrent + (int) ((Math.abs(destX - xCurrent) * EASING_STOP + 1));
            } else if (xCurrent > destX + 2) {
                xCurrent = xCurrent - (int) ((Math.abs(destX - xCurrent) * EASING_STOP + 1));
            } else {
                xCurrent = destX;
            }
        }
        if (yCurrent != destY) {
            if (yCurrent < destY - 2) {
                yCurrent = yCurrent + (int) ((Math.abs(destY - yCurrent) * EASING_STOP + 1));
            } else if (yCurrent > destY + 2) {
                yCurrent = yCurrent - (int) ((Math.abs(destY - yCurrent) * EASING_STOP + 1));
            } else {
                yCurrent = destY;
            }
        }
    }

    /**
     * changes the status of this intractable
     */
    public void clicked(){
        this.selected = !this.selected;
    }

    /**
     * returns if this is at a specified location
     * @param x x to check
     * @param y y to check
     * @return true or flase
     */
    public boolean isAtPosition(int x , int y){
        return (xCurrent == x && yCurrent == y);
    }

    /**
     * give this intractable a new x and y to move towards each update()
     * @param x new x
     * @param y new y
     */
    public void newDestination(int x , int y){
        xDest = x;
        yDest = y;
    }

    /**
     * @return if this has been selected
     */
    public boolean isSelected(){
        return selected;
    }


}
