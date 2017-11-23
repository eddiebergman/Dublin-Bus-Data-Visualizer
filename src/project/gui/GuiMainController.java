package project.gui;

//import com.jogamp.opengl.Threading;
import processing.core.*;
import processing.event.MouseEvent;
import project.controllers.Controller;
import project.database.DbColumn;
import project.gui.dataDisplay.RouteExplorer.RouteExplorer;
import project.gui.dataDisplay.dataGraphBar.GraphBarChart;
import project.gui.dataDisplay.dataGraphLine.GraphLine;
import project.gui.dataDisplay.dataMap.Map;
import project.gui.dataDisplay.dataMap.MapBus;
import project.gui.dataDisplay.dataTable.Table;
import project.gui.guiController.*;
import project.gui.navSideBar.*;
import project.gui.screen.*;
import project.gui.widget.*;
import project.model.ModelMain;
import project.model.exceptions.DataNotFoundException;

import java.util.ArrayList;

/**
 * GuiMainController
 * The main object class that will control the gui aspects of the program. It's an object and not a static library because we need to pass the PApplet to each object it creates
 *
 * @author Samuel McCann
 * @since 08/03/2016
 */

public class GuiMainController {

    private PApplet processing;
    private static GuiStatusEnum guiStatus;
    private static GuiAnimationEnum currentAnimation;

    public static ArrayList<Screen> screens;
    public static Screen currentScreen;
    public static Screen animatingScreen;
    private static ArrayList<NavSideBar> navSideBars;
    public static NavSideBar currentNavSideBar;

    public static int currentScreenIndex;

    private final float EASING = 0.15f;

    //----------------- Multi-Thread Variables ---------------------
    //Implemented by Samuel
    // For Each new thread we create that runs an infinite loop, they get their own variable in each of the following variable sets
    public Thread logicThread = new Thread(new LogicThread());
    public Thread mouseReleasedThread = new Thread(new MouseReleasedThread());  //not an infinite loop thread
    public Thread miscThread = new Thread(new MiscThread());

    /*mainDrawMethodThread isn't actually a thread, its the main Draw method processing calls over and over again. I'm going to refer to it as its own thread. Processing determines
    the fps of the Main Draw method, and so we don't need to set a desired framerate for it here.*/
    int frameRateControllerThread = 60; //The desired frameRate of each thread
    int frameRateMiscThread = 1;

    int countMainDrawThreadCalls;     //How many times a thread has ran. Used to calculate fps of thread
    int countControllerThreadCalls = 0;
    int countMiscThreadCalls = 0;

    int countMainDrawThreadCallsOld = 0;   //How many times has the thread run so far. Used to calculate fps of thread
    int countControllerThreadCallsOld = 0;
    int countMiscThreadCallsOld = 0;

    //mainDrawMethod doesn't need a lastCall variable
    int lastCallControllerThread = 0;        //How long ago the last time each thread ran. Used to match threads to program framerate
    int lastCallMiscThread = 0;

    int controllerThreadFPS = 0;
    int mainDrawThreadFPS = 0;

    //-----------------------------------------------------------

    public GuiMainController(PApplet p) {
        this.processing = p;
        currentScreenIndex = 3;
        guiStatus = GuiStatusEnum.LOADING;
    }

    public void setup() {
        initialiseScreens();
        initialiseNavBars();

        logicThread.start();             //initialise the threads last in setup
        miscThread.start();
        System.out.println(Thread.currentThread().getName() + ": The primary program method is running.");
        setStatus(GuiStatusEnum.READY);
    }

