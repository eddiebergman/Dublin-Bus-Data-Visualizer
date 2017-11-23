package project.gui.widget;

import project.gui.GuiEventsEnum;
import processing.core.*;

/**
 * WidgetButton
 * Creates a button widget.
 *
 * @author  Seng Leung
 * @version 1.00
 * @since   2016-03-13
 * @version 1.01
 * @since   2016-03-14
 * - Removed ControlP5.
 * - Recoded using proprietary Processing for
 *   full compatibility with PGraphics.
 */
public class WidgetButton extends Widget {

    private PGraphics pg;
    private GuiEventsEnum event;
    private String name;
    private int xSize, ySize;
    private int colDefault;
    private int colHover;
    private boolean isMouseOverButton;
    private PFont font;

    /**
     * Simplified constructor with no colours.
     * @param p             Main PApplet
     * @param pg            PGraphics
     * @param event         Event enumerator
     * @param name          Button label
     * @param xPos          x coordinates
     * @param yPos          y coordinates
     * @param xSize         length
     * @param ySize         width
     */
    public WidgetButton(PApplet p, PGraphics pg,
                        GuiEventsEnum event,  String name,
                        int xPos, int yPos,
                        int xSize, int ySize) {
        super(p, xPos, yPos, event);
        this.pg = pg;
        this.event = event;
        this.name = name;
        this.xSize = xSize;
        this.ySize = ySize;
        this.isMouseOverButton = false;
        this.font = processing.createFont("Arial", 12);
        this.colDefault = processing.color(190);
        this.colHover = processing.color(180);
    }

    /**
     * Full constructor with colours.
     * @param processing    Main PApplet
     * @param pg            PGraphics
     * @param event         Event enumerator
     * @param name          Button label
     * @param xPos          x coordinates
     * @param yPos          y coordinates
     * @param xSize         length
     * @param ySize         width
     * @param colDefault    colour default
     * @param colHover      colour when mouse hovers
     */
    public WidgetButton(PApplet processing, PGraphics pg,
                        GuiEventsEnum event,  String name,
                        int xPos, int yPos,
                        int xSize, int ySize,
                        int colDefault, int colHover) {
        this(processing,pg,event,name,xPos,yPos,xSize,ySize);
        this.colDefault = colDefault;
        this.colHover = colHover;
    }

    /**
     * Draws the button on a PGraphics window.
     */
    @Override
    public void draw(){
        //super.draw(); // commented out by samuel, not needed?
        if (isMouseOverButton) {
            pg.stroke(0);
            pg.fill(colHover);
        } else {
            pg.stroke(100);
            pg.fill(colDefault);
        }
        pg.textFont(font);
        pg.rect(xPos, yPos, xSize, ySize);
        pg.fill(30);
        pg.textAlign(PConstants.CENTER, PConstants.CENTER);
        pg.text(name, xPos + xSize/2, yPos + ySize/2);
    }

    @Override
    public void update(){
        isMouseOverButton = processing.mouseX > xPos && processing.mouseX < xPos + xSize &&
                processing.mouseY > yPos && processing.mouseY < yPos + ySize;
    }

    /**
     * Event handling for button if called.
     * @return event    Event enumerator
     */
    public GuiEventsEnum getEvent() {
        if (processing.mouseX > xPos && processing.mouseX < xPos + xSize &&
            processing.mouseY > yPos && processing.mouseY < yPos + ySize) {
            return this.event;
        }
        return null;
    }
}
