package project.gui.widget;

import processing.core.PApplet;
import processing.core.PGraphics;
import project.gui.GuiEventsEnum;
import project.gui.GuiMainController;
import project.gui.GuiStatusEnum;

/**
 * WidgetRectangle
 * Creates a three rectangle widget.
 *
 * @author Seng Leung
 * @version 1.00
 * @since 2016-03-13
 */

public class WidgetRectangle extends Widget {

    private PGraphics pg;
    private int xSize, ySize;
    private boolean isMouseOverButton;

    /**
     * Constructs the widget rectangle.
     *
     * @param p
     * @param xpos
     * @param ypos
     * @param event
     * @param pg
     */
    public WidgetRectangle(PApplet p, PGraphics pg, int xpos, int ypos, GuiEventsEnum event) {
        super(p, xpos, ypos, event);
        this.pg = pg;
        this.event = event;
        this.xSize = 30;
        this.ySize = 23;
        isMouseOverButton = false;
    }

    /**
     * Draws the three rectangles.
     */
    public void draw() {
        super.draw();
        if (isMouseOverButton) {
            pg.stroke(50);
        } else {
            pg.stroke(150);
        }
        pg.fill(255);
        pg.rect(xPos, yPos + 0, xSize, 5); //Height = 5, Gap between = 4;
        pg.rect(xPos, yPos + 9, xSize, 5);
        pg.rect(xPos, yPos + 18, xSize, 5);
    }

    @Override
    public void update(){
        if(GuiMainController.currentNavSideBar.getStatus() == GuiStatusEnum.ANIMATING) {
            xPos = GuiMainController.currentNavSideBar.getXPos() + GuiMainController.currentNavSideBar.windowWidth + 5;
        }

        isMouseOverButton = processing.mouseX > xPos && processing.mouseX < xPos + xSize &&
                processing.mouseY > yPos && processing.mouseY < yPos + ySize;
    }

    /**
     * Returns the event if mouse pressed.
     *
     * @return event
     */
    public GuiEventsEnum getEvent() {
        if (processing.mouseX > xPos && processing.mouseX < xPos + 33 &&
                processing.mouseY > yPos && processing.mouseY < yPos + 30) {
            return event;
        }
        return null;
    }

}
