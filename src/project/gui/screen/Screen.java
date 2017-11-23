package project.gui.screen;

import processing.core.*;
import project.gui.*;
import project.gui.dataDisplay.*;
import project.gui.widget.*;

import java.util.ArrayList;

/**
 *Screen
 * The core class for the different screens in the program
 * @author Samuel
 */
public class Screen {

    public PApplet processing;
    public PGraphics screenWindowGraphics;
    public ArrayList<Widget> widgets;
    public ArrayList<DataDisplayI> dataDisplays;
    private ArrayList<InteractiveDisplayI> interactiveDisplays;
    private int currentDataDisplayIndex = 0;
    private int xPos;

    public final int WINDOW_WIDTH = 1280;
    public final int WINDOW_HEIGHT = 720;

    public Screen(PApplet p){
        this.processing = p;
        screenWindowGraphics = processing.createGraphics(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.xPos = 0;
        interactiveDisplays = new ArrayList<InteractiveDisplayI>();
        initialiseWidgets();
        initialiseDataDisplays();
    }

    private void initialiseWidgets(){
        widgets = new ArrayList<Widget>();
        widgets.add(new WidgetRectangle(processing, screenWindowGraphics, 5, 5, GuiEventsEnum.PUSH_NAV_SIDE_BAR));
        widgets.add(new WidgetButton(processing, screenWindowGraphics, GuiEventsEnum.BACK_BUTTON, "◄", 5, 33, 30, 16));
    }

    private void initialiseDataDisplays(){
        dataDisplays = new ArrayList<DataDisplayI>();
    }

    public void draw(){
        screenWindowGraphics.beginDraw();

        //initialize drawing for dataDisplay
        screenWindowGraphics.background(245);
        screenWindowGraphics.strokeWeight(1);
        screenWindowGraphics.colorMode(PConstants.RGB);
        drawDataDisplays();

        //initialize drawing for widgets
        screenWindowGraphics.strokeWeight(1);
        screenWindowGraphics.colorMode(PConstants.RGB);
        drawWidgets();

        screenWindowGraphics.endDraw();
        processing.image(screenWindowGraphics, xPos, 0);
    }

    public void drawWidgets(){
        for(int i = 0; i < widgets.size(); i++){
            widgets.get(i).draw();
        }
    }

    public void drawDataDisplays(){
       // for(int i = 0; i < dataDisplays.size(); i++){
            dataDisplays.get(currentDataDisplayIndex).draw();
        //}
    }

    public void update(){
        updateWidgets();
        updateDataDisplays();
    }

    public void updateWidgets() {
        for (int i = 0; i < widgets.size(); i++) {
            widgets.get(i).update();
        }
    }

    /**
     * Updates the current DataDisplay
     * @author Samuel
     * @author Eddie
     */
    public void updateDataDisplays(){
        dataDisplays.get(currentDataDisplayIndex).update();
    }

    public GuiEventsEnum getEvents(){
        GuiEventsEnum currentEvent = null;
        for(int i = 0; i < widgets.size(); i++){
            currentEvent = widgets.get(i).getEvent();
            if(currentEvent != null){
                return currentEvent;
            }
        }
        return null;
    }

    /**
     * Adds a dataDisplay to a screen and adds it to its ListenerList if it is interactive
     * @param data dataDisplay to add
     */
    public void addDataDisplay(DataDisplayI data){
        dataDisplays.add(data);
        if(data.isInteractive()){
            interactiveDisplays.add((InteractiveDisplayI) data);
        }
    }

    /**
     * Sets the current dataDisplay of this screen
     * @param type type of data display
     */
    public void setDataDisplay(DataDisplayEnum type){
        for(int i = 0 ; i < dataDisplays.size() ; i++){
            if(dataDisplays.get(i).getType() == type){
                currentDataDisplayIndex = i;
            }
        }
    }

    /**
     * gets dataDisplay associated with this enum
     * @param type type of DataDisplay to get
     * @return dataDisplay requested
     */
    public DataDisplayI getDataDisplay(DataDisplayEnum type){
        for(DataDisplayI dataDisplay : dataDisplays){
            if(dataDisplay.getType() == type){
                return dataDisplay;
            }
        }
        return null;
    }

    /**
     * Updates the listeners of this screen when called
     * @param event mouseEvent that occured
     * @author Eddie
     */
    public void updateListeners(int event){
        if(GuiMainController.currentNavSideBar.getStatus() == GuiStatusEnum.PUSHED_IN){
            for(InteractiveDisplayI dataDisplay:interactiveDisplays) {
                    dataDisplay.mouseEventOccurred(event);
                }
        }
    }

    public void setXPos(int xPos){
        this.xPos = xPos;
    }
    public int getXPos(){
        return xPos;
    }
}