    /**
     * initialiseScreens
     * Method to initialise each screen object needed in the program, For now, I've only made the ScreenHome class, so they're all ScreenHome's
     * @author Samuel, Eddie
     * @since 08/03/2016
     */
    private void initialiseScreens() {
        screens = new ArrayList<Screen>();

        //Initialization for the Table Screen
        Screen tableScreen = new Screen(processing);
        String[][] tableData = ModelMain.getTabularDataFor(ModelMain.DAY1,ModelMain.DAY2, 100000,DbColumn.time,DbColumn.busId,DbColumn.stopId,DbColumn.atStop,DbColumn.delay);
        String[] tableLabels = {DbColumn.time.toString(),DbColumn.busId.toString(),DbColumn.stopId.toString(),DbColumn.atStop.toString(), DbColumn.delay.toString()};
        tableScreen.addDataDisplay(new Table(processing, tableScreen.screenWindowGraphics, tableData, tableLabels));
        screens.add(tableScreen);

        //Initialization for the Graph Screen
        Screen graphScreen = new Screen(processing);
        GraphLine graphLine = new GraphLine(processing, graphScreen.screenWindowGraphics);
        Controller.currentQuery = GuiEventsEnum.DELAY_GRAPH_LINE;
        graphLine.getData();
        graphScreen.addDataDisplay(graphLine);

        graphScreen.addDataDisplay(new GraphBarChart(processing,graphScreen.screenWindowGraphics));
        screens.add(graphScreen);

        //Initialization for the Map Screen
        Screen mapScreen = new Screen(processing);
        MapBus[] busData = ModelMain.getTestMapData(new String[] {"14" , "16" , "25A"});//should contain [busId = stopNumber , route.length = 1 , route[0] = long/lat]
        mapScreen.addDataDisplay(new Map(processing, mapScreen.screenWindowGraphics, busData));
        screens.add(mapScreen);


        //Initialization for the Route Screen
        Screen routeScreen = new Screen(processing);
        try {
            routeScreen.addDataDisplay(new RouteExplorer(processing,routeScreen.screenWindowGraphics,"14",ModelMain.getBus("14").getStops0(),ModelMain.getBus("14").getStops1()));
        }catch (DataNotFoundException e){

            System.out.println(e.getMessage());
        }
        screens.add(routeScreen);

        currentScreen = screens.get(currentScreenIndex);
    }

