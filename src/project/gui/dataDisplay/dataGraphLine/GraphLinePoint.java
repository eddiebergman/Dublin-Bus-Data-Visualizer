package project.gui.dataDisplay.dataGraphLine;

import processing.core.PApplet;
import processing.core.PGraphics;


/**
 * Created by Conal on 16/03/2016.
 */
public class GraphLinePoint {

    private PApplet processing;
    private PGraphics parentGraphicWindow;

    private float xPos, yPos;
    private int radiusOriginal, radiusLarge;
    private float graphX;
    private float graphY;
    public boolean isMouseOver;
    private int ID;
    private String busId;

    public InfoWindow showDataWindow;

    private GraphLine parentGraphLine;

    public GraphLinePoint(PApplet p, PGraphics pg, float xPos, float yPos, int rad, GraphLine graph, int ID ,String busId){
        this.processing = p;
        this.parentGraphicWindow = pg;
        this.parentGraphLine = graph;
        this.xPos = xPos;
        this.yPos = yPos;
        this.radiusOriginal = rad;
        this.radiusLarge = radiusOriginal * 2;
        this.ID = ID;
        this.busId = busId;

        this.showDataWindow = new InfoWindow(processing,parentGraphicWindow,parentGraphLine);
        this.isMouseOver = false;

        this.graphX = getGX(parentGraphLine, this.xPos);
        this.graphY = getGY(parentGraphLine, this.yPos);
    }

    void draw(){
        parentGraphicWindow.noStroke();
       if(ID  % 7== 0){
           parentGraphicWindow.fill(224,52,52);
       }else if(ID  % 7== 1){
           parentGraphicWindow.fill(129, 235, 103);
       }else if(ID  % 7== 2){
           parentGraphicWindow.fill(0,218,255);
       }else if(ID  % 7== 3){
           parentGraphicWindow.fill(245, 109, 0);
       }else if(ID  % 7== 4){
           parentGraphicWindow.fill(67,122,250);
       }else if(ID  % 7== 5){
           parentGraphicWindow.fill(198,85,230);
       }else if(ID  % 7== 6){
            parentGraphicWindow.fill(250,122,203);
       }else {
           parentGraphicWindow.fill(200, 100, 0);
       }
        //rect(gx - radius/2,gy - radius/2,radius,radius);
        parentGraphicWindow.ellipse(graphX ,graphY,isMouseOver?radiusLarge:radiusOriginal,isMouseOver?radiusLarge:radiusOriginal);
        update();
        this.graphX = getGX(parentGraphLine, this.xPos);
        this.graphY = getGY(parentGraphLine, this.yPos);

    }

    void update(){

        if(processing.mouseX > graphX - (radiusOriginal)
                && processing.mouseX < graphX + (radiusOriginal)
                && processing.mouseY > graphY - (radiusOriginal)
                && processing.mouseY < graphY + (radiusOriginal))
        {
            showDataWindow.makeVisible();

            parentGraphicWindow.stroke(0);
            parentGraphicWindow.fill(0);
            parentGraphicWindow.line(graphX, graphY, (float)(parentGraphicWindow.width * 0.1), graphY);
            
        }
        else
        {
            showDataWindow.makeInvisible();

        }



        showDataWindow.setDisplayValues(xPos,yPos,busId);
    }

    private float getGX(GraphLine lineGraph,float xPos){
        float axisWidth = (lineGraph.axis.getXIncrementDistance() * (lineGraph.axis.getNumberOfGraduationsX() - 1) );
        float largestXAxisValue = (lineGraph.axis.getXIncrementValue() * (lineGraph.axis.getNumberOfGraduationsX() - 1) );
        float multiplier = (axisWidth / largestXAxisValue);
        float graphX = (lineGraph.axis.getXGraphZero() + ((xPos - parentGraphLine.axis.getXMin()) * multiplier));
        return graphX;
    }

    private float getGY(GraphLine lineGraph,float yPos){
        float axisHeight = (lineGraph.axis.getYIncrementDistance() * (lineGraph.axis.getNumberOfGraduationsY() - 1) );
        float largestYAxisValue = (lineGraph.axis.getYIncrementValue() * (lineGraph.axis.getNumberOfGraduationsY() - 1) );
        float multiplier = (axisHeight / largestYAxisValue);
        float graphY = (lineGraph.axis.getYGraphZero() - (((yPos != 0)?(yPos - 1):(yPos)) * (multiplier)));
        graphY -= (yPos == 0)? (0):(radiusOriginal);
        return graphY;
    }


    public float getGraphX(){
        return graphX;
    }

    public float getGraphY(){
        return graphY;
    }

    public float getxPos(){
        return this.xPos;
    }
    public float getyPos(){
        return this.yPos;
    }
}
