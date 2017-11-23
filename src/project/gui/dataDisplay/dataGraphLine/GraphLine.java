package project.gui.dataDisplay.dataGraphLine;

import processing.core.*;
import project.controllers.Controller;
import project.gui.*;
import project.gui.dataDisplay.DataDisplayEnum;
import project.gui.dataDisplay.DataDisplayI;
import project.gui.dataDisplay.dataGraphBar.GraphBarAxis;
import project.gui.dataDisplay.dataMap.Map;
import project.model.ModelMain;
import project.model.exceptions.DataNotFoundException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GraphLine implements AnimateI, DataDisplayI {

    private static final DataDisplayEnum TYPE = DataDisplayEnum.GRAPH_LINE;
    //this is used to know if you want to listen for clicks/other mouse events
    // (talk to Eddie if you want, its one method to implement if you want)
    private static final boolean INTERACTIVE = false;

    PApplet processing;
    PGraphics graphLineWindow;

    GuiStatusEnum currentStatus;
    GuiAnimationEnum currentAnimation;

    int xMax,yMax,pointRadius,numberOfPoints;
    ArrayList<PVector> graphLinePoints = new ArrayList<PVector>();

    private ArrayList<ArrayList<PVector>> graphLines = new ArrayList<ArrayList<PVector>>();

    private String xAxisLabel;
    private String yAxisLabel;
    private String[] deafaultIDs = {"null","null"};

    private boolean isDrawing;

    ArrayList<PVector> animatingGraphLinePoints = new ArrayList<PVector>();
    boolean[] arePointsAnimating;
    int pointsDone = 0;
    public GraphLineAxis axis;


    public GraphLine(PApplet p, PGraphics pg,String xAxisLabel,String yAxisLabel){

        this.processing = p;
        this.graphLineWindow = pg;
        this.pointRadius = 7;
        currentStatus = GuiStatusEnum.LOADING;
        currentAnimation = GuiAnimationEnum.REVEAL_GRAPH_LINE;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.isDrawing = true;
        initialiseGraph();

    }

    public GraphLine(PApplet p, PGraphics pg){
        this(p,pg,"Time: Hours","Delay in seconds");

    }

    private void initialiseGraph(){
        xMax = 500;
        yMax = 50;

        int axisWidth = (int)(graphLineWindow.width*0.8);
        int axisOriginX = (int) (graphLineWindow.width*0.1);
        float axisHeight = (float) (graphLineWindow.height*0.8);
        int axisOriginY = (int) (graphLineWindow.height - (graphLineWindow.height*0.1));
        axis = new GraphLineAxis(processing, graphLineWindow,this, axisOriginX, axisOriginY, axisWidth, axisHeight, xMax, yMax, graphLines, pointRadius,deafaultIDs);

        //---Generate Random Data
        for(int i = 0;i < 4;i++){
            ArrayList<PVector> temp = new ArrayList<PVector>();
            graphLines.add(temp);
                for(int j = 0; j < 10;j++) {
                    graphLines.get(i).add(new PVector(j*5 + 10,(int)((-50 * Math.random()) + (50 * Math.random()))));
                }
        }

        //----------------------
        //getData();  //Made default query
        /*
        for(int i =     0; i < graphLinePoints.size(); i++){
            animatingGraphLinePoints.add(new PVector((int)graphLinePoints.get(i).x, 0));
        }
        arePointsAnimating = new boolean[animatingGraphLinePoints.size()];
        for(int i = 0; i < arePointsAnimating.length; i++){
            arePointsAnimating[i] = true;
        }
        //----------------*/
        axis.setData(graphLines,deafaultIDs);
        //currentAnimation = GuiAnimationEnum.REVEAL_GRAPH_LINE;
        //currentStatus = GuiStatusEnum.ANIMATING;
        //GuiAnimationController.add(this);

    }

    public void draw(){
        axis.draw();
    }

    public void update(){
        //add updates here, eg info boxes
    }

    public void animate(){
        for(int i = 0; i < animatingGraphLinePoints.size(); i++){
            if(arePointsAnimating[i]) {
                int dy = (int) ((graphLinePoints.get(i).y - animatingGraphLinePoints.get(i).y) * 0.1);
                animatingGraphLinePoints.get(i).y += dy;

                if((animatingGraphLinePoints.get(i).y > graphLinePoints.get(i).y && dy>0) || (animatingGraphLinePoints.get(i).y < graphLinePoints.get(i).y && dy < 0)) {
                    arePointsAnimating[i] = false;
                    animatingGraphLinePoints.get(i).y = graphLinePoints.get(i).y;
                    pointsDone++;
                }
            }
            //axis.setData(animatingGraphLinePoints);
        }
        //setData(animatingGraphLinePoints);
        if(pointsDone >= graphLinePoints.size()){
            setStatus(GuiStatusEnum.READY);
        //    setData(graphLinePoints);
        }
    }

    public void setAnimation(GuiAnimationEnum animation){
        this.currentAnimation = animation;
    }

    public GuiAnimationEnum getAnimation(){
        return this.currentAnimation;
    }

    public void setStatus(GuiStatusEnum status){
        this.currentStatus = status;
    }

    public GuiStatusEnum getStatus(){
        return this.currentStatus;
    }

    /**
     * This is called when the query button for this is made
     * gets relevant data from its side bars and gets the data it needs
     * @author Conal
     * @author eddie
     */
    public void getData(){
        String bus;
        int day;
        int points;
        String[] buses = null;
        this.isDrawing = false;
        switch (Controller.currentQuery) {
            case DELAY_GRAPH_LINE:
            try {
                buses = GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(0).widgets.get(1).getString().split(" ");
                day = GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(0).widgets.get(2).getInt();
                points = GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(0).widgets.get(3).getInt();
            } catch (NullPointerException e) {
                buses = new String[3];
                buses[0] = "25B";
                buses[1] = "16";
                buses[2] = "9";
                day = 16;
                points = 75;
            }
            try {
                this.graphLines = new ArrayList<ArrayList<PVector>>();
                for(String buss : buses) {
                    this.graphLines.add(ModelMain.getBusDelays(buss, day, points));  // ****change graphLinePoints to graphLines******
                }
                this.xAxisLabel = "Time: Hour Of Day";
                this.yAxisLabel = "Delay: seconds";
                axis.setData(graphLines,buses);
            } catch (DataNotFoundException e) {
                System.out.println(e.getMessage() + ": Using previous data");
            }
            break;
            case DELAYS_BY_TIME_OF_DAY:
                buses = GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(3).widgets.get(1).getString().split(" ");
                day = GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(3).widgets.get(2).getInt();
                int direction = GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(3).widgets.get(3).getInt();
                this.graphLines = new ArrayList<ArrayList<PVector>>();
                for(String buss : buses) {

                    this.graphLines.add(ModelMain.getLengthJourneyTimes(buss, day, direction));
                }
                this.xAxisLabel = "Time: Hour Of Day";
                this.yAxisLabel = "Length: minutes";
                axis.setData(graphLines,buses);
                break;
        }
        this.isDrawing = true;
    }

    public String getXAxisLabel(){
        return xAxisLabel;
    }

    public String getYAxisLabel(){
        return yAxisLabel;
    }

    public boolean isDrawing(){
        return isDrawing;
    }

    /**
     * returns the typeClock of data display this is (used for controller purposes)
     * @return the typeClock of this data display
     * @Conal Conal , eddie
     */
    public DataDisplayEnum getType(){
        return TYPE;
    }

    /**
     * returns if this data display is interactive or not
     * (so the screen knows whether to include this in a listening list)
     * @return if interactive
     * @authors Conal , eddie
     */
    public boolean isInteractive(){
        return INTERACTIVE;
    }


}