    /**
     * initialiseScreens
     * Method to initialise each navSideBar object needed in the program.
     * @author Samuel, Eddie
     * @since 08/03/2016
     */
    private void initialiseNavBars() {
        navSideBars = new ArrayList<NavSideBar>();
        //-------Initialization for the Table Nav Bar--------
        NavSideBar tableNavBar = new NavSideBar(processing);

        String[] options = new String[DbColumn.values().length];
        for(int i = 0 ; i < DbColumn.values().length ; i++){
            options[i] = DbColumn.values()[i].toString();
        }
        //Adding in Menu Item with query options
        NavSideBarMenu tableQuery = new NavSideBarMenu(processing, tableNavBar.sideBarWindowGraphics, "Table Query", 1, 150, tableNavBar.windowWidth-2, 50, tableNavBar.windowWidth-2, 500); //0
        tableQuery.addWidget(new WidgetTextInput(processing, tableNavBar.sideBarWindowGraphics, null, "Bus ID Filter (space separated values)", 1, 40, tableNavBar.windowWidth-2, 40, true), 40); //1
        tableQuery.addWidget(new WidgetTextInput(processing, tableNavBar.sideBarWindowGraphics, null, "Stop ID Filter (space separated values)", 1, 90, tableNavBar.windowWidth-2, 40, true), 90); //2
        tableQuery.addWidget(new WidgetCheckbox(processing,tableNavBar.sideBarWindowGraphics,null,"At Stop",new String[]{""},1,150,tableNavBar.windowWidth-2,40), 140); //3
        tableQuery.addWidget(new WidgetDualSlider(processing, tableNavBar.sideBarWindowGraphics, null, "Hours of the Day", 1, 190, tableNavBar.windowWidth-2, 40, 1, 24), 190); //4
        tableQuery.addWidget(new WidgetDualSlider(processing, tableNavBar.sideBarWindowGraphics, null, "Date Range", 1, 240, tableNavBar.windowWidth-2, 40, 1, 31), 270); //5
        tableQuery.addWidget(new WidgetSlider(processing, tableNavBar.sideBarWindowGraphics, null, "Result Limit", 1, 290, tableNavBar.windowWidth-2, 40, 1, 10000), 320); //6
        tableQuery.addWidget(new WidgetDropDown(processing,tableNavBar.sideBarWindowGraphics,null,"Information Fields",options,1,150,tableNavBar.windowWidth-2,200) , 10); //7
        tableQuery.addWidget(new WidgetButton(processing, tableNavBar.sideBarWindowGraphics, GuiEventsEnum.TABLE_QUERY, "OK", 230, 160, 60, 20), 400);

        tableNavBar.addMenuItem(tableQuery);
        navSideBars.add(tableNavBar);

        //-----Initialization for the Graph Nav Bar---------
        NavSideBar graphNavBar = new NavSideBar(processing);

        //Delay Graph Menu
        NavSideBarMenu delayGraph = new NavSideBarMenu(processing, graphNavBar.sideBarWindowGraphics, "Delay Graph", 1, 150, graphNavBar.windowWidth-2, 50, graphNavBar.windowWidth, 300);
        delayGraph.addWidget(new WidgetTextInput(processing, graphNavBar.sideBarWindowGraphics, null, "Bus Line ID's ( space separated values)", 1, 150, graphNavBar.windowWidth-2, 40, true), 50);
        delayGraph.addWidget(new WidgetSlider(processing, graphNavBar.sideBarWindowGraphics, null, "Day of Month", 1, 150, graphNavBar.windowWidth-2, 40, 1, 31), 100);
        delayGraph.addWidget(new WidgetSlider(processing, graphNavBar.sideBarWindowGraphics, null, "Number of data points", 1, 150, graphNavBar.windowWidth-2, 40, 1, 1000), 150);
        delayGraph.addWidget(new WidgetButton(processing, graphNavBar.sideBarWindowGraphics, GuiEventsEnum.DELAY_GRAPH_LINE, "Make Query", 90, 140, 120, 30), 200);

        //Bar Chart Menu
        NavSideBarMenu operationalBuses = new NavSideBarMenu(processing, graphNavBar.sideBarWindowGraphics, "Operational Buses",1, 200, graphNavBar.windowWidth-2, 50, graphNavBar.windowWidth, 300);
        operationalBuses.addWidget(new WidgetTextInput(processing, graphNavBar.sideBarWindowGraphics, null, "Route Numbers (space separated values)", 1, 140, graphNavBar.windowWidth-2, 40, true), 30);
        operationalBuses.addWidget(new WidgetDualSlider(processing, graphNavBar.sideBarWindowGraphics, null, "Hour Range (hours)", 1, 180, graphNavBar.windowWidth-2, 40, 1, 24), 100);
        operationalBuses.addWidget(new WidgetSlider(processing,graphNavBar.sideBarWindowGraphics,null,"Date (day)",1,220,graphNavBar.windowWidth-2,40,1,31),170);
        operationalBuses.addWidget(new WidgetButton(processing, graphNavBar.sideBarWindowGraphics, GuiEventsEnum.OPERATIONAL_BUSES, "Make Query", 90, 260, 120, 30), 240);

        //buses at a stop menu
        NavSideBarMenu busesAtAStop = new NavSideBarMenu(processing,graphNavBar.sideBarWindowGraphics,"Buses That Visited This Stop",1,250,graphNavBar.windowWidth-2,50,graphNavBar.windowWidth, 300);
        busesAtAStop.addWidget(new WidgetTextInput(processing,graphNavBar.sideBarWindowGraphics,null,"Stop Number",1,250,graphNavBar.windowWidth-2,40,true),30);
        busesAtAStop.addWidget(new WidgetSlider(processing,graphNavBar.sideBarWindowGraphics,null,"Day (days)",1,250,graphNavBar.windowWidth-2,40,1,31),100);
        busesAtAStop.addWidget(new WidgetButton(processing, graphNavBar.sideBarWindowGraphics, GuiEventsEnum.BUSES_AT_A_STOP, "Make Query", 90, 260, 120, 30), 170);

        //delays for bus journey menu
        NavSideBarMenu delaysForJourneys = new NavSideBarMenu(processing,graphNavBar.sideBarWindowGraphics,"Time Length Of Journeys",1,300,graphNavBar.windowWidth-2,50,graphNavBar.windowWidth,300);
        delaysForJourneys.addWidget(new WidgetTextInput(processing,graphNavBar.sideBarWindowGraphics,null,"Bus Ids (space separated values)",1,250,graphNavBar.windowWidth-2,40,true),30);
        delaysForJourneys.addWidget(new WidgetSlider(processing,graphNavBar.sideBarWindowGraphics,null,"Day (days)",1,250,graphNavBar.windowWidth-2,40,1,30),90);
        delaysForJourneys.addWidget(new WidgetSlider(processing,graphNavBar.sideBarWindowGraphics,null,"Route Direction",1,250,graphNavBar.windowWidth-2,40,0,1),150);
        delaysForJourneys.addWidget(new WidgetButton(processing, graphNavBar.sideBarWindowGraphics, GuiEventsEnum.DELAYS_BY_TIME_OF_DAY, "Make Query", 90, 260, 120, 30), 210);

        //Add the previously 4 created menu items to the graphNavBar
        graphNavBar.addMenuItem(delayGraph);
        graphNavBar.addMenuItem(operationalBuses);
        graphNavBar.addMenuItem(busesAtAStop);
        graphNavBar.addMenuItem(delaysForJourneys);

        navSideBars.add(graphNavBar);

        //-----Map Nav Bar--------
        NavSideBar mapNavBar = new NavSideBar(processing);
        mapNavBar.addMenuItem(new NavSideBarMenu(processing, mapNavBar.sideBarWindowGraphics, "Map Query", 1, 150, mapNavBar.windowWidth-2, 50, mapNavBar.windowWidth, 300));
        mapNavBar.getMenuItem(0).addWidget(new WidgetTextInput(processing, mapNavBar.sideBarWindowGraphics, null, "Bus  IDs (space separated values)", 1, 140, mapNavBar.windowWidth-2, 40, true), 20);
        mapNavBar.getMenuItem(0).addWidget(new WidgetButton(processing, mapNavBar.sideBarWindowGraphics, GuiEventsEnum.MAP_QUERY_VIEW_ROUTES, "OK", 230, 160, 60, 20), 80);
        navSideBars.add(mapNavBar);


        //Route Nav Bar
        NavSideBar routeNavBar = new NavSideBar(processing);
        routeNavBar.addMenuItem(new NavSideBarMenu(processing, routeNavBar.sideBarWindowGraphics, "Explorer Query", 1, 150, mapNavBar.windowWidth-2, 50, mapNavBar.windowWidth, 300));
        routeNavBar.getMenuItem(0).addWidget(new WidgetTextInput(processing,routeNavBar.sideBarWindowGraphics,null,"Bus ID",1,140,routeNavBar.windowWidth-2,40, true),20);
        routeNavBar.getMenuItem(0).addWidget(new WidgetButton(processing, routeNavBar.sideBarWindowGraphics, GuiEventsEnum.ROUTE_QUERY, "OK", 230, 160, 60, 20), 80);
        //routeNavBar.getMenuItem(0).addWidget(new WidgetTextInput(processing,routeNavBar.sideBarWindowGraphics,null,"Bus ID",1,150,routeNavBar.windowWidth-2,40, true),80);
        //routeNavBar.getMenuItem(0).addWidget(new WidgetButton(processing, routeNavBar.sideBarWindowGraphics, GuiEventsEnum.ROUTE_QUERY, "Make Query", 90, 140, 120, 30), 250);
        navSideBars.add(routeNavBar);

        currentNavSideBar = navSideBars.get(currentScreenIndex);
    }

