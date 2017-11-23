package project.gui.dataDisplay.RouteExplorer;

import processing.core.*;
import processing.event.MouseEvent;
import project.gui.GuiEventsEnum;
import project.gui.GuiMainController;
import project.gui.ScreenNames;
import project.gui.dataDisplay.DataDisplayEnum;
import project.gui.dataDisplay.DataDisplayI;
import project.gui.dataDisplay.InteractiveDisplayI;
import project.gui.dataDisplay.dataMap.Map;
import project.gui.guiController.GuiEventController;
import project.gui.widget.Widget;
import project.model.ModelMain;
import project.model.exceptions.DataNotFoundException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Main class for Route Explorer , All logic stems from here
 * @author Eddie
 */
public class RouteExplorer implements DataDisplayI,InteractiveDisplayI {

    //---Constants---
    private static final DataDisplayEnum TYPE = DataDisplayEnum.ROUTE_EXPLORER;
    private static final boolean INTERACTIVE = true;

    private static final int STROKE_NORMAL = 200;
    private static final int STROKE_WEIGHT_SELECTED = 2;

    private static final int RADIUS_NORMAL = 20;
    private static final int MARGIN = RADIUS_NORMAL*2;
    private static final int RADIUS_HOVERED = (int) (RADIUS_NORMAL*1.25f);
    private static final int RADIUS_SELECTED = RADIUS_NORMAL*5;
    private static final int RADIUS_SELECTED_HOVERED = (int) (RADIUS_SELECTED*0.75f);
    private static final int WIDTH_NORMAL = 100;
    private static final int WIDTH_HOVERED = 110;
    private static final int HEIGHT_NORMAL = 30;
    private static final int HEIGHT_HOVERED = 40;
    private static final int SPACING = RADIUS_NORMAL/2;
    private static final int SELECTED_X_POS = 200;
    private static final int SELECTED_Y_POS = 200;

    //---variables---
    private PApplet processing;
    private PGraphics screen;
    private String busId;
    private REBubbleStop[] stops;
    private RERectButton[] delayButtons;
    protected static PFont arialBold;
    protected static PFont arial;
    private REBubbleStop selectedStop;
    private REBubbleBus selectedBus;
    private RERectImageButton graphButton;
    private RERectImageButton mapButton;
    private RERectImageButton mapMainButton;
    private REBubbleImageButton[] busBubbleButtons;
    private RouteExplorerRequestTypes requestType;
    private REInternalEvents clockType;
    private int previousSelectedStop;
    private String previousSelectedBus;
    private int routeStart1;
    private int routeStart2;
    private int routeEnd1;
    private int routeEnd2;
    private int width1;
    private int width2;
    private int height;
    private int direction = 0;
    protected PImage barChartImage;
    protected PImage mapImage;
    protected PImage tableImage;
    protected RESlider sliderClock;
    protected RESlider sliderBus;
    protected RERectImageButton journeyTimeButton0;
    protected RERectImageButton journeyTimeButton1;

    //---constructor---

