package project.gui.dataDisplay.dataGraphLine;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;


import java.lang.reflect.Array;
import java.util.ArrayList;



/**
 * Created by Conal on 16/03/2016.
 */
public class GraphLineAxis {

    private PApplet processing;
    private PGraphics screenWindowGraphics;
    private GraphLine parentGraph;
    private float xGraphZero;
    private float yGraphZero;

    private float xAxisLength;
    private float yAxisLength;

    private float lowestYValue;

    private float xMax;
    private float yMax;
    private float yMin;
    private float xMin;


    private String xAxisLabel;
    private String yAxisLabel;
    private String[] busIds;

    private PVector bottomRightPoint;

    private int numberOfGraduationsX;
    private int numberOfGraduationsY;

    private float animationDec;
    private float xIncrementDistance;
    private float yIncrementDistance;

    private int pointRadius;

    private float xIncrementValue;
    private float yIncrementValue;

    private boolean xIsZero;

    private ArrayList<GraphXGrad> xMarkers = new ArrayList<GraphXGrad>();
    private ArrayList<GraphYGrad> yMarkers = new ArrayList<GraphYGrad>();
    //private ArrayList<PVector> graphPoints;

    private ArrayList<ArrayList<PVector>> graphsInput = new ArrayList<ArrayList<PVector>>();
    private ArrayList<ArrayList<GraphLinePoint>> graphs = new ArrayList<ArrayList<GraphLinePoint>>();
    private ArrayList<GraphLinePoint> pointsOnGraph = new ArrayList<GraphLinePoint>();


    public GraphLineAxis(PApplet applet, PGraphics pg, GraphLine parentGraph, int xOrigin, int yOrigin, int xScale, float yScale, int largestX, int largestY, ArrayList<ArrayList<PVector>> points, int pointRad, String[] busIds) {
        this.processing = applet;
        this.screenWindowGraphics = pg;
        this.parentGraph = parentGraph;

        this.xAxisLabel = parentGraph.getXAxisLabel();
        this.yAxisLabel = parentGraph.getYAxisLabel();

        this.xIsZero = false;
        this.bottomRightPoint = new PVector(xOrigin,yOrigin);
        this.xGraphZero = xOrigin;
        this.yGraphZero = yOrigin;

        this.lowestYValue = 0;

        this.pointRadius = pointRad;

        this.busIds = busIds;
        this.animationDec = 0;
        //this.graphPoints = points;
        this.graphsInput = points;

        this.xAxisLength = xScale;
        this.yAxisLength = yScale;

        this.xMax = getLargestXValue(graphsInput);
        this.yMax = getLargestYValue(graphsInput);

        this.xMin = 0;
        this.yMin = 0;


        this.numberOfGraduationsY = 20;
        this.numberOfGraduationsX = 15;

        this.xIncrementValue = (int) ((xMax / numberOfGraduationsX) + 1);
        this.yIncrementValue = (int) ((yMax / numberOfGraduationsY) + 1);

        this.xIncrementDistance = (xScale / numberOfGraduationsX);
        this.yIncrementDistance = (yScale / numberOfGraduationsY);
    }

    public void draw() {
        if(parentGraph.isDrawing()) {
            screenWindowGraphics.stroke(0);

            screenWindowGraphics.stroke(0);
            screenWindowGraphics.line(xGraphZero, yGraphZero, xGraphZero + xAxisLength * animationDec, yGraphZero);
            screenWindowGraphics.line(xGraphZero, bottomRightPoint.y, xGraphZero, bottomRightPoint.y - (yAxisLength * animationDec));

            screenWindowGraphics.text(parentGraph.getXAxisLabel(), xGraphZero + xAxisLength + 50, yGraphZero);
            screenWindowGraphics.text(parentGraph.getYAxisLabel(), xGraphZero, bottomRightPoint.y - yAxisLength - 20);


            for (int i = xMarkers.size() - 1; i >= 0; i--) {
                xMarkers.get(i).draw();
            }

            for (int i = yMarkers.size() - 1; i >= 0; i--) {
                yMarkers.get(i).draw();
            }


            for (int i = graphs.size() - 1; i >= 0; i--) {
                for (int j = graphs.get(i).size() - 1; j >= 0; j--) {
                    graphs.get(i).get(j).draw();

                }

            }

            if (animationDec < 1) {
                animationDec += 0.02;
            } else {
                animationDec = 1;
            }
            drawLines();

            for (int i = graphs.size() - 1; i >= 0; i--) {
                for (int j = graphs.get(i).size() - 1; j >= 0; j--) {

                    graphs.get(i).get(j).showDataWindow.draw();
                }

            }
        }
    }

