package project.gui.dataDisplay.dataGraphLine;

import processing.core.*;

/**
 * Created by Conal on 16/03/2016.
 */
public class GraphYGrad {

    private float displayNumber;
    private float xPos;
    private float yPos;
    private float w = 5;

    private PApplet processing;
    private PGraphics parentGraphicWindow;

    public GraphYGrad(PApplet p, PGraphics pg, float displayNo, float x, float y){
        this.processing = p;
        this.parentGraphicWindow = pg;

        this.xPos = x;
        this.yPos = y;
        this.displayNumber = displayNo;

    }

    void draw(){
        parentGraphicWindow.fill(0);
        parentGraphicWindow.line(xPos,yPos,xPos - w,yPos);

        parentGraphicWindow.textAlign(PConstants.CENTER);
        parentGraphicWindow.text((int)displayNumber,xPos - 30,yPos);
    }
}
