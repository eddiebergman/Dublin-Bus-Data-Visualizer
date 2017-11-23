package project.gui.dataDisplay.dataGraphLine;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * Created by Conal on 16/03/2016.
 */
public class GraphXGrad {
    private float displayNumber;
    private float xPos,yPos;
    private float w = 5;

    private PApplet processing;
    private PGraphics parentGraphicWindow;

    public GraphXGrad(PApplet p, PGraphics pg, float displayNo, float x, float y){
        this.processing = p;
        this.parentGraphicWindow = pg;

        this.xPos = x;
        this.yPos = y;
        this.displayNumber = displayNo;

    }

    void draw(){
        parentGraphicWindow.fill(0);
        parentGraphicWindow.line(xPos,yPos,xPos,yPos + w);

        parentGraphicWindow.textAlign(PConstants.CENTER);
        parentGraphicWindow.text((int)displayNumber,xPos,yPos + 20);
    }

}
