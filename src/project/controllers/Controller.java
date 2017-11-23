package project.controllers;

import project.gui.*;
import project.gui.dataDisplay.*;
import project.model.*;
import project.model.exceptions.DataNotFoundException;
import static project.gui.GuiEventsEnum.*;
import static project.gui.dataDisplay.DataDisplayEnum.GRAPH_BAR;

/**
 * Controller class to handle the communication between gui and model
 * @author Samuel
 * @author Eddie
 */
public class Controller {

    public static GuiEventsEnum currentQuery;

    /**
     * The query thread that is run when a query is made
     * @author Samuel
     * @author Eddie
     */
    private static class QueryMadeThread implements Runnable{
        public void run() {
            //The query thread tells other objects to fetch their respective queries within this thread.
            System.out.println(currentQuery);
            switch (currentQuery) {
                //routeScreen
                case ROUTE_QUERY:
                    GuiMainController.getScreen(ScreenNames.ROUTE_EXPLORER).getDataDisplay(DataDisplayEnum.ROUTE_EXPLORER).getData();
                    break;
                //tableScreen
                case TABLE_QUERY:
                    GuiMainController.getScreen(ScreenNames.TABLE).getDataDisplay(DataDisplayEnum.TABLE).getData();
                    break;
                case VIEW_ARRIVAL_TIMES:
                    GuiMainController.getScreen(ScreenNames.TABLE).getDataDisplay(DataDisplayEnum.TABLE).getData();
                    break;
                //graphsScreen
                case OPERATIONAL_BUSES:
                    GuiMainController.getScreen(ScreenNames.GRAPH).getDataDisplay(DataDisplayEnum.GRAPH_BAR).getData();
                    GuiMainController.getScreen(ScreenNames.GRAPH).setDataDisplay(DataDisplayEnum.GRAPH_BAR);
                    break;
                case DELAY_GRAPH_LINE:
                    GuiMainController.getScreen(ScreenNames.GRAPH).getDataDisplay(DataDisplayEnum.GRAPH_LINE).getData();
                    GuiMainController.getScreen(ScreenNames.GRAPH).setDataDisplay(DataDisplayEnum.GRAPH_LINE);
                    break;
                case BUSES_AT_A_STOP:
                    GuiMainController.getScreen(ScreenNames.GRAPH).getDataDisplay(DataDisplayEnum.GRAPH_BAR).getData();
                    GuiMainController.getScreen(ScreenNames.GRAPH).setDataDisplay(DataDisplayEnum.GRAPH_BAR);
                    break;
                case DELAYS_BY_TIME_OF_DAY:
                    GuiMainController.getScreen(ScreenNames.GRAPH).getDataDisplay(DataDisplayEnum.GRAPH_LINE).getData();
                    GuiMainController.getScreen(ScreenNames.GRAPH).setDataDisplay(DataDisplayEnum.GRAPH_LINE);
                    break;
                //mapScreen
                case MAP_QUERY_VIEW_ROUTES:
                case MAP_QUERY_VIEW_ROUTE:
                    GuiMainController.getScreen(ScreenNames.MAP).getDataDisplay(DataDisplayEnum.MAP).getData();
                    break;
            }
        }
    }


    /**
     * The method that boots up the query thread with a query
     * @author Samuel
     * @param event
     */
    public static void queryMade(GuiEventsEnum event) {
        currentQuery = event;
        try {
            new Thread(new QueryMadeThread()).start();
        }catch(IllegalThreadStateException e){
            System.out.print(e.getMessage());
            System.out.println("Recovering: Not making current Query");
        }
    }


}
