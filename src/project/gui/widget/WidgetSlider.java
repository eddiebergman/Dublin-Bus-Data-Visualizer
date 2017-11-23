package project.gui.widget;

import processing.core.*;
import project.gui.GuiEventsEnum;

/**
 * WidgetSlider
 * Creates a sliderClock widget.
 *
 * @author  Seng Leung
 * @version 1.00
 * @since   2016-03-21
 * @version 1.01
 * @since   2016-04-04
 * - Slider can be adjusted at any position
 *   once mouse is clicked within range.
 */
public class WidgetSlider extends Widget {

    public PGraphics pg;
    public String name;
    public int xSize;
    public int ySize;
    public PFont arial;

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
    public WidgetSlider(PApplet processing, PGraphics pg,
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

        sliderVal = xPos + (xSize / 2);
        arial = processing.createFont("Arial", 12);
    }

    public boolean enable = false;
    public int sliderVal;
    public int adjustedVal;
    public int maxVal;
    public int minVal;

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
        pg.fill(150);
        logicSlider();
        pg.rect(sliderVal, yPos + (ySize * 3 / 4), 4, 16);
        pg.rectMode(PConstants.CORNER);

        pg.textAlign(PConstants.RIGHT, PConstants.CENTER);
        pg.fill(0);
        pg.textFont(arial);
        pg.text(adjustedVal, xPos + xSize - 4, yPos + ySize / 4);
    }


    public void update(){
        if ((processing.mousePressed && processing.mouseX > sliderVal - xSize &&
                processing.mouseX < sliderVal + xSize && processing.mouseY > yPos + (ySize * 3 / 4) - ySize / 4 &&
                processing.mouseY < yPos + (ySize * 3 / 4) + 16 + ySize / 4) || enable) {
            //pg.fill(200); // this was causing flickering
            enable = true;
            sliderVal = processing.mouseX;
            if (sliderVal > xPos + xSize - 5) {
                sliderVal = xPos + xSize - 5;
            }
            if (sliderVal < xPos + 5) {
                sliderVal = xPos + 5;
            }
        }
        if (!processing.mousePressed) {
            enable = false;
        }

        //processing have a method for ratio stuff , map( someValue , oldMin , oldMax , newMin , newMax)
        //e.g.  map(6, 0 , 10 ,0 , 100) == 60 ,
        // 6 used to go from 0 to 10 ,we want where 6 would be in 0 to 100 in the same ratio
        //sliderValue used to go from x1 to x2(x1+size) , we need that in same ratio from minVal to maxVal
        adjustedVal = Math.round( PApplet.map(sliderVal,xPos+5,xPos+xSize-5,minVal,maxVal) );


        /*
        double sliderRatio = (double) sliderVal / (double) (xSize + 2);
        if (sliderRatio >= 0.98) {
            adjustedVal = maxVal;
        } else if (sliderRatio <= 0.02) {
            adjustedVal = minVal;
        } else {
            adjustedVal = (int) ((sliderRatio * (maxVal - minVal)) + minVal);
        }*/
    }

    /**
     * Performs sliderClock logic and calculations.
     */
    private void logicSlider() {
/*        if (processing.mousePressed && processing.mouseX > sliderVal - xSize &&
            processing.mouseX < sliderVal + xSize && processing.mouseY > yPos + (ySize * 3 / 4) - ySize / 4 &&
            processing.mouseY < yPos + (ySize * 3 / 4) + 16 + ySize / 4) {
            pg.fill(200);
            sliderVal = processing.mouseX;
            if (sliderVal > xPos + xSize - 5) {
                sliderVal = xPos + xSize - 5;
            }
            if (sliderVal < xPos + 5) {
                sliderVal = xPos + 5;
            }
        }
        double sliderRatio = (double) sliderVal / (double) (xSize + 2);
        if (sliderRatio >= 0.98) {
            adjustedVal = maxVal;
        } else if (sliderRatio <= 0.02) {
            adjustedVal = minVal;
        } else {
            adjustedVal = (int) ((sliderRatio * (maxVal - minVal)) + minVal);
        }*/
    }

    /**
     * Returns the adjusted value in range min-max.
     *
     * @return adjustedVal
     */

    @Override
    public int getInt() {
        return adjustedVal;
    }

    @Override
    public void setInt(int newValue){
        this.adjustedVal = newValue;
    }

}