    /**
     * @param screenName Screen name you wish to get
     * @return the screen associated with that screen name
     * @author Eddie
     */
    public static Screen getScreen(ScreenNames screenName){
        return screens.get(screenName.ordinal());
    }

    /**
     * @param screenName Screen name you wish to get
     * @return NavSideBar associated with that screen name
     * @author Eddie
     */
    public static NavSideBar getNavSideBar(ScreenNames screenName){
        return navSideBars.get(screenName.ordinal());
    }

    /**
     * draw
     * The main draw method of the program. This is considered our rendering thread
     * @author Sameul
     */
    public void draw() {
        countMainDrawThreadCalls++;       //How many times this Draw Method has been called. Used in FPS Calculation

        currentScreen.draw();
        if(guiStatus == GuiStatusEnum.LOADING && currentAnimation == GuiAnimationEnum.FORWARD_SCREEN){
            animatingScreen.draw();
            if(animatingScreen.getXPos() <= 0){
                currentScreen = animatingScreen;
                currentNavSideBar = navSideBars.get(GuiEventController.nextScreenIndex);
                guiStatus = GuiStatusEnum.READY;
            }
        }
        else if(guiStatus == GuiStatusEnum.LOADING && currentAnimation == GuiAnimationEnum.BACK_SCREEN){
            System.out.println(animatingScreen.getXPos());
            animatingScreen.draw();
            if(animatingScreen.getXPos() >=1280){
                currentNavSideBar = navSideBars.get(GuiEventController.nextScreenIndex);
                guiStatus = GuiStatusEnum.READY;
            }
        }
        currentNavSideBar.draw();
    }

