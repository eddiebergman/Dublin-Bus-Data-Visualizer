package project.gui.dataDisplay.dataMap;

import processing.core.*;
import project.gui.GuiMainController;
import project.gui.ScreenNames;
import project.gui.dataDisplay.*;
import project.model.ModelMain;

import java.util.ArrayList;

/**
 * Map
 * A map background of Dublin with interactive
 * overlay features.
 *
 * @author  Seng Leung
 * @version 1.00
 * @since   2016-04-01
 */
public class Map implements DataDisplayI, InteractiveDisplayI {

    private static final DataDisplayEnum TYPE = DataDisplayEnum.MAP;
    private static final boolean INTERACTIVE = true;

    public static int stopToDisplay = -1;

    private PApplet processing;
    private PGraphics screen;
    private PImage dublinMap;
    private ArrayList<MapPoint> mapPoints;

    /**
     * Map constructor.
     *
     * @param processing    PApplet.
     * @param screen        PGraphics.
     * @param busData       MapBus[]
     */
    public Map(PApplet processing, PGraphics screen, MapBus[] busData) {
        // Parameter assignments.
        this.processing = processing;
        this.screen = screen;
        this.dublinMap = processing.loadImage("map/dublinMap.png");
        mapPoints = convertToMapPoints(busData);
    }

    private ArrayList<MapPoint> convertToMapPoints(MapBus[] busData){
        // Colour palette for different bus ID.
        int[]  primaryColours = new int[]{processing.color(255, 102, 102), processing.color(0, 153, 0),
                processing.color(255, 153, 51), processing.color(0, 102, 204),
                processing.color(127, 0,255)};
        int[] colours = new int[busData.length];
        // Assign colour array for bus points.
        for (int i = 0; i < busData.length; i++) {
            // First uses pre-defined colours.
            if (i < primaryColours.length) {
                colours[i] = primaryColours[i];
                // If bus ID > pre-defined colours, then use randomly generated colour.
            } else {
                colours[i] = processing.color((int) (Math.random() * 255),
                        (int) (Math.random() * 255),
                        (int) (Math.random() * 255));
            }
        }

        // Points of bus positions in between stops.
        ArrayList<MapPoint> newMapPoints = new ArrayList<>();
        for (int i = 0; i < busData.length; i++) {
            PVector[] pv = busData[i].route;
            String name = busData[i].busId;
            for (int j = 0; j < busData[i].route.length; j++) {
                double x = pv[j].x;
                double y = pv[j].y;
                newMapPoints.add(new MapPoint(processing, screen, name, 0, x, y, colours[i]));
            }
        }

        // Points of bus stops.
        for (int i = 0; i < busData.length; i++) {
            PVector[] stops = busData[i].stops;
            String name = busData[i].busId;
            for (int j = 0; j < busData[i].stops.length; j++) {
                double stopX = stops[j].x;
                double stopY = stops[j].y;
                double stopID = stops[j].z;
                newMapPoints.add(new MapPoint(processing, screen, name, stopID, stopX, stopY, colours[i]));
            }
        }
        return newMapPoints;
    }

    /**
     * Displays the map with information overlay.
     */
    public void draw() {
        // Map of Dublin.
        screen.image(dublinMap, 0, 0);

        //draw the rectangles back onto screen for navigation
       // GuiMainController.currentNavSideBar.

        // Bus points.
        for (MapPoint j : mapPoints) {
            j.drawPoint();
        }

        // Information window.
        for (MapPoint i : mapPoints) {
            i.drawInfo();
        }
    }

    private void setSelectedPoint(int stopNumber){
        for(MapPoint point : mapPoints){
            if(point.stopID == stopNumber){
                point.showInfo = true;
                setInfoState(point.toString());
            }
        }
    }


    private static String infoState = "";

    public static String getInfoState() {
        return infoState;
    }

    public static void setInfoState(String state) {
        infoState = state;
    }

    public void update() {
        for (MapPoint point : mapPoints){
            point.update();
        }
    }

    /**
     * Gets the data it needs for this dataDisplay
     * @author Seng
     * @author Eddie
     */
    public void getData() {
        // Constructor for data input.
        System.out.println("here query");
        mapPoints = convertToMapPoints(ModelMain.getTestMapData(
                GuiMainController.getNavSideBar(ScreenNames.MAP).getMenuItem(0).widgets.get(1).getString().split(" ")));
        if(stopToDisplay != -1){
            setSelectedPoint(stopToDisplay);
            stopToDisplay = -1;
        }
    }

    public static void setStopToDisplay(int stopNumber){
        stopToDisplay = stopNumber;
    }

    public DataDisplayEnum getType() {
        return TYPE;
    }

    public boolean isInteractive(){
        return INTERACTIVE;
    }

    public void mouseEventOccurred(int mouseEvent) {

    }

}
