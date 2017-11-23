package project.gui.dataDisplay.dataGraphLine;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * Created by Conal on 16/03/2016.
 */
public class InfoWindow {

    private PApplet processing;
    private PGraphics parentGraphicWindow;

    private int sizeX,sizeY;
    private boolean isVisible;
    private float displayX;
    private float displayY;
    private float xPos;
    private float yPos;
    private String lineID;
    private GraphLine parentGraphLine;

    public InfoWindow(PApplet p, PGraphics pg, GraphLine parentGraph){
        this.processing = p;
        this.parentGraphicWindow = pg;

        this.parentGraphLine = parentGraph;

        sizeX = 155;
        sizeY = 75;
        isVisible = false;
    }

    public void draw(){
        if(isVisible)
        {
            parentGraphicWindow.stroke(0,0,0,255);
            parentGraphicWindow.fill(255,255,255,50);
            parentGraphicWindow.rect(processing.mouseX - (sizeX/2) - 1,processing.mouseY - (sizeY) -15,sizeX,sizeY,4);
            parentGraphicWindow.fill(0,160);
            parentGraphicWindow.rect(processing.mouseX - (sizeX/2),processing.mouseY - (sizeY)-14,sizeX,sizeY,4);

            parentGraphicWindow.fill(255);
            parentGraphicWindow.textAlign(PConstants.CENTER, PConstants.CENTER);
            parentGraphicWindow.text(parentGraphLine.getXAxisLabel() + " = " +  displayX, processing.mouseX + 4, processing.mouseY - (sizeY) );
            parentGraphicWindow.text(parentGraphLine.getYAxisLabel() + " = " + (int) displayY, processing.mouseX + 4, processing.mouseY - (sizeY) + 16);
            parentGraphicWindow.text( "Line ID = " + lineID, processing.mouseX + 4, processing.mouseY - (sizeY) + 32);
        }
    }

    public void setDisplayValues(float x,float y,String ID){
        this.displayX = x;
        this.displayY = y;
        this.lineID = ID;
    }

    public void makeVisible(){
        this.isVisible = true;
    }

    public void makeInvisible(){
        this.isVisible = false;
    }
}