    /**
     * Creates a Route Explorer
     * @param processing PAppleter to use
     * @param screen screen to draw on
     * @param busId id of the bus to initially use
     * @param stops1 the stops for direction one of the bus
     * @param stops2 the stops for direction two of the bus
     */
    public RouteExplorer(PApplet processing , PGraphics screen ,String busId , int[] stops1 , int[] stops2){
        this.processing = processing;
        this.arialBold = processing.createFont("Arial Bold",14,true);
        this.arial = processing.createFont("Arial",12,true);
        this.screen = screen;
        this.busId = busId;
        this.width1 = screen.width/2 - MARGIN;
        this.width2 = width1;
        this.height = screen.height - MARGIN;
        this.stops = toStopBubbles(stops1,stops2);
        this.previousSelectedStop = -1;
        this.requestType = RouteExplorerRequestTypes.BUS;
        delayButtons = new RERectButton[3];
        delayButtons[0] = new RERectButton(processing,screen,"Minimum",375,650,WIDTH_NORMAL,HEIGHT_NORMAL, REInternalEvents.MIN,2);
        delayButtons[1] = new RERectButton(processing,screen,"Average",500,650,WIDTH_NORMAL,HEIGHT_NORMAL, REInternalEvents.AVG,2);
        delayButtons[2] = new RERectButton(processing,screen,"Maximum",625,650,WIDTH_NORMAL,HEIGHT_NORMAL, REInternalEvents.MAX,2);
        this.barChartImage = processing.loadImage("data/images/barGraphButton.png");
        this.mapImage = processing.loadImage("data/images/mapButton.png");
        this.tableImage = processing.loadImage("data/images/tableButton.png");
        this.mapMainButton = new RERectImageButton(processing,screen,mapImage,width1-5,180,GuiEventsEnum.MAP_QUERY_VIEW_ROUTE);
        this.graphButton = new RERectImageButton(processing,screen,barChartImage,70,400, GuiEventsEnum.BUSES_AT_A_STOP);
        this.mapButton = new RERectImageButton(processing,screen,mapImage,70,500,GuiEventsEnum.MAP_QUERY_VIEW_ROUTES);
        busBubbleButtons = new REBubbleImageButton[2];
        busBubbleButtons[0] = new REBubbleImageButton(processing,screen,mapImage,1150,150,mapImage.width,GuiEventsEnum.MAP_QUERY_VIEW_ROUTE);
        busBubbleButtons[1] = new REBubbleImageButton(processing,screen,tableImage,1150,260,tableImage.width,GuiEventsEnum.VIEW_ARRIVAL_TIMES);
        sliderClock = new RESlider(processing,screen,null,"Date To View",375-WIDTH_NORMAL/2,675,WIDTH_NORMAL*3 + WIDTH_NORMAL/2,30,1,30);
        sliderBus = new RESlider(processing,screen,null,"Lengths Of Journey Times",400,650,WIDTH_NORMAL*3 + WIDTH_NORMAL/2,30,1,30);
        journeyTimeButton0 = new RERectImageButton(processing,screen,barChartImage,width1-1,270,GuiEventsEnum.DELAYS_BY_TIME_OF_DAY);
        journeyTimeButton1 = new RERectImageButton(processing,screen,barChartImage,width1-1,360,GuiEventsEnum.DELAYS_BY_TIME_OF_DAY);
    }

