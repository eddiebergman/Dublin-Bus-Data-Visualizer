package project.gui.dataDisplay.RouteExplorer;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.Optional;

/**
 * Solid implementation of the REAbstractBubble class
 * For a bus
 * @author Eddie
 */
public class REBubbleBus extends REAbstractBubble {


    /**
     *
     * @param processing PApplet this class is using
     * @param screen screen to draw on
     * @param label label of the bus
     * @param x x pos
     * @param y y pos
     * @param radius initial radius of this REBubbleBus
     */
    protected REBubbleBus(PApplet processing , PGraphics screen , String label, int x, int y, int radius ){
        super(processing,screen,label,x,y,radius);
    }






}
