package project.model;

import project.database.DbColumn;
import project.database.DbTable;
import project.gui.dataDisplay.RouteExplorer.REInternalEvents;
import project.gui.dataDisplay.dataGraphBar.DataPointBarChart;
import project.model.exceptions.DataNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//***************************
//TODO change queries upon database change
//TODO reconsider innerClass 'dataPoint'
//***************************

/**
 * class used to represent a busStop
 * @author Eddie
 */
public class BusStop {

    //---variables---
    private int stopId;
    private String[] visitingBuses = null;

    //---constructors---

    /**
     * Creates the bus object
     * @param stopId this id of the stop
     */
    protected BusStop(int stopId){
        this.stopId = stopId;
    }

    //---methods---

    /**
     * @return id of this stop
     */
    public int getStopId(){
        return stopId;
    }

    /**
     * returns all buses that visited this stop
     * @return visiting buses
     */
    public String[] getVisitingBuses(){
        if(visitingBuses == null){
            loadVisitingBuses();
        }
        return visitingBuses;
    }

    /**
     * checks whether this stop has passed busId
     * @param bus bus to check for
     * @return whether it contains bus passed
     */
    public boolean hasBus(String bus){
        if(visitingBuses == null){
            loadVisitingBuses();
        }
        for(String busId : visitingBuses){
            if(busId.equals(bus)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the average Dealy for a given bus between two typs and also whether to be Min , Avg or Max Delay
     * @param time1 timestamp 1
     * @param time2 timestamp 2
     * @param busId busId to get info for
     * @param type min,avg or max for clock
     * @return gets (type) delay between two given times
     * @throws DataNotFoundException if no data was found between these two times
     */
    public int getAverageDelayBetweenFor(long time1 , long time2 , String busId , REInternalEvents type) throws DataNotFoundException{
        //have to use mainTable as buses don't always give a datapoint when it arrives at a stop
        ResultSet ans = ModelMain.dbManager.makeQuery(String.format(
                        "SELECT %s(%s) FROM %s WHERE %s = '%s' AND %s = %s AND %s >= %s AND %s <= %s"
                ,type.toString(), DbColumn.delay , DbTable.mainTable ,DbColumn.busId ,busId, DbColumn.stopId , this.stopId,
                DbColumn.time , time1 , DbColumn.time , time2));
        try{
            ans.next();
            return ans.getInt(1);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        throw new DataNotFoundException("avg(delay) for bus " + busId + " could not be gotten");
    }

    /**
     * Counts of all the buses that visit this stop on a given day
     * @param day day to get
     * @return Bar Char Data to display
     */
    public ArrayList<DataPointBarChart> getCountOfVisitingBuses(int day){
        ArrayList<DataPointBarChart> returnData = new ArrayList<DataPointBarChart>();
        for(String bus : getVisitingBuses()){
            ResultSet rs = ModelMain.dbManager.makeQuery(String.format(
                    "SELECT count(%s) FROM (SELECT %s FROM %s WHERE %s = '%s' AND %s >= %s AND %s <= %s AND %s = %s)",
                    DbColumn.journeyId,DbColumn.journeyId,DbTable.mainTable,DbColumn.busId,bus,
                    DbColumn.time,ModelMain.dayToTimeStamp(day),DbColumn.time,ModelMain.dayToTimeStamp(day+1),DbColumn.stopId,stopId
            ));
            try{
                rs.next();
                returnData.add(new DataPointBarChart(rs.getInt(1),bus));
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return returnData;
    }

    /**
     * loads in what buses visited this stop
     */
    private void loadVisitingBuses(){
        ArrayList<String> list = new ArrayList<>();
        ResultSet rs = ModelMain.dbManager.makeQuery(String.format(
                "SELECT %s FROM %s WHERE %s = %s ",
                DbColumn.busId , DbTable.stopsBusesTable, DbColumn.stopId , this.stopId));
        try{
            while(rs.next()) {
                list.add(rs.getString(1)); //1 specifies first column which is busId
            }
            visitingBuses = list.toArray(new String[list.size()]);
        }catch(SQLException e) {
            System.out.println("Failed loadBuses for stop " + stopId + '\n' + e.getMessage());
        }
    }


}