    /**
     * Handles what it should draw
     */
    public void draw(){
        screen.textFont(arial);
        screen.strokeWeight(2);
        //if no selected stop
        if(selectedStop == null){
            //draw labels
            screen.fill(0);
            screen.textAlign(PConstants.LEFT , PConstants.CENTER);
            screen.textFont(arialBold);
            screen.text( "Direction 1 : " +routeStart1+" → "+routeEnd1 , MARGIN*2 , MARGIN);
            screen.text( "Direction 2 : " +routeStart2+" → "+routeEnd2 , width1+(MARGIN*2) , MARGIN);
            float lastSize = screen.textSize;
            screen.textSize(25);
            screen.textAlign(PConstants.CENTER , PConstants.CENTER);
            screen.text(busId,width1-5,100);
            screen.textSize(lastSize);

            //draw all stops
            screen.textFont(arial);
            for(REBubbleStop stop: stops) {
                if (stop.isMouseOver)  {screen.stroke(0,0,150);}
                else if((stop.direction == 0 && journeyTimeButton0.isMouseOver)
                        || (stop.direction == 1 && journeyTimeButton1.isMouseOver)) {

                    screen.stroke(0,200,200);
                } else {
                    screen.stroke(STROKE_NORMAL);
                }
                if(stop.stopNumber == previousSelectedStop) {
                    screen.fill(225,255,0);
                }else{
                    screen.fill(220);
                }
                stop.draw();
            }

            //draw journey time button
            if(journeyTimeButton0.isMouseOver){
                screen.stroke(0,0,150);
            }else{
                screen.stroke(STROKE_NORMAL);
            }
            screen.fill(220);
            journeyTimeButton0.draw();

            if(journeyTimeButton1.isMouseOver){
                screen.stroke(0,0,150);
            }else{
                screen.stroke(STROKE_NORMAL);
            }
            screen.fill(220);
            journeyTimeButton1.draw();

            //draw average time slider
            if(journeyTimeButton0.isMouseOver || journeyTimeButton1.isMouseOver){
                screen.stroke(0,0,150);
            }
            sliderBus.draw();

            //draw main map button
            screen.fill(220);
            if(mapMainButton.isMouseOver){
                screen.stroke(0, 0, 150);
            }else {
                screen.stroke(STROKE_NORMAL);
            }
            mapMainButton.draw();

            //else there is a selected stop so draw {
        }else{
            //draw sliderClock
            sliderClock.draw();

            //draw stem of selectedStop
            screen.fill(225,225,0);
            screen.strokeWeight(2);
            screen.stroke(20);
            screen.rect(SELECTED_X_POS-5,SELECTED_Y_POS,10,screen.height - SELECTED_Y_POS);

            //draw the stop numbers in the middle of stem
            char[] stopNumbers = Integer.toString(selectedStop.stopNumber).toCharArray();
            screen.fill(20);
            screen.rect(SELECTED_X_POS-8,SELECTED_Y_POS+ (screen.height - SELECTED_Y_POS)/2,16,stopNumbers.length*screen.textFont.getSize()+5,4);
            screen.fill(225,225,0);
            for(int i = 0;i < stopNumbers.length ; i++){
                screen.text(stopNumbers[i],SELECTED_X_POS,SELECTED_Y_POS+ (screen.height - SELECTED_Y_POS)/2 + 5 + (i)*(screen.textFont.getSize()+1));
            }

            //only draw selected stop
            screen.fill(225,225,0);
            screen.strokeWeight(4);
            screen.textFont(arialBold);
            if (selectedStop.isMouseOver) {
                screen.stroke(0, 0, 150);
            } else {
                screen.stroke(20);
            }
            selectedStop.draw();

            //draw clocks if there are any
            if(selectedStop.currentClock != null){
                selectedStop.currentClock.draw();
            }

            //draw buttons
            screen.strokeWeight(2);
            for(RERectButton button: delayButtons){
                if(button.isMouseOver){
                    screen.stroke(0,0,150);
                }else{
                    screen.stroke(50);
                }
                switch (button.typeClock){
                    case MIN: screen.fill(0,150,150); break;
                    case AVG: screen.fill(0,150,0); break;
                    case MAX: screen.fill(150,0,0); break;
                }
                button.draw();
            }

            //draw its visiting buses
            screen.textFont(arialBold);
            screen.fill(0);
            screen.text("Buses That Visit " + selectedStop.stopNumber,820,50);
            screen.strokeWeight(2);
            screen.textFont(arial);
            for(REBubbleBus busBubble : selectedStop.busesThatVisit){
                screen.stroke(20);
                if(busBubble.isMouseOver){
                    screen.stroke(0,0,150);
                }else {
                    screen.stroke(20);
                }
                if(busBubble.isSelected()){
                    screen.fill(20,155,20);
                }else{
                    screen.fill(200);
                }
                busBubble.draw();
            }

            //draw bus option bubbles
            if(selectedBus != null){
                for(REBubbleImageButton button : busBubbleButtons){
                    if(button.isMouseOver){
                        screen.stroke(0,0,150);
                    }else{
                        screen.stroke(20);
                    }
                    screen.fill(144,212,176);
                    button.draw();
                }
            }

            //draw graph image button
            if(graphButton.isMouseOver){
                screen.stroke(0,0,150);
            }else {
                screen.stroke(20);
            }
            screen.fill(200);
            graphButton.draw();

            //draw map Image button
            if(mapButton.isMouseOver){
                screen.stroke(0,0,150);
            }else {
                screen.stroke(20);
            }
            screen.fill(200);
            mapButton.draw();
        }
    }

