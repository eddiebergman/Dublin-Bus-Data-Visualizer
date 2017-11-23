package project.gui.dataDisplay.dataMap;

import processing.core.PVector;

/**
 * MapBus
 * Bus map data representing bus ID,
 * coordinates, and stop ID and stop
 * coordinates.
 *
 * @author  Seng Leung
 * @version 1.00
 * @since   2016-04-01
 */
public class MapBus {

    protected String busId;
    protected PVector[] route; //vec.x = long , vec.y = lat
    protected PVector[] stops; //vec.x = long , vec.y = lat , vec.z = stopNumber

    public MapBus(String busId , PVector[] route , PVector[] stops){
        this.busId = busId;
        this.route = route;
        this.stops = stops;
    }


}
