package project.gui.widget;

import processing.core.*;
import project.gui.GuiEventsEnum;

/**
 * WidgetCheckbox
 * Creates a checkbox widget.
 *
 * @author  Seng Leung
 * @version 1.00
 * @since   2016-03-30
 */
public class WidgetCheckbox extends Widget {

    private PGraphics pg;
    private String name;
    private int xSize;
    private int ySize;
    private int numCheckbox;
    private boolean[] checkboxes;
    private int clickDelay = 0;
    private String[] options;
    private PFont arial;
    private PFont consolas;

    /**
     * Constructor.
     *
     * @param processing    Main PApplet
     * @param pg            PGraphics
     * @param event         Event enumeration
     * @param name          Checkbox name label
     * @param options       String array of checkbox options
     * @param xPos          x coordinates
     * @param yPos          y coordinates
     * @param xSize         length
     * @param ySize         width
     */
    public WidgetCheckbox(PApplet processing, PGraphics pg,
                          GuiEventsEnum event, String name,
                          String[] options,
                          int xPos, int yPos,
                          int xSize, int ySize) {
        super(processing, xPos, yPos, event);
        this.event = event;
        this.pg = pg;
        this.name = name;
        this.xSize = xSize;
        this.ySize = ySize;
        this.options = options;
        consolas = processing.createFont("Consolas", 12);
        arial = processing.createFont("Arial", 12);
        numCheckbox = options.length;
        checkboxes = new boolean[numCheckbox];
        for (int i = 0; i < checkboxes.length; i++) {
            checkboxes[i] = false;
        }
    }

    /**
     * Draws the checkbox widget.
     */
    public void draw () {
        // Initialise rect mode
        pg.textAlign(PConstants.LEFT, PConstants.CENTER);
        pg.textFont(consolas);
        pg.rectMode(PConstants.CORNER);

        // Title
        pg.fill(200);
        pg.rect(xPos, yPos, xSize, (ySize / (numCheckbox + 1)));
        pg.textAlign(PConstants.LEFT, PConstants.CENTER);
        pg.fill(0);
        pg.textFont(arial);
        pg.text(name, xPos + 2, yPos + (ySize / ((numCheckbox + 1)*2)));
        pg.fill(230);

        // Checkbox logic
        logicCheckbox();

        // Reset rect mode
        pg.rectMode(PConstants.CORNER);
    }

    private void logicCheckbox() {
        for (int i = 0; i < numCheckbox; i++) {
            pg.rect(xPos, yPos + (ySize / (numCheckbox + 1)) + (((ySize / (numCheckbox + 1)) * i)), xSize, (ySize / (numCheckbox + 1)));
        }
        // Checkbox
        pg.rectMode(PConstants.CENTER);
        for (int i = 0; i < numCheckbox; i++) {
            pg.rect(xSize - 10, yPos + (ySize / ((numCheckbox + 1) * 2)) + ((ySize / (numCheckbox + 1)) * (i + 1)), 12, 12);
        }

/*        // Click detection
        for (int i = 0; i < numCheckbox; i++) {
            if (processing.mousePressed && clickDelay > 10 &&
                    processing.mouseX > xSize - 10 - 6 && processing.mouseX < xSize - 2 &&
                    processing.mouseY > yPos + (ySize / ((numCheckbox + 1) * 2)) + ((ySize / (numCheckbox + 1)) * (i + 1)) - 6 &&
                    processing.mouseY < yPos + (ySize / ((numCheckbox + 1) * 2)) + ((ySize / (numCheckbox + 1)) * (i + 1)) + 6) {
                if (checkboxes[i]) {
                    checkboxes[i] = false;
                } else {
                    checkboxes[i] = true;
                }
                clickDelay = 0;
            }
        }
        clickDelay++;*/

        // Confirmed checkbox
        for (int i = 0; i < numCheckbox; i++) {
            if (checkboxes[i]) {
                pg.fill(150);
                pg.rect(xSize - 10, yPos + (ySize / ((numCheckbox + 1) * 2)) + ((ySize / (numCheckbox + 1)) * (i + 1)), 8, 8);
            }
        }
        // Checkbox names
        pg.fill(0);
        for (int i = 0; i < numCheckbox; i++) {
            pg.text(options[i], xPos + 2, yPos + (ySize / ((numCheckbox + 1) * 2)) + ((ySize / (numCheckbox + 1)) * (i + 1)));
        }
    }

    public void update(){
        // Click detection
        for (int i = 0; i < numCheckbox; i++) {
            if (processing.mousePressed && clickDelay > 10 &&
                    processing.mouseX > xSize - 10 - 6 && processing.mouseX < xSize - 2 &&
                    processing.mouseY > yPos + (ySize / ((numCheckbox + 1) * 2)) + ((ySize / (numCheckbox + 1)) * (i + 1)) - 6 &&
                    processing.mouseY < yPos + (ySize / ((numCheckbox + 1) * 2)) + ((ySize / (numCheckbox + 1)) * (i + 1)) + 6) {
                if (checkboxes[i]) {
                    checkboxes[i] = false;
                } else {
                    checkboxes[i] = true;
                }
                clickDelay = 0;
            }
        }
        clickDelay++;
    }

    /**
     * Returns the boolean array of checkbox results.
     *
     * @return checkboxes
     */
    public boolean[] getCheckbox() {
        return checkboxes;
    }


}
