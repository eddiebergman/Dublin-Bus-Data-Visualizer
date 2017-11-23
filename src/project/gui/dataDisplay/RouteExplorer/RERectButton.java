package project.gui.dataDisplay.RouteExplorer;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Button implementation of the REAbstract rectangle
 * @author Eddie
 */
class RERectButton extends REAbstractRect {

    //---variables---
    REInternalEvents typeClock;

    //---Constructors---

    /**
     * @param processing PApllet
     * @param screen screen to draw on
     * @param label label to display
     * @param x x pos
     * @param y y pos
     * @param width width of the rectangle
     * @param height height of the rectangle
     * @param typeClock type of clock this button represents
     */
    public RERectButton(PApplet processing , PGraphics screen , String label, int x, int y , int width , int height , REInternalEvents typeClock){
        super(processing,screen,label,x,y,width,height);
        this.typeClock = typeClock;
    }

    /**
     * @param processing PApllet
     * @param screen screen to draw on
     * @param label label to display
     * @param x x pos
     * @param y y pos
     * @param width width of the rectangle
     * @param height height of the rectangle
     * @param typeClock type of clock this button represents
     * @param roundingValue how many corners to smooth of the rectangle
     */
    public RERectButton(PApplet processing , PGraphics screen , String label, int x, int y , int width , int height , REInternalEvents typeClock , int roundingValue){
        super(processing,screen,label,x,y,width,height,roundingValue);
        this.typeClock = typeClock;
    }



}
