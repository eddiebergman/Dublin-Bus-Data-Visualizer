package project.gui.dataDisplay.dataGraphBar;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * Created by Conal on 31/03/2016.
 */
public class GraphBarYGrad {
    private int displayNumber;
    private float xPos,yPos;
    private float w = 5;

    private PApplet processing;
    private PGraphics parentGraphicWindow;

    public GraphBarYGrad(PApplet p, PGraphics pg, int displayNo, int x, float y){
        this.processing = p;
        this.parentGraphicWindow = pg;

        this.xPos = x;
        this.yPos = y;
        this.displayNumber = displayNo;

    }

    void draw(){
        parentGraphicWindow.fill(0);
        parentGraphicWindow.stroke(0);
        parentGraphicWindow.line(xPos,yPos,xPos - w,yPos);

        parentGraphicWindow.textAlign(PConstants.CENTER);
        parentGraphicWindow.text(displayNumber,xPos - 30,yPos);
    }
}
