package project.gui;

/**
 * The different possible queries in the program
 * @author Samuel Eddie
 */
public enum GuiEventsEnum {
    //gui events
    PUSH_NAV_SIDE_BAR, EXTEND_CURRENT_MENU,
    SWITCH_TO_MAPSCREEN, SWITCH_TO_TABLESCREEN, SWITCH_TO_GRAPHSCREEN, SWITCH_TO_ROUTESCREEN,
    BACK_BUTTON,

    //Query Events
    TABLE_QUERY,
    DELAY_GRAPH_LINE,
    OPERATIONAL_BUSES,
    BUSES_AT_A_STOP,
    MAP_QUERY_VIEW_ROUTES,
    MAP_QUERY_VIEW_ROUTE,// soz , put this is as it was easy fix (time reasons)
    ROUTE_QUERY,
    DELAYS_BY_TIME_OF_DAY,
    VIEW_ARRIVAL_TIMES,
    COMPARE_QUNATITYS

}

