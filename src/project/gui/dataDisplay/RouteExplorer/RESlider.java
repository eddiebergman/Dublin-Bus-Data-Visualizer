package project.gui.dataDisplay.RouteExplorer;

import processing.core.*;
import project.gui.GuiEventsEnum;
import project.gui.widget.Widget;
import project.gui.widget.WidgetSlider;
import project.model.ModelMain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Slider for the RouteExplorer but changed how it draws
 * @author Eddie
 * @author Seng (Created intial Widget Slider)
 */
public class RESlider extends WidgetSlider {

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
    public RESlider(PApplet processing, PGraphics pg,
                        GuiEventsEnum event, String name,
                        int xPos, int yPos,
                        int xSize, int ySize,
                        int minVal, int maxVal) {
        super(processing,pg,event,name, xPos, yPos,xSize,ySize,minVal,maxVal);
    }

    /**
     * Draws the sliderClock widget.
     */
    public void draw() {
        int prevWeight = (int) pg.strokeWeight;
        pg.strokeWeight(1);
        pg.fill(200);
       // pg.rect(xPos, yPos, xSize, ySize / 2);

        pg.rectMode(PConstants.CENTER);
        pg.noFill();
        pg.rect(xPos + xSize / 2, yPos + (ySize * 3 / 4), xSize - 6, 2);
        pg.fill(150);
        pg.rect(sliderVal, yPos + (ySize * 3 / 4), 4, 16);

        pg.fill(0);
        pg.textFont(arial);
        pg.textAlign(PConstants.CENTER, PConstants.TOP);
        pg.text(RouteExplorer.toDate(ModelMain.dayToTimeStamp(adjustedVal)),sliderVal, yPos );
        pg.textAlign(PConstants.BOTTOM , PConstants.CENTER);
        pg.text(name,xPos,yPos+ySize);
        pg.rectMode(PConstants.CORNER);
        pg.strokeWeight(prevWeight);
    }



}
