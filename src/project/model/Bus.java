package project.model;
import project.database.DbColumn;
import project.database.DbTable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to represent a bus object
 * @author Eddie
 */
public class Bus {

    //---variables----
    private String busId;
    private int[] stops0 = null;
    private int[] stops1 = null;
    private int[] allStops = null;

    //---constructors---

    /**
     * Constructs a bus object of given busId
     * @param busId id of the bus e.g. 25A
     */
    public Bus(String busId){
        this.busId = busId;
    }

    //---methods---
    /**
     * get the id of this bus
     * @return busId
     */
    public String getBusId(){
        return busId;
    }

    /**
     * gets all the stops this bus goes too
     * @return stops
     */
    public int[] getStops0() {
        if (stops0 == null || stops1 == null || allStops == null) {
            loadStops();
        }
        return this.stops0;
    }

    /**
     * gets all the stops this bus goes too
     * @return stops
     */
    public int[] getStops1() {
        if (stops0 == null || stops1 == null || allStops == null) {
            loadStops();
        }
        return this.stops1;
    }

    /**
     * gets all the stops this bus goes too
     * @return stops
     */
    public int[] getAllStops() {
        if (stops0 == null || stops1 == null || allStops == null) {
           loadStops();
        }
        return this.allStops;
    }

    /**
     * loads all the stops into the object
     */
    public void loadStops(){
        //direction 1
        System.out.println("LOADING STOPS FOR BUS ID " + this.busId);
                ResultSet stops0results = ModelMain.dbManager.makeQuery(String.format(
                "SELECT %s FROM %s WHERE %s = '%s' AND %s = %s ",
                DbColumn.stopId,DbTable.stopsBusesTable,DbColumn.busId,busId,DbColumn.direction,0
        ));
        ResultSet stops0count = ModelMain.dbManager.makeQuery(String.format(
                "SELECT count(*) FROM %s WHERE %s = '%s' AND %s = %s",
               DbTable.stopsBusesTable,DbColumn.busId,busId,DbColumn.direction,0
        ));
        //direction 2
        ResultSet stops1results = ModelMain.dbManager.makeQuery(String.format(
                "SELECT %s FROM %s WHERE %s = '%s' AND %s = %s",
                DbColumn.stopId, DbTable.stopsBusesTable,DbColumn.busId,busId,DbColumn.direction,1
        ));
        ResultSet stops1count = ModelMain.dbManager.makeQuery(String.format(
                "SELECT count(*) FROM %s WHERE %s = '%s' AND %s = %s",
                DbTable.stopsBusesTable,DbColumn.busId,busId,DbColumn.direction,1
        ));
        try{
            int index = 0;
            int index2 = 0;
            stops0count.next();
            stops1count.next();
            stops0 = new int[stops0count.getInt(1)];
            stops1 = new int[stops1count.getInt(1)];
            allStops = new int[stops0.length + stops1.length];
            while(stops0results.next()){
                stops0[index] = stops0results.getInt(1);
                allStops[index++] = stops0results.getInt(1);
            }

            while (stops1results.next()){
                stops1[index2] = stops1results.getInt(1);
                allStops[index+index2++] = stops1results.getInt(1);
            }
            System.out.println("FINISHED STOPS FOR BUS ID " + this.busId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
