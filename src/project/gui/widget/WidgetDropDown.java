package project.gui.widget;

import processing.core.*;
import project.gui.GuiEventsEnum;

/**
 * WidgetDropDown
 * Creates a drop down widget.
 *
 * @author  Seng Leung
 * @version 1.00
 * @since   2016-03-30
 * @version 1.01
 * @since   2016-04-30
 */
public class WidgetDropDown extends Widget {

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
    private boolean displayDropDown = false;

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
    public WidgetDropDown(PApplet processing, PGraphics pg,
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

    private int triangleColour;

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
        pg.noStroke();

        pg.fill(triangleColour);
        pg.triangle(xSize - 4, yPos + ((ySize / (numCheckbox + 1)))/3 + 2,
                    xSize - 16, yPos +  ((ySize / (numCheckbox + 1)))/3 + 2,
                    xSize - 10, yPos + ((ySize / (numCheckbox + 1)) * 2)/3 + 2);
        pg.stroke(100);
        pg.fill(230);

        //triangleLogic();

        // Checkbox logic
        logicCheckbox();

        // Reset rect mode
        pg.rectMode(PConstants.CORNER);
        clickDelay++;
    }

    public void update(){
        //triangleLogic();
        if (processing.mouseX > xSize - 15 && processing.mouseX < xSize &&
                processing.mouseY > yPos + ((ySize / (numCheckbox + 1)))/3 + 2 &&
                processing.mouseY < yPos + ((ySize / (numCheckbox + 1)) * 2)/3 + 10) {
            triangleColour = processing.color(70);
        } else {
            triangleColour = processing.color(100);
        }

        if (processing.mousePressed && clickDelay > 10 &&
                processing.mouseX > xSize - 15 && processing.mouseX < xSize &&
                processing.mouseY > yPos + ((ySize / (numCheckbox + 1)))/3 + 2 &&
                processing.mouseY < yPos + ((ySize / (numCheckbox + 1)) * 2)/3 + 10) {
            //System.out.println(displayDropDown);
            if (displayDropDown) {
                displayDropDown = false;
            } else {
                displayDropDown = true;
            }
            clickDelay = 0;
        }

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
    }

    private void triangleLogic() {
/*        if (processing.mousePressed && clickDelay > 10 &&
            processing.mouseX > xSize - 15 && processing.mouseX < xSize &&
            processing.mouseY > yPos + ((ySize / (numCheckbox + 1)))/3 + 2 &&
            processing.mouseY < yPos + ((ySize / (numCheckbox + 1)) * 2)/3 + 10) {
            System.out.println(displayDropDown);
            if (displayDropDown) {
                displayDropDown = false;
            } else {
                displayDropDown = true;
            }
            clickDelay = 0;
        }*/
    }

    private void logicCheckbox() {
        if (displayDropDown) {
            for (int i = 0; i < numCheckbox; i++) {
                pg.rect(xPos, yPos + (ySize / (numCheckbox + 1)) + (((ySize / (numCheckbox + 1)) * i)), xSize, (ySize / (numCheckbox + 1)));
            }
            // Checkbox
            pg.rectMode(PConstants.CENTER);
            for (int i = 0; i < numCheckbox; i++) {
                pg.rect(xSize - 10, yPos + (ySize / ((numCheckbox + 1) * 2)) + ((ySize / (numCheckbox + 1)) * (i + 1)), 12, 12);
            }
/*            // Click detection
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
            }*/
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
    }

    /**
     * Returns the boolean array of checkbox results.
     *
     * @return checkboxes
     */
    public boolean[] getCheckbox() {
        return checkboxes;
    }

    /**
     * gets all the options (soz for intruding in widgets , needed to know what boolean matches to each string)
     * @return list of options this dropDown displays
     */
    public String[] getOptions(){
        return options;
    }

    @Override
    public void setCheckBox(boolean[] newSelections) {
        checkboxes = newSelections;
    }
}