    /**
     * Handles what should be updated and tells them to update
     */
    public void update() {

        //cycle through each stop and update
        for(REBubbleStop stop : stops){
            if(selectedStop == stop){
                //update selected stop
                if(stop.isMouseOver){
                    stop.newSize(RADIUS_SELECTED_HOVERED);
                }else{
                    stop.newSize(RADIUS_SELECTED);
                }
            }else{
                //update all stops
                if(stop.isMouseOver){
                    stop.newSize(RADIUS_HOVERED);
                }else {
                    stop.newSize(RADIUS_NORMAL);
                }
            }
            stop.update();
        }

        //update average time button
        if(journeyTimeButton0.isMouseOver) {
            journeyTimeButton0.newSize(barChartImage.width-20,barChartImage.height-20);
        }else{
            journeyTimeButton0.newSize(barChartImage.width-10,barChartImage.height-10);
        }
        journeyTimeButton0.update();

        if(journeyTimeButton1.isMouseOver) {
            journeyTimeButton1.newSize(barChartImage.width-20,barChartImage.height-20);
        }else{
            journeyTimeButton1.newSize(barChartImage.width-10,barChartImage.height-10);
        }
        journeyTimeButton1.update();

        //update journeyTime slider
        sliderBus.update();

        //update main map button
        if(mapMainButton.isMouseOver){
            mapMainButton.newSize(mapImage.width+10,mapImage.height+10);
        }else{
            mapMainButton.newSize(mapImage.width,mapImage.height);
        }
        mapMainButton.update();

        //if there is a selected stop
        if(selectedStop!= null){

            //update currentClock of selectedStop
            if(selectedStop.currentClock != null) {
                selectedStop.currentClock.update();
            }

            //update delay buttons
            for(RERectButton button:delayButtons){
                if(button.isMouseOver){
                    button.newSize(WIDTH_HOVERED,HEIGHT_HOVERED);
                }else{
                    button.newSize(WIDTH_NORMAL,HEIGHT_NORMAL);
                }
                button.update();
            }

            //update busBubbles
            if(selectedStop.busesThatVisit != null) {
                for (REBubbleBus busBubble : selectedStop.busesThatVisit) {
                    if(busBubble != null) {
                        if (busBubble.isMouseOver) {
                            busBubble.newSize(RADIUS_HOVERED);
                        } else {
                            busBubble.newSize(RADIUS_NORMAL);
                        }
                        busBubble.update();
                    }
                }
            }

            //update bus bubble options
            for(REBubbleImageButton button : busBubbleButtons){
                if(button.isMouseOver){
                    button.newSize(40+10);
                }else {
                    button.newSize(40);
                }
                button.update();
            }


            //update graph image button
            if(graphButton.isMouseOver){
                graphButton.newSize(barChartImage.width+10,barChartImage.height+10);
            }else{
                graphButton.newSize(barChartImage.width,barChartImage.height);
            }
            graphButton.update();

            //update secondary map button
            if(mapButton.isMouseOver){
                mapButton.newSize(mapImage.width+10,mapImage.height+10);
            }else{
                mapButton.newSize(mapImage.width,mapImage.height);
            }
            mapButton.update();

            //update slider of the clock
            sliderClock.update();
        }
    }