    /**
     * LogicThread
     * Logic thread that does all updating and number crunching in the program
     * @author Samuel
     */
    private class LogicThread implements Runnable {
        public void run() {
            //ArtificialProcessingMain.initialDrawThread.interrupt();
            System.out.println(Thread.currentThread().getName() + ": the logicThread is running");

            while (true) {    //infinite loop, just like draw()
                countControllerThreadCalls++;       //How many times this Logic Method has been called. Used in FPS Calculation

                //-----------------Logic method-------------------
                GuiAnimationController.animate();
                currentScreen.update();
                currentNavSideBar.update();
                if(guiStatus == GuiStatusEnum.LOADING){
                    animateScreen();
                }
                //----------------------------------------------

                //-----------------Frame Limiter------------------
                int timeToWait = 1000 / frameRateControllerThread - (processing.millis() - lastCallControllerThread);
                if (timeToWait > 1) {
                    try {
                        //Sleep the thread for long enough to match framerate
                        Thread.currentThread().sleep(timeToWait);
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + ": Interrupting Thread");
                    }
                }

                lastCallControllerThread = processing.millis();

                //End of navSideBarThread Main Loop
            }
        }
    }

    private void animateScreen(){
        if(currentAnimation == GuiAnimationEnum.FORWARD_SCREEN) {
            int dx = -1 * (int) ((animatingScreen.getXPos() * EASING) + 2);
            animatingScreen.setXPos(animatingScreen.getXPos() + dx);

            if (animatingScreen.getXPos() <= 3) {
                animatingScreen.setXPos(0);
                currentScreenIndex = GuiEventController.nextScreenIndex;
            }
        }
        else if(currentAnimation == GuiAnimationEnum.BACK_SCREEN){
            int dx = (int)(((1280-animatingScreen.getXPos()) * EASING) + 2);
            animatingScreen.setXPos(animatingScreen.getXPos() + dx);

            if (animatingScreen.getXPos() >= 1277) {
                animatingScreen.setXPos(1280);
                currentScreenIndex = GuiEventController.nextScreenIndex;
            }
        }
    }

    /**
     * MouseReleasedThread
     * An event thread that checks everything in the program for an event and updates objects as necessary in the GuiEventController
     * @author Samuel
     */
    private class MouseReleasedThread implements Runnable {
        public void run(){
            //navSideBar Event's
            GuiEventsEnum currentEvent = currentNavSideBar.getEvents();
            if (currentEvent != null) {
                GuiEventController.guiEventOccurred(currentEvent);
                System.out.println("NAV EVENT " + currentEvent);
            }

            //Screen Event's
            currentEvent = currentScreen.getEvents();
            if(currentEvent != null) {
                GuiEventController.guiEventOccurred(currentEvent);
                System.out.println("SCREEN EVENT " + currentEvent);
            }

            //dataDisplayEvents
            currentScreen.updateListeners(MouseEvent.RELEASE);
        }
    }

    /**
     * setScreen
     * method to change the current screen
     *
     * @since 08/03/2016
     */
    public static void setScreen(int screenIndex) {
        currentScreenIndex = screenIndex;
        currentScreen = screens.get(screenIndex);
        currentNavSideBar = navSideBars.get(screenIndex);
    }

    public static GuiStatusEnum getStatus(){
        return guiStatus;
    }

    public static void setStatus(GuiStatusEnum status){
        guiStatus = status;
    }

    public GuiAnimationEnum getAnimation(){
        return currentAnimation;
    }

    public static void setAnimation(GuiAnimationEnum animation){
        currentAnimation = animation;
    }

    /**
     * MiscThread
     * This thread is solely for miscellaneous things in the program, E.G the fps of each thread. Used mainly for debugging etc
     * @author Samuel
     */
    private class MiscThread implements Runnable {
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": the MiscThread is running");

            while (true) {        //infiniteLoop

                countMiscThreadCalls++;

                //---------------FPS Calculation---------------

                controllerThreadFPS = (((countMainDrawThreadCalls - countMainDrawThreadCallsOld) * frameRateMiscThread));
                mainDrawThreadFPS = (((countControllerThreadCalls - countControllerThreadCallsOld) * frameRateMiscThread));

                String mainDrawMethodThreadFPSTitle = "mainDrawMethodThread FPS: " + controllerThreadFPS + ". ";        //calculation of fps
                String controllerThreadFPSTitle = "logicThread FPS: " + mainDrawThreadFPS + ". ";
                //Future Thread FPS Calculations go here
                String miscThreadFPS = "miscThreadFPS: " + (countMiscThreadCalls - countMiscThreadCallsOld + ". ");
                processing.getSurface().setTitle(mainDrawMethodThreadFPSTitle + controllerThreadFPSTitle + miscThreadFPS);


                countMainDrawThreadCallsOld = countMainDrawThreadCalls;
                countControllerThreadCallsOld = countControllerThreadCalls;
                //Future Thread countCalls here
                countMiscThreadCallsOld = countMiscThreadCalls;

                //---------------------------------------------

                //--------------Frame Limiter-----------------
                int timeToWait = 1000 / frameRateMiscThread - (processing.millis() - lastCallMiscThread);
                if (timeToWait > 1) {
                    try {
                        //Sleep the thread for long enough to match framerate
                        Thread.currentThread().sleep(timeToWait);
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + ": Interrupting Thread");
                    }
                }

                lastCallMiscThread = processing.millis();
                //------------------------------------------

                //End of MiscThread Main Loop
            }
        }
    }
}