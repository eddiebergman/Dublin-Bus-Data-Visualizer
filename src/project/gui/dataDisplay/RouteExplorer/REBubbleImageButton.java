package project.gui.dataDisplay.RouteExplorer;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import project.gui.GuiEventsEnum;

/**
 * Abstract Bubble with button capabilities with an image
 */
public class REBubbleImageButton extends REAbstractBubble {

    //---Varaibles---
    PImage image;
    GuiEventsEnum type;

    //---Constructor---

    /**
     *
     * @param processing PApllet
     * @param screen screen to draw on
     * @param image image to display in buble
     * @param x x pos
     * @param y y pos
     * @param radius radius
     * @param type event type
     */
    public REBubbleImageButton(PApplet processing, PGraphics screen, PImage image, int x, int y, int radius , GuiEventsEnum type) {
        super(processing, screen, "", x, y, radius);
        this.image = image;
        this.type = type;
    }

    /**
     * draws this BubbleImageButton to screen
     */
    public void draw(){
        super.draw();
        if(radiusCurrent > 0) {
            int prevMode = screen.imageMode;
            screen.imageMode(PConstants.CENTER);
            PImage tempImage = image.copy();
            tempImage.resize(radiusCurrent, radiusCurrent);
            screen.image(tempImage,xCurrent,yCurrent);
            screen.imageMode(prevMode);
        }
    }


}