    /**
     * Handles when a mouse event occurs
     * @param mouseEvent the event that happened
     */
    public void mouseEventOccurred(int mouseEvent){
        boolean eventFound = false;
        if(mouseEvent == MouseEvent.RELEASE ) {

            //if there is a selected stop
            if (selectedStop != null) {

                //check buttons
                for (RERectButton button : delayButtons) {
                    if (button.isMouseOver()) {
                        button.clicked();
                        eventFound = true;
                        //if the clock for this button isn't loaded
                        if (selectedStop.getClockForEnum(button.typeClock) == null) {
                            //set current Clock for the stop
                            selectedStop.type = button.typeClock;
                            selectedStop.setClockFromEnum(button.typeClock);

                            //prepare for controller query
                            requestType = RouteExplorerRequestTypes.CLOCK;
                            clockType = button.typeClock;
                            getData();
                            break;

                            //the clock is loaded
                        } else {
                            //set stop current clock to this button and reset animation
                            selectedStop.setClockFromEnum(button.typeClock);
                            selectedStop.currentClock.resetAnimation();
                            break;
                        }
                    }
                }

                //check busBubbles
                if (selectedStop.busesThatVisit != null && !eventFound) {
                    for (REBubbleBus busBubble : selectedStop.busesThatVisit) {
                        if (busBubble.isMouseOver()) {
                            eventFound = true;
                            //if already selected
                            if (busBubble.isSelected()) {
                                previousSelectedStop = selectedStop.stopNumber;
                                previousSelectedBus = busBubble.label;
                                //prepare for controller query
                                requestType = RouteExplorerRequestTypes.BUS;
                                GuiMainController.currentNavSideBar.getMenuItem(0).widgets.get(1).setString(busBubble.label);
                                getData();
                                break;
                            } else {
                                //not already selected
                                busBubble.clicked();
                                if (selectedBus != null) {
                                    selectedBus.selected = false;
                                    selectedBus = null;
                                }
                                selectedBus = busBubble;
                                //make busBubble options reanimate
                                if (busBubbleButtons != null) {
                                    for (REBubbleImageButton busOption : busBubbleButtons) {
                                        busOption.radiusCurrent = 0;
                                        busOption.newSize(50);
                                    }
                                }

                            }
                        }
                    }
                }

                //check selected stop
                if (selectedStop.isMouseOver() && !eventFound) {
                    selectedStop.clicked(); //if currently selected stop is clicked
                    eventFound = true;
                    selectedStop.newDestination(selectedStop.xOriginal, selectedStop.yOriginal);
                    previousSelectedStop = selectedStop.stopNumber;
                    selectedStop = null;
                }

                //check graphButton
                if (graphButton.isMouseOver() && !eventFound) {
                    eventFound = true;
                    switchScreen(graphButton.type);
                }

                //check mapButton
                if (mapButton.isMouseOver() && !eventFound) {
                    eventFound = true;
                    mapButton.isMouseOver = false;
                    Map.stopToDisplay = selectedStop.stopNumber;
                    switchScreen(mapButton.type);
                }

                //check bus option
                if (!eventFound){
                    for (REBubbleImageButton button : busBubbleButtons) {
                        if (button.isMouseOver()) {
                            eventFound = true;
                            switchScreen(button.type);
                            break;
                        }
                    }
                }

            } else {
                //check all stops
                for (REBubbleStop stop : stops) {
                    if (stop.isMouseOver()) {
                        //logic for Bubble
                        stop.clicked();
                        eventFound = true;
                        selectedStop = stop;
                        stop.newDestination(SELECTED_X_POS,SELECTED_Y_POS);

                        //queryLogic
                        if(stop.currentClock == null) {
                            selectedStop.type = REInternalEvents.AVG;
                            requestType = RouteExplorerRequestTypes.CLOCK;
                            clockType = stop.type;
                            getData();
                            requestType = RouteExplorerRequestTypes.STOP_BUSES;
                            getData();
                        }else{
                            stop.currentClock.resetAnimation();
                        }
                        break;
                    }
                }

                //check journey time button0
                if(!eventFound){
                    if(journeyTimeButton0.isMouseOver()){
                        direction = 0;
                        switchScreen(journeyTimeButton0.type);
                    }
                }

                //check journey time button1
                if(!eventFound){
                    if(journeyTimeButton1.isMouseOver()){
                        direction =1;
                        switchScreen(journeyTimeButton1.type);
                    }
                }

                //check main Map button
                if(!eventFound){
                    if(mapMainButton.isMouseOver()){
                        switchScreen(mapMainButton.type);
                    }
                }
            }

        }
    }

