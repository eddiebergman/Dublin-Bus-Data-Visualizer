package project.gui.widget;

import processing.core.*;
import project.gui.GuiEventsEnum;

/**
 * WidgetDualSlider
 * Creates a dual sliderClock widget.
 *
 * @author Seng Leung
 * @version 1.00
 * @since 2016-03-30
 */
public class WidgetDualSlider extends Widget {

    private PGraphics pg;
    private String name;
    private int xSize;
    private int ySize;
    private PFont arial;

    /**
     * Constructor.
     *
     * @param processing Main PApplet
     * @param pg         PGraphics
     * @param event      Event enumerator
     * @param name       Text field label
     * @param xPos       x coordinates
     * @param yPos       y coordinates
     * @param xSize      length
     * @param ySize      width
     * @param minVal     minimum sliderClock value
     * @param maxVal     maximum sliderClock value
     */
    public WidgetDualSlider(PApplet processing, PGraphics pg,
                            GuiEventsEnum event, String name,
                            int xPos, int yPos,
                            int xSize, int ySize,
                            int minVal, int maxVal) {
        super(processing, xPos, yPos, event);
        this.event = event;
        this.pg = pg;
        this.name = name;
        this.xSize = xSize;
        this.ySize = ySize;
        this.minVal = minVal;
        this.maxVal = maxVal;

        xPrimary = xPos + (xSize / 3);
        xSecondary = xPos + (xSize * 2 / 3);
        arial = processing.createFont("Arial", 12);
    }

    private int xPrimary;
    private boolean enablePrimary = false;
    private int valPrimary;
    private int xSecondary;
    private boolean enableSecondary = false;
    private int valSecondary;
    private int maxVal;
    private int minVal;

    /**
     * Draws the sliderClock widget.
     */
    public void draw() {
        pg.fill(200);
        pg.rect(xPos, yPos, xSize, ySize / 2);

        pg.textAlign(PConstants.LEFT, PConstants.CENTER);
        pg.fill(0);
        pg.textFont(arial);
        pg.text(name, xPos + 2, yPos + ySize / 4);

        pg.fill(230);
        pg.rect(xPos, yPos + ySize / 2, xSize, ySize / 2);
        pg.fill(0);

        pg.rectMode(PConstants.CENTER);
        pg.noFill();
        pg.rect(xPos + xSize / 2, yPos + (ySize * 3 / 4), xSize - 6, 2);

//      logicPrimary(); //added this if statement
        if(enablePrimary){
            pg.fill(200);
        }else{
            pg.fill(150);
        }
        pg.rect(xPrimary, yPos + (ySize * 3 / 4), 4, 16);

//      logicSecondary(); //added this if statement
        if(enableSecondary){
            pg.fill(200);
        }else{
            pg.fill(150);
        }
        pg.rect(xSecondary, yPos + (ySize * 3 / 4), 4, 16);

        pg.rectMode(PConstants.CORNER);
        pg.fill(200);
        pg.rect(xPrimary + 2, yPos + (ySize * 3 / 4) - 2, xSecondary - xPrimary - 4, 4);

        pg.textAlign(PConstants.RIGHT, PConstants.CENTER);
        pg.fill(0);
        pg.textSize(12);
        pg.textFont(arial);
        pg.text(valPrimary + "-" + valSecondary, xPos + xSize - 4, yPos + ySize / 4);
    }

    public void update(){
        logicPrimary();
        logicSecondary();
    }

    /**
     * Performs primary sliderClock logic and calculations.
     */
    private void logicPrimary() {
        if (processing.mousePressed && processing.mouseX > xSecondary - xSize &&
                (processing.mouseX < xPrimary + ((xSecondary - xPrimary - 4) / 2) || enablePrimary) &&
                processing.mouseX < xPrimary + xSize && processing.mouseY > yPos + (ySize * 3 / 4) - ySize / 4 &&
                processing.mouseY < yPos + (ySize * 3 / 4) + 16 + ySize / 4) {
            enablePrimary = true;
          //  pg.fill(200);
            xPrimary = processing.mouseX;
            if (xPrimary > xPos + xSize - 5) {
                xPrimary = xPos + xSize - 5;
            }
            if (xPrimary < xPos + 5) {
                xPrimary = xPos + 5;
            }
        }
        if (!processing.mousePressed) {
            enablePrimary = false;
        }
        double sliderRatio = (double) xPrimary / (double) (xSize + 2);
        if (sliderRatio >= 0.98) {
            valPrimary = maxVal;
        } else if (sliderRatio <= 0.02) {
            valPrimary = minVal;
        } else {
            valPrimary = (int) ((sliderRatio * (maxVal - minVal)) + minVal);
        }
      /*  if (enablePrimary) {
            pg.fill(200);
        } else {
            pg.fill(150);
        }*/
    }

    /**
     * Performs secondary sliderClock logic and calculations.
     */
    private void logicSecondary() {
        if (processing.mousePressed && processing.mouseX < xSecondary + xSize &&
                (processing.mouseX > xSecondary - ((xSecondary - xPrimary - 4) / 2) || enableSecondary) &&
                processing.mouseY > yPos + (ySize * 3 / 4) - ySize / 4 &&
                processing.mouseY < yPos + (ySize * 3 / 4) + 16 + ySize / 4) {
            enableSecondary = true;
          //  pg.fill(200); commented this out
            xSecondary = processing.mouseX;
            if (xSecondary > xPos + xSize - 5) {
                xSecondary = xPos + xSize - 5;
            }
            if (xSecondary < xPos + 5) {
                xSecondary = xPos + 5;
            }
        }
        if (!processing.mousePressed) {
            enableSecondary = false;
        }
        double sliderRatio = (double) xSecondary / (double) (xSize + 2);
        if (sliderRatio >= 0.98) {
            valSecondary = maxVal;
        } else if (sliderRatio <= 0.02) {
            valSecondary = minVal;
        } else {
            valSecondary = (int) ((sliderRatio * (maxVal - minVal)) + minVal);
        }

        //commented these out
      /*  if (enableSecondary) {
            pg.fill(200);
        } else {
            pg.fill(150);
        }*/
    }

    /**
     * Returns the adjusted minimum value in range min-max.
     *
     * @return valPrimary
     */
    public int getMin() {
        return valPrimary;
    }

    /**
     * Returns the adjusted maximum value in range min-max.
     *
     * @return valSecondary
     */
    public int getMax() {
        return valSecondary;
    }

    @Override
    public void setMinMax(int newMin , int newMax){
        //if within allowed boundaries
        if(newMin >= minVal && newMin <= maxVal && newMax <= maxVal && newMax >= minVal) {
            if (minVal > newMax || newMax < newMin) {
                //if max is smaller than min or vice versa, set both to min
                valPrimary = newMin;
                valSecondary = newMin;
            }else {
                valPrimary = newMin;
                valSecondary = newMax;
            }
        }
    }

}