    private void drawLines() {
        if(parentGraph.isDrawing()) {
            if (graphs.size() > 0) {
                for (int i = graphs.size() - 1; i >= 0; i--) {
                    if (graphs.get(i).size() > 0) {
                        for (int j = graphs.get(i).size() - 1; j > 0; j--) {
                            GraphLinePoint firstPoint = graphs.get(i).get(j);
                            GraphLinePoint secondPoint = graphs.get(i).get(j - 1);
                            if (i == 0) {
                                screenWindowGraphics.stroke(214, 42, 42);
                            } else if (i == 1) {
                                screenWindowGraphics.stroke(129, 235, 103);
                            } else if (i == 2) {
                                screenWindowGraphics.stroke(0, 218, 235);
                            } else if (i == 3) {
                                screenWindowGraphics.stroke(245, 109, 0);
                            } else if (i == 4) {
                                screenWindowGraphics.stroke(57, 112, 240);
                            } else if (i == 5) {
                                screenWindowGraphics.stroke(178, 75, 220);
                            } else if (i == 6) {
                                screenWindowGraphics.stroke(240, 112, 193);
                            } else {
                                screenWindowGraphics.stroke(190, 90, 0);
                            }
                            screenWindowGraphics.strokeWeight(1);
                            screenWindowGraphics.line(firstPoint.getGraphX(), firstPoint.getGraphY(), (firstPoint.getGraphX()) - ((firstPoint.getGraphX() - secondPoint.getGraphX()) * animationDec), secondPoint.getGraphY() + (firstPoint.getGraphY() - secondPoint.getGraphY()) * (1 - animationDec));
                        }
                    }
                }
            }
        }
    }