    /**
     * Retrieves the data needed based on what is set
     */
    public void getData() {
        switch (requestType) {
            case BUS:
                try {
                    String newBusId = GuiMainController.getNavSideBar(ScreenNames.ROUTE_EXPLORER).getMenuItem(0).widgets.get(1).getString();
                    stops = toStopBubbles(ModelMain.getBus(newBusId).getStops0(), ModelMain.getBus(newBusId).getStops1()); //get stops
                    busId = newBusId;

                    //if there was a previously selectedStop
                    if(selectedStop != null) {
                        boolean getsToSameStop = false;
                        for (REBubbleStop stop : stops) {
                            if (stop.stopNumber == previousSelectedStop) {
                                selectedStop = stop;
                                getsToSameStop = true;
                                break;
                            }
                        }
                        if(getsToSameStop) {
                            //get visiting buses for this stop
                            requestType = RouteExplorerRequestTypes.STOP_BUSES;
                            getData();
                            selectedStop.newDestination(SELECTED_X_POS, SELECTED_Y_POS);
                            requestType = RouteExplorerRequestTypes.CLOCK;
                            clockType = REInternalEvents.AVG;
                            getData();

                            //doesnt work , meant to try keep the same bus selected
                            for(REBubbleBus bus : selectedStop.busesThatVisit){
                                if(bus.label.equals(previousSelectedBus)){
                                    System.out.print("found match");
                                    selectedBus = bus;
                                    selectedBus.selected = true;
                                    break;
                                }
                            }
                            //else theres no same stops so reset to main RouteExplorer screen
                        }else{
                            selectedStop = null;
                            selectedBus = null;
                        }

                        //reset request type
                        requestType = RouteExplorerRequestTypes.BUS;

                    }
                } catch (DataNotFoundException e) {
                    GuiMainController.getNavSideBar(ScreenNames.ROUTE_EXPLORER).getMenuItem(0).widgets.get(1).makeError();
                }
                break;
            case CLOCK:
                try{
                    int day = sliderClock.getInt();
                    if(day == 0) {
                        day = 6;
                    }
                    selectedStop.currentClock = new REClock(
                            processing,
                            screen,
                            ModelMain.getClockDelayForBusStop(busId,selectedStop.stopNumber,day,clockType),
                            clockType.toString() + " delay at stop " + selectedStop.stopNumber + " for " + busId + " on " + toDate(ModelMain.dayToTimeStamp(day)),
                            400,80,100, //x,y,radius
                            false
                            ) ;
                    requestType = RouteExplorerRequestTypes.BUS;
                }catch (DataNotFoundException e){
                    System.out.println(e.getMessage());
                }
                break;
            case STOP_BUSES:
                try{
                    selectedStop.createBusBubbles(ModelMain.getBusStop(selectedStop.stopNumber).getVisitingBuses());
                    requestType = RouteExplorerRequestTypes.BUS;
                }catch (DataNotFoundException e){
                    System.out.println(e.getMessage());
                }
                break;
        }
    }

    /**
     * Converts the int[] arrays into stop Bubbles that are interactive
     * @param stops1 stops for direction 1
     * @param stops2 stops for direction 2
     * @return an array of all the stop bubbles , positioned correctly
     */
    private REBubbleStop[] toStopBubbles(int[] stops1 , int[] stops2){
        REBubbleStop[] returnStops = new REBubbleStop[stops1.length + stops2.length];

        //variables for creating the grid of stops
        int columns1 = width1/(2*(RADIUS_NORMAL + SPACING));
        int rows1 = (int) Math.ceil((double) stops1.length/columns1);
        int columns2 = width2/(2*(RADIUS_NORMAL + SPACING));
        int rows2 = (int) Math.ceil((double) stops2.length/columns2);
        this.routeStart1 = stops1[0];
        this.routeStart2 = stops2[0];
        this.routeEnd1 = stops1[stops1.length-1];
        this.routeEnd2 = stops2[stops2.length-1];

        for(int i = 0 ; i < stops1.length ; i++){
            returnStops[i] = new REBubbleStop(
                    processing,
                    screen,
                    Integer.toString(stops1[i]),
                    stops1[i],
                    (int)PApplet.map((i)%columns1,0,columns1+1,MARGIN,width1-MARGIN)+MARGIN,
                    (int)PApplet.map((i)/columns1,0,rows1+1,MARGIN,height)+MARGIN,
                    RADIUS_NORMAL,
                    0
            );
        }
        for(int i = 0 ; i < stops2.length ; i++){
            returnStops[i+stops1.length] = new REBubbleStop(
                    processing,
                    screen,
                    Integer.toString(stops2[i]),
                    stops2[i],
                    (int)PApplet.map((i)%columns2,0,columns2+1,MARGIN+width1,width1+width2-MARGIN)+MARGIN,
                    (int)PApplet.map((i)/columns2,0,rows2+1,MARGIN,height)+MARGIN,
                    RADIUS_NORMAL,
                    1
            );
        }
        return returnStops;
    }

