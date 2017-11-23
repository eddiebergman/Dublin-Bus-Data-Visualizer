package project.gui.dataDisplay.RouteExplorer;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import project.gui.GuiEventsEnum;

/**
 * Rectangle button but with an image instead
 * @author Eddie
 */
class RERectImageButton extends REAbstractRect{

    //---Variables---
    GuiEventsEnum type;
    PImage image;
    int xImageOffset;
    int yImageOffset;

    //---Constructor---

    /**
     * @param processing PApplet to use
     * @param screen screen to draw on
     * @param image image to use
     * @param x x pos
     * @param y y pos
     * @param type type of event this button holds
     */
    protected RERectImageButton(PApplet processing , PGraphics screen , PImage image, int x, int y , GuiEventsEnum type){
        super(processing,screen,"",x,y,image.width+1,image.height+1,4);
        this.image = image;
        this.type = type;
        this.xImageOffset = image.width/2;
        this.yImageOffset = image.height/2;
    }

    //---methods---

    /**
     * draws itself to the screen
     */
    public void draw(){
        super.draw();
        if(widthCurrent > 0 && heightCurrent > 0) {
            int prevMode = screen.imageMode;
            screen.imageMode(PConstants.CENTER);
            PImage temp = image.copy();
            temp.resize(widthCurrent,heightCurrent);
            screen.image(temp, xCurrent, yCurrent);
            screen.imageMode(prevMode);
        }

    }




}