    public void setData(ArrayList<ArrayList<PVector>> newPoints, String[] busIds){
        this.busIds = busIds;
        this.graphsInput = newPoints;
        //this.graphPoints = newPoints;
        this.xMax = getLargestXValue(newPoints);

        if(!xIsZero){
            this.xMin = getLowestXValue(newPoints);
        }
        else{
            this.xMin = 0;
        }

        this.yMax = getLargestYValue(newPoints);
        this.yMin = getLowestYValue(newPoints);

        this.numberOfGraduationsY = 20;
        this.numberOfGraduationsX = 9;

        this.xIncrementDistance =  (this.xAxisLength / numberOfGraduationsX);
        this.xIncrementValue = (int) (Math.floor((xMax - xMin)/ numberOfGraduationsX) + 1);

        this.yIncrementDistance =  (this.yAxisLength / numberOfGraduationsY);

        if(yMin < 0){
            this.yGraphZero = (int) (bottomRightPoint.y - (this.numberOfGraduationsY * yIncrementDistance)/2);
            if(Math.abs(yMin)  > yMax){
                this.yIncrementValue = (int) (Math.floor((Math.abs(yMin) * 2) / numberOfGraduationsY) + 1);
                this.lowestYValue = yIncrementValue * (numberOfGraduationsY/2) * -1;
            }
            else {
                this.yIncrementValue = (int) (Math.floor((yMax*2) / numberOfGraduationsY) + 1);
                this.lowestYValue = yIncrementValue * (numberOfGraduationsY / 2) * -1;
            }
        }
        else{
            this.yGraphZero = (int) bottomRightPoint.y;
            this.yIncrementValue = (int) (Math.floor(yMax / numberOfGraduationsY) + 1);
            lowestYValue = 0;
        }





        for (int i = xMarkers.size()- 1; i >=0; i--) {
            xMarkers.remove(i);
        }

        for (int i = yMarkers.size() - 1; i >=0; i--) {
            yMarkers.remove(i);
        }

        for (int i = graphs.size() - 1; i >=0; i--) {
            for(int j = graphs.get(i).size() - 1; j >= 0; j--){
                graphs.get(i).remove(j);
            }
        }



        for (int i = 0; i <= numberOfGraduationsX; i++) {
            GraphXGrad tempMarker = new GraphXGrad(processing,screenWindowGraphics,xMin + i * xIncrementValue, xGraphZero + (i * xIncrementDistance), yGraphZero);
            xMarkers.add(tempMarker);
        }

        for (int i = 0; i <= numberOfGraduationsY; i++) {
            GraphYGrad tempMarker = new GraphYGrad(processing,screenWindowGraphics,lowestYValue + i * yIncrementValue, xGraphZero, (float)(bottomRightPoint.y - (i * yIncrementDistance)));
            yMarkers.add(tempMarker);
        }

        graphs = new ArrayList<ArrayList<GraphLinePoint>>();
        for (int i = 0; i < graphsInput.size(); i++) {
            ArrayList<GraphLinePoint> temp = new ArrayList<GraphLinePoint>();
            graphs.add(temp);
            for(int j = 0; j < graphsInput.get(i).size(); j++){
                GraphLinePoint tempPoint = new GraphLinePoint(processing,screenWindowGraphics, (graphsInput.get(i).get(j).x), (graphsInput.get(i).get(j).y), pointRadius, this.parentGraph,i,(busIds.length > i) ? (busIds[i] ): ("null"));
                graphs.get(i).add(tempPoint);
            }

        }
    }




    public int getLargestXValue(ArrayList<ArrayList<PVector>> points){
        int largest = 0;

        for(int i = 0; i < points.size(); i++){

            for(int j = 0; j < points.get(i).size(); j++){

                if(points.get(i).get(j).x > largest){
                    largest = (int)(points.get(i).get(j).x);
                }
            }

        }
        return  largest;
    }

    public int getLargestYValue(ArrayList<ArrayList<PVector>> points){
        int largest = 0;

        for(int i = 0; i < points.size(); i++){
            for(int j = 0; j < points.get(i).size(); j++){

                if(points.get(i).get(j).y > largest){
                    largest = (int)(points.get(i).get(j).y);
                }

            }

        }
        return  largest;
    }

    public int getLowestYValue(ArrayList<ArrayList<PVector>> points){

        int lowest = (int)(points.get(0).get(0).y);
        for(int i = 0; i < points.size(); i++){
            for(int j = 0; j < points.get(i).size();j++){

                    if(points.get(i).get(j).y < lowest){
                        lowest = (int)(points.get(i).get(j).y);
                     }

            }
        }

        return  lowest;
    }


    public int getLowestXValue(ArrayList<ArrayList<PVector>> points){

        int lowest = (int)(points.get(0).get(0).x);
        for(int i = 0; i < points.size(); i++){
            for(int j = 0; j < points.get(i).size();j++){

                if(points.get(i).get(j).x < lowest){
                    lowest = (int)(points.get(i).get(j).x);
                }

            }
        }

        return  lowest;
    }
    public float getXIncrementDistance(){
        return xIncrementDistance;
    }

    public float getYIncrementDistance(){
        return yIncrementDistance;
    }

    public float getXGraphZero(){
        return xGraphZero;
    }

    public float getYGraphZero(){
        return yGraphZero;
    }

    public int getNumberOfGraduationsX(){
        return numberOfGraduationsX;
    }

    public int getNumberOfGraduationsY(){
        return numberOfGraduationsY;
    }

    public float getXIncrementValue(){
        return  xIncrementValue;
    }

    public float getYIncrementValue(){
        return yIncrementValue;
    }

    public float getXMin(){ return xMin;}

}