    /**
     * returns if this is an interactive DataDisplay
     * @return true
     */
    public boolean isInteractive(){
        return INTERACTIVE;
    }

    /**
     * typeClock of DataDisplay this is
     * @return ROUTE
     */
    public DataDisplayEnum getType(){
        return TYPE;
    }

    /**
     * Method for handling setting the input fields for other dataDisplays when they
     * are told to get their data
     * @param event type of GUI_Event that occured
     */
    private void switchScreen(GuiEventsEnum event){
        switch (event) {
            case BUSES_AT_A_STOP:
                GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(2).widgets.get(1).setString(Integer.toString(selectedStop.stopNumber));
                GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(2).widgets.get(2).setInt(sliderClock.getInt());
                GuiEventController.guiEventOccurred(event); //this is a query event to tell the datadisplay to getData()
                GuiEventController.guiEventOccurred(GuiEventsEnum.SWITCH_TO_GRAPHSCREEN);
                break;
            case MAP_QUERY_VIEW_ROUTES:
                StringBuilder sb = new StringBuilder();
                for (REBubbleBus bus : selectedStop.busesThatVisit) {
                    sb.append(bus.label);
                    sb.append(' ');
                }
                sb.deleteCharAt(sb.length() - 1);
                GuiMainController.getNavSideBar(ScreenNames.MAP).getMenuItem(0).widgets.get(1).setString(sb.toString());
                GuiEventController.guiEventOccurred(event);
                GuiEventController.guiEventOccurred(GuiEventsEnum.SWITCH_TO_MAPSCREEN);
                break;
            case MAP_QUERY_VIEW_ROUTE:
                if (selectedStop != null){
                    GuiMainController.getNavSideBar(ScreenNames.MAP).getMenuItem(0).widgets.get(1).setString(selectedBus.label);
                }else{
                    GuiMainController.getNavSideBar(ScreenNames.MAP).getMenuItem(0).widgets.get(1).setString(busId);
                }
                GuiEventController.guiEventOccurred(event);
                GuiEventController.guiEventOccurred(GuiEventsEnum.SWITCH_TO_MAPSCREEN);
                break;
            case DELAYS_BY_TIME_OF_DAY:
                GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(3).widgets.get(1).setString(busId);
                GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(3).widgets.get(2).setInt(sliderBus.getInt());
                GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(3).widgets.get(3).setInt(direction);
                GuiEventController.guiEventOccurred(event);
                GuiEventController.guiEventOccurred(GuiEventsEnum.SWITCH_TO_GRAPHSCREEN);
                break;
            case VIEW_ARRIVAL_TIMES:
                ArrayList<Widget> widgets =   GuiMainController.getNavSideBar(ScreenNames.TABLE).getMenuItem(0).widgets;
                widgets.get(6).setInt(1000);
                widgets.get(1).setString(selectedBus.label);
                widgets.get(2).setString(Integer.toString(selectedStop.stopNumber));
                //set to only display time , delay and at stop
                widgets.get(7).setCheckBox(new boolean[] {true,false,false,false,false,false,false,true,false,true});
                for(boolean b : widgets.get(7).getCheckbox()){
                    System.out.println(b);
                }
                widgets.get(4).setMinMax(1,24);
                widgets.get(5).setMinMax(sliderClock.getInt(),sliderClock.getInt());
                GuiEventController.guiEventOccurred(event);
                GuiEventController.guiEventOccurred(GuiEventsEnum.SWITCH_TO_TABLESCREEN);
                break;
        }
    }

    /**
     * Converts a time stamp to a given string formatted date
     * @param timeStamp timeStamp (long)
     * @return String representation of that timeStamp
     */
    public static String toDate(long timeStamp){
        return new SimpleDateFormat("MMM-dd").format(new Date(timeStamp));
    }
}


