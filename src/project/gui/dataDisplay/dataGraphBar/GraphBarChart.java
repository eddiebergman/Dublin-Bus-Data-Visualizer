package project.gui.dataDisplay.dataGraphBar;

import processing.core.PApplet;
import processing.core.PGraphics;
import project.controllers.Controller;
import project.gui.GuiEventsEnum;
import project.gui.GuiMainController;
import project.gui.ScreenNames;
import project.gui.dataDisplay.DataDisplayEnum;
import project.gui.dataDisplay.DataDisplayI;
import project.gui.widget.WidgetDualSlider;
import project.gui.widget.WidgetSlider;
import project.model.ModelMain;
import project.model.exceptions.DataNotFoundException;


import java.util.ArrayList;

/**
 * Created by Conal on 28/03/2016.
 */
public class GraphBarChart implements DataDisplayI {

    private static final DataDisplayEnum TYPE = DataDisplayEnum.GRAPH_BAR;
    private static final boolean INTERACTIVE = false;

    private PApplet processing;
    private PGraphics graphBarWindow;

    private ArrayList<DataPointBarChart> graphData;


    private GraphBarAxis axis;

    public GraphBarChart(PApplet p, PGraphics pg){
        this.processing = p;
        this.graphBarWindow = pg;
        this.graphData = new ArrayList<>();
        for(int i = 0; i < 15; i++ )
        {
            DataPointBarChart temp = new DataPointBarChart((i*5) + 10, i + "");
            graphData.add(temp);
        }
        this.axis = new GraphBarAxis(processing,graphBarWindow,graphData,"rand y" , "rand x");
    }

    /**
     * Gets the relevant data needed based on whatever Event is currently stored in the Controller
     * @author Conal
     * @author Eddie
     */
    public void getData(){
        int day;
        String[] buses;
        switch (Controller.currentQuery) {
            case OPERATIONAL_BUSES:
                //gets the information it needs from the set side Navigation bars
                buses = GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(1).widgets.get(1).getString().split(" ");
                int hour1 = GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(1).widgets.get(2).getMin();
                int hour2 = GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(1).widgets.get(2).getMax();
                day = GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(1).widgets.get(3).getInt();
                graphData = ModelMain.getOperationalBuses(buses, day, hour1, hour2);
                this.axis = new GraphBarAxis(processing, graphBarWindow, graphData,"Number Of Operational Buses","Route Id");
                break;
            case BUSES_AT_A_STOP:
                //gets the information it needs from the set side Navigation bars
                int stopNumber = Integer.parseInt(GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(2).widgets.get(1).getString());
                day = GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(2).widgets.get(2).getInt();
                try {
                    graphData = ModelMain.getBusStop(stopNumber).getCountOfVisitingBuses(day);
                    this.axis = new GraphBarAxis(processing,graphBarWindow,graphData , "Number Of times this stop was visited" , "Route Id");
                }catch (DataNotFoundException e){
                    System.out.println(e.getMessage());
                    GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(2).widgets.get(1).makeError();
                    GuiMainController.getNavSideBar(ScreenNames.GRAPH).getMenuItem(2).widgets.get(2).makeError();
                }
                break;
        }
    }

    public void update(){
        for(int i = 0 ; i < axis.barElements.size(); i++) {
            axis.barElements.get(i).update();
        }
    }

    public void draw(){
        this.axis.draw();
    }

    /**
     * @return if this is an interactive display object (beyond mouse hover)
     */
    public boolean isInteractive(){
        return INTERACTIVE;
    }

    /**
     * @return type of datadisplay this is
     */
    public DataDisplayEnum getType(){
        return TYPE;
    }


}
