package project.gui.dataDisplay.dataGraphBar;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

/**
 * Created by Conal on 28/03/2016.
 */
public class GraphBarAxis {

    private PApplet processing;
    private PGraphics graphBarWindow;

    private int xAxisXPos;
    private int xAxisYPos;
    private int yAxisXPos;
    private int yAxisYPos;

    private String yLabel;
    private String xLabel;

    private int xAxisWidth;
    private float yAxisHeight;

    private int numberOfBars;
    private int barWidth;
    private int spaceBetweenBars;
    private int numberOfSpaces;

    private int numberOfYDivisions;
    private int largestYDivision;
    private int yDivisionValue;
    private int largestY;


    private ArrayList<DataPointBarChart> dataPoints;
    public ArrayList<GraphBarElement> barElements;
    private ArrayList<GraphBarYGrad> yAxisGradients;

    public GraphBarAxis(PApplet p, PGraphics pg, ArrayList<DataPointBarChart> data , String yLabel , String xLabel){
        this.processing = p;
        this.graphBarWindow = pg;

        this.numberOfYDivisions = 15;

        this.xAxisWidth = (int)(graphBarWindow.width*0.8);
        this.xAxisXPos = (int) (graphBarWindow.width*0.1);
        this.xAxisYPos = (int) (graphBarWindow.height - (graphBarWindow.height*0.1));

        this.yLabel = yLabel;
        this.xLabel = xLabel;

        this.yAxisHeight = (int) (graphBarWindow.height*0.8);
        this.yAxisXPos = (int) (graphBarWindow.width*0.1);
        this.yAxisYPos = (int) (graphBarWindow.height - (graphBarWindow.height*0.1));

        this.barElements = new ArrayList<GraphBarElement>();
        this.yAxisGradients = new ArrayList<GraphBarYGrad>();

        setData(data);

    }

    public void setData(ArrayList<DataPointBarChart> newData){
        this.dataPoints = newData;
        this.numberOfBars = dataPoints.size();
        this.numberOfSpaces = numberOfBars + 1;
        this.largestY = getLargestY(newData);

        double divisor = xAxisWidth / ( (numberOfBars * 1.2) + 1 );
        this.barWidth = (int) (divisor);
        this.spaceBetweenBars = barWidth/5;


        yDivisionValue = (largestY  / (numberOfYDivisions - 1)) + 1;
        largestYDivision =  yDivisionValue * (numberOfYDivisions - 1);

        ArrayList<GraphBarElement> bars = new ArrayList<GraphBarElement>();
        for(int i = 0; i < dataPoints.size(); i++){
            int xPosition = xAxisXPos + ((i * barWidth) + ((i + 1) * spaceBetweenBars));
            int yPosition = yAxisYPos;
            int width = barWidth;
            float height = (yAxisHeight / largestYDivision) * dataPoints.get(i).dataValue;
            System.out.println("yAxisHeight = " + yAxisHeight + "largestYDivision" + largestYDivision);
            System.out.println("Bar " + i + "height =  " + height);
            GraphBarElement bar = new GraphBarElement(processing,graphBarWindow,dataPoints.get(i),xPosition,yPosition,width,height);
            bars.add(bar);
        }

        ArrayList<GraphBarYGrad> gradiations = new ArrayList<>();
        for(int i = 0; i < numberOfYDivisions; i++){
            GraphBarYGrad temp = new GraphBarYGrad(this.processing,this.graphBarWindow,i * this.yDivisionValue,this.yAxisXPos,this.yAxisYPos - ((i) * (this.yAxisHeight/(numberOfYDivisions - 1))));
            gradiations.add(temp);
        }

        barElements = bars;
        yAxisGradients = gradiations;


    }

    public void draw(){
        graphBarWindow.stroke(0);
        graphBarWindow.line(xAxisXPos,xAxisYPos,xAxisXPos + xAxisWidth,xAxisYPos);
        graphBarWindow.line(yAxisXPos,yAxisYPos,yAxisXPos,yAxisYPos - yAxisHeight);

        graphBarWindow.text(xLabel,xAxisXPos + (xAxisWidth/2),yAxisYPos +50);
        graphBarWindow.text(yLabel,xAxisXPos,yAxisYPos - yAxisHeight - 20);

        for(int i = 0; i < barElements.size(); i++){
            if((i  % 2 == 0)) {

                graphBarWindow.stroke(0);
                graphBarWindow.fill(255);
            }
            else{
                graphBarWindow.stroke(0);
                graphBarWindow.fill(235);
            }

                barElements.get(i).draw();
        }

        for(int i = 0; i < numberOfYDivisions; i++){
            yAxisGradients.get(i).draw();
        }

    }

    public int getLargestY(ArrayList<DataPointBarChart> data){
        int largestY = 0;
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).dataValue > largestY){
                largestY = data.get(i).dataValue;
            }
        }
        return largestY;
    }
}
