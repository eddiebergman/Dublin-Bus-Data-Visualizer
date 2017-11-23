package project.model;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import project.ArtificialProcessingMain;
import project.database.DatabaseManager;
import project.database.DbColumn;
import project.database.DbTable;
import project.gui.dataDisplay.RouteExplorer.REInternalEvents;
import project.gui.dataDisplay.dataGraphBar.DataPointBarChart;
import project.gui.dataDisplay.dataMap.MapBus;
import project.model.exceptions.DataNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Main interface for program to request data from model
 * @author Eddie
 */
public class ModelMain {

    //---variables---
    protected static final String DATABASE_DIRECTORY = "data/Database/testDatabase.db";
    protected static final DatabaseManager dbManager = new DatabaseManager(DATABASE_DIRECTORY);
    protected static final float MIN_LATITUDE = 53.070415f ;
    protected static final float MAX_LATITUDE = 53.606533f;
    protected static final float MIN_LONGITUDE = -6.61505f;
    protected static final float MAX_LONGITUDE = -6.05315f;
    public static final long DAY1 = 1356998400000L; //1st jan 5am
    public static final long DAY2 = 1357084800000L;
    public static final long LENGTH_OF_DAY = DAY2-DAY1;
    public static final long LENGTH_OF_HOUR = LENGTH_OF_DAY/24;

    private static ArrayList<Bus> loadedBuses = new ArrayList<>();
    private static ArrayList<BusStop> loadedBusStops = new ArrayList<>();
    private static ArrayList<String> validBuses = loadValidBuses();
    private static int[] validStops = loadValidStops();

    //---methods---
    /**
     * Returns a bus object , only access to bus Constructor in this package
     * @param busId id of bus
     * @return bus object created
     */
    public static Bus getBus(String busId) throws DataNotFoundException{
        if(isValidBus(busId)) {
            for (Bus bus : loadedBuses) {
                if (bus.getBusId().equals(busId)) {
                    return bus;
                }
            }
            Bus returnBus = new Bus(busId);
            loadedBuses.add(returnBus);
            return returnBus;
        }else{
            throw new DataNotFoundException(busId + " is not a valid bus Id");
        }

    }

    /**
     * Returns a busStop object , only access to busStop Constructor in this package
     * @param stopId id of the busStop
     * @return busStop object created
     */
    public static BusStop getBusStop(int stopId)throws DataNotFoundException{
        if(isValidStop(stopId)) {
            for (BusStop stop : loadedBusStops) {
                if (stop.getStopId() == stopId) {
                    return stop;
                }
            }
            BusStop returnStop = new BusStop(stopId);
            loadedBusStops.add(returnStop);
            return returnStop;
        }else{
            throw new DataNotFoundException(stopId + " is not a valid stop Id");
        }

    }

    /**
     * Gets the delays for a bus on a particular day
     * @param busId id of bus
     * @param day day to get
     * @return all the delays for that day
     */
    public static ArrayList<PVector> getBusDelays(String busId , int day ,int points) throws DataNotFoundException{
      // return getBus(busId).getDelaysFor(day,points);
        ResultSet rs = dbManager.makeQuery(String.format(
                "SELECT %s,%s FROM %s WHERE %s = '%s' AND %s >= %s AND %s <= %s",
                DbColumn.time,DbColumn.delay,DbTable.mainTable,DbColumn.busId,busId,
                DbColumn.time,dayToTimeStamp(day),DbColumn.time,dayToTimeStamp(day+1)
        ));
        ResultSet count = dbManager.makeQuery(String.format(
                "SELECT count(*) FROM %s WHERE %s = '%s' AND %s >= %s AND %s <= %s",
                DbTable.mainTable,DbColumn.busId,busId,
                DbColumn.time,dayToTimeStamp(day),DbColumn.time,dayToTimeStamp(day+1)
        ));
        ArrayList<PVector> returnData = new ArrayList<PVector>();
        try{
            count.next();
            int amount = count.getInt(1);
            int jumpAmount = amount/points;
            long day1Time = dayToTimeStamp(day);
            while(rs.next()){

                //skips through a few based on number of points
                for(int i = 0 ; i < jumpAmount ; i++ , rs.next());
                ////////

                float seconds = (float) ((rs.getLong(1)-day1Time)/1000);
                returnData.add(new PVector(seconds/3600, rs.getInt(2)));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        if(returnData.size() == 0){
            throw new DataNotFoundException("getBusDelays(" + busId + ", " +day+ ","+ points+ ')' );
        }
        return returnData;
    }

    /**
     * Gets all the data needed for a delayClock on a certain day
     * @param busId busId To Get
     * @param stopNumber the stopnumber to get
     * @param day day to get data on
     * @param type what type (min,avg,max)
     * @return int[24] of all the values for (type) delay for each hour
     * @throws DataNotFoundException
     */
    public static int[] getClockDelayForBusStop(String busId , int stopNumber , int day , REInternalEvents type)throws DataNotFoundException{
        BusStop stop = getBusStop(stopNumber);
        int[] averages = new int[24];
        for(int i = 0 ; i < averages.length ; i++){
            averages[i] = stop.getAverageDelayBetweenFor(dayToTimeStamp(day) + i*LENGTH_OF_HOUR , dayToTimeStamp(day)+ (i+1)*LENGTH_OF_HOUR,busId,type);
        }
        return averages;
    }

    /**
     * Gets all the data asked for in columns between time1 and time2 up to the limit
     * @param time1 timestamp 1
     * @param time2 timestamp 2
     * @param limit max amount of returned items
     * @param columns what column types to return data for
     * @return String[][] representing all asked for data
     */
    public static String[][] getTabularDataFor(long time1 , long time2 ,int limit, DbColumn... columns){
        StringBuilder sb = new StringBuilder();
        for(DbColumn c : columns){
            sb.append(c.toString());
            sb.append(',');
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        String[][] returnData = null;
        ResultSet rs;
        ResultSet count = null;
        if(limit == 0) {
            count = dbManager.makeQuery(String.format(
                    "SELECT count(*) FROM %s WHERE %s >= %s AND %s <= %s",
                    DbTable.mainTable, DbColumn.time, time1, DbColumn.time, time2
            ));
            rs = dbManager.makeQuery(String.format(
                    "SELECT %s FROM %s WHERE %s >= %s AND %s <= %s",
                    sb.toString(), DbTable.mainTable, DbColumn.time, time1, DbColumn.time, time2
            ));
        }else{
            rs = dbManager.makeQuery(String.format(
                    "SELECT %s FROM %s WHERE %s >= %s AND %s <= %s LIMIT %s",
                    sb.toString(), DbTable.mainTable, DbColumn.time, time1, DbColumn.time, time2,limit
            ));
        }
        try{
            int height;
            if(limit == 0) {
                count.next();
                height = count.getInt(1);
            }else{
                height = limit;
            }
            int width = columns.length;
            returnData = new String[width][height];
            for(int j = 0 ; rs.next() ; j++){
                for(int i = 0 ; i<columns.length ; i++){
                    returnData[i][j] = rs.getString(i+1);
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return returnData;
    }

    /**
     * converts an integer representing a the amount of days from 1st Jan 2013
     * @param day the day
     * @return unix time stamp of the beginning of that day
     */
    public static long dayToTimeStamp(int day){
        return DAY1 + ((day-1)*LENGTH_OF_DAY );
    }

    /**
     * Converts an hour of a particular day to a timestamp
     * @param day day
     * @param hour hour
     * @return timestamp representing that hour of that day
     */
    public static long hourOfDayToTimeStamp(int day , int hour){
        return dayToTimeStamp(day) + hour*LENGTH_OF_HOUR;
    }

    /**
     * returns if it is a valid bus entry
     * @param bus id of bus
     * @return if valid
     */
    private static boolean isValidBus(String bus){
        for(String validBus : validBuses){
            if(validBus.equals(bus)){
                return true;
            }
        }
        return false;
    }

    /**
     * returns if it is a valid busstop entry
     * @param busStop id of stop
     * @return if valid
     */
    private static boolean isValidStop(int busStop){
        for(int validStop : validStops){
            if(validStop == busStop){
                return true;
            }
        }
        return false;
    }

    /**
     * loads all the valid buses
     * @return ArrayList of the buses
     */
    private static ArrayList<String> loadValidBuses(){
        ResultSet busResults = dbManager.makeQuery(String.format(
                "SELECT %s FROM %s GROUP BY %s",
                DbColumn.busId,DbTable.stopsBusesTable,DbColumn.busId
        ));
        ArrayList<String> buses = new ArrayList<>();
        try{
            while (busResults.next()){
                buses.add(busResults.getString(1));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return buses;
    }

    /**
     * loads all the valid stops
     * @return int[] of all the stops
     */
    private static int[] loadValidStops(){
        ResultSet stopResults = dbManager.makeQuery(String.format(
                "SELECT %s FROM %s",
                DbColumn.stopId, DbTable.stopPosTable
        ));
        ResultSet amountOfStops = dbManager.makeQuery(String.format(
                "SELECT count(%s) FROM %s",
                DbColumn.stopId, DbTable.stopPosTable
        ));
        int[] stops = null;
        try{
            amountOfStops.next();
            stops = new int[amountOfStops.getInt(1)];
            for(int i = 0 ; i < stops.length ; i++){
                stopResults.next();
                stops[i] = stopResults.getInt(1);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage() + " (load valid Stops)");
        }
        return stops;
    }

    /**
     * Gets Data requred for routes of the map
     * @param busesToGet the String[] of buses to get
     * @return MapBus[] of all the buses,their routes and stop
     */
    public static MapBus[] getTestMapData(String[] busesToGet){
        MapBus[] busesForData = new MapBus[busesToGet.length];
        ArrayList<PVector> route;
        ArrayList<PVector> stops;
        long day2 = dayToTimeStamp(2);
        for (int i = 0; i < busesToGet.length; i++) {
            String bus = busesToGet[i];
            route = new ArrayList<>();
            stops = new ArrayList<>();
            ResultSet route1Set = dbManager.makeQuery(String.format(
                    "SELECT %s, %s FROM %s WHERE %s = " +
                            "(SELECT %s FROM %s WHERE %s = %s AND %s = '%s') AND  %s = '%s' AND %s <= %s",
                    DbColumn.longitude,DbColumn.latitude,DbTable.mainTable,DbColumn.journeyId,
                    DbColumn.journeyId,DbTable.busModelJourneys,DbColumn.direction, 0 , DbColumn.busId , bus,DbColumn.busId,bus,
                    DbColumn.time,day2
            ));
            ResultSet route2Set = dbManager.makeQuery(String.format(
                    "SELECT %s, %s FROM %s WHERE %s = " +
                            "(SELECT %s FROM %s WHERE %s = %s AND %s = '%s') AND  %s = '%s'  AND %s <= %s",
                    DbColumn.longitude,DbColumn.latitude,DbTable.mainTable,DbColumn.journeyId,
                    DbColumn.journeyId,DbTable.busModelJourneys,DbColumn.direction, 1 , DbColumn.busId , bus,DbColumn.busId,bus,
                    DbColumn.time,day2
            ));
            ResultSet stops1Set = dbManager.makeQuery(String.format(
                    "SELECT %s,%s,%s FROM %s WHERE %s = (SELECT %s FROM %s WHERE %s = %s AND %s = '%s') AND %s = '%s'" +
                            " AND %s <= %s GROUP BY %s",
                    DbColumn.longitude,DbColumn.latitude,DbColumn.stopId,DbTable.mainTable,DbColumn.journeyId, DbColumn.journeyId,
                    DbTable.busModelJourneys,DbColumn.direction, 0 , DbColumn.busId , bus,DbColumn.busId,bus,DbColumn.time,day2,
                   DbColumn.stopId
            ));
            ResultSet stops2Set = dbManager.makeQuery(String.format(
                    "SELECT %s,%s,%s FROM %s WHERE %s = (SELECT %s FROM %s WHERE %s = %s AND %s = '%s') AND %s = '%s'" +
                            " AND %s <= %s GROUP BY %s",
                    DbColumn.longitude,DbColumn.latitude,DbColumn.stopId,DbTable.mainTable,DbColumn.journeyId, DbColumn.journeyId,
                    DbTable.busModelJourneys,DbColumn.direction, 1 , DbColumn.busId , bus,DbColumn.busId,bus,DbColumn.time,day2,
                    DbColumn.stopId
            ));
            try{
                while (route1Set.next()){
                    route.add(new PVector(route1Set.getFloat(1),route1Set.getFloat(2)));
                }
                while(route2Set.next()){
                    route.add(new PVector(route2Set.getFloat(1),route2Set.getFloat(2)));
                }
                while (stops1Set.next()){
                    stops.add(new PVector(stops1Set.getFloat(1),stops1Set.getFloat(2),stops1Set.getInt(3)));
                }
                while (stops2Set.next()){
                    stops.add(new PVector(stops2Set.getFloat(1),stops2Set.getFloat(2),stops2Set.getInt(3)));
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
            busesForData[i] = new MapBus(bus, route.toArray(new PVector[route.size()]), stops.toArray(new PVector[stops.size()]));
        }
        return busesForData;
    }

    /**
     * Gets the count of all operational buses on a certain day between hour1 and hour2
     * @param buses buses to get
     * @param day day
     * @param hour1 hour1
     * @param hour2 hour2
     * @return BarChart data storing the count of each bus
     */
    public static ArrayList<DataPointBarChart> getOperationalBuses(String[] buses , int day , int hour1 , int hour2){
        ArrayList<DataPointBarChart> dpbc = new ArrayList<>();
        for(String bus:buses){
            try {
                ResultSet rs = dbManager.makeQuery(String.format(
                        "SELECT count(%s) FROM (SELECT %s FROM %s WHERE %s = '%s' AND %s >= %s AND %s <= %s GROUP BY %s)",
                        DbColumn.journeyId, DbColumn.journeyId, DbTable.mainTable, DbColumn.busId, bus,
                        DbColumn.time,(dayToTimeStamp(day)+hour1*LENGTH_OF_HOUR),DbColumn.time,dayToTimeStamp(day)+hour2*LENGTH_OF_HOUR,
                        DbColumn.journeyId
                ));
                rs.next();
                dpbc.add(new DataPointBarChart(rs.getInt(1),bus));
            }catch (SQLException e){
               System.out.println(e.getMessage());
            }
        }
        return dpbc;
    }

    /**
     * Gets all the Data asked for by the parameters for the table
     * @param selections what to select (e.g. time , atStop , delay)
     * @param limit max limit of items to get
     * @param routeNumbers limit to these route number
     * @param stopNumbers limit to there stop numbers
     * @param atStop limit to if it is at the stop
     * @param hour1 low limit of hour range
     * @param hour2 high limit of hour range
     * @param day1 low limit of day range
     * @param day2 high limit of day range
     * @return table data representing info asked for
     */
    public static String[][] populateTableData(String[] selections ,int limit, String[] routeNumbers , String[] stopNumbers,boolean atStop,int hour1 , int hour2 , int day1 , int day2) {
        if(selections.length == 0){
            return new String[][] {{"Nothing Selected"}};
        }
        StringBuilder selectionBuilder = new StringBuilder();
        for (String selection : selections) {
            selectionBuilder.append(selection);
            selectionBuilder.append(',');
        }
        selectionBuilder.deleteCharAt(selectionBuilder.length() - 1);

        StringBuilder timeConditionalBuilder = new StringBuilder();
        timeConditionalBuilder.append("(");
        for (int day = day1; day <= day2; day++) {
            timeConditionalBuilder.append(String.format(
                    "(%s >= %s AND %s <= %s) OR ",
                    DbColumn.time, hourOfDayToTimeStamp(day, hour1),
                    DbColumn.time, hourOfDayToTimeStamp(day, hour2)
            ));
        }
        timeConditionalBuilder.delete(timeConditionalBuilder.length() - 4, timeConditionalBuilder.length() - 1);
        timeConditionalBuilder.append(')');


        StringBuilder routeConditional = new StringBuilder();
        if(routeNumbers.length != 0){
            if (routeNumbers != null) {
            routeConditional.append("AND (");
            for (String route : routeNumbers) {
                if (!route.equals("")) {
                    routeConditional.append(String.format(
                            "%s = '%s' OR ",
                            DbColumn.busId, route
                    ));
                }
            }
            routeConditional.delete(routeConditional.length() - 4, routeConditional.length() - 1);
            routeConditional.append(')');
            }
        }else{
            routeConditional.append(' ');
        }

        StringBuilder stopConditional = new StringBuilder();
        if(stopNumbers.length != 0){
            if(!stopNumbers[0].equals("")) {
                stopConditional.append("AND (");
                for (String stop : stopNumbers) {
                    if (!stop.equals("")) {
                        stopConditional.append(String.format(
                                "%s = %s OR ",
                                DbColumn.stopId, stop
                        ));
                    }
                }
                stopConditional.delete(stopConditional.length() - 4, stopConditional.length() - 1);
                stopConditional.append(')');
            }
        }else{
            stopConditional.append(' ');
        }

        String atStopString = (atStop)? "AND atStop = 1" : "";
        ResultSet rs = dbManager.makeQuery(String.format(
                "SELECT %s FROM %s WHERE %s %s %s %s LIMIT %s",
                selectionBuilder.toString(),DbTable.mainTable,
                timeConditionalBuilder.toString(),routeConditional.toString(),stopConditional,atStopString,limit
        ));
        String[][] newData = new String[selections.length][limit];


        try {
            for(int j = 0; j < limit && rs.next(); j++){
                for(int i = 0; i < selections.length; i++){
                    if(selections[i].equals("time")) {
                        newData[i][j] = new SimpleDateFormat("MMM-dd   HH:mm").format(new Date(rs.getLong(i+1)));
                    }else{
                        newData[i][j] = rs.getString(i + 1);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return newData;
    }

    /**
     *
     * @param bus returns the length that a journey took on a given day
     * @param day day to check
     * @param direction direction the bus was heading
     * @return ArrayList<PVector> where vec.x = time of day and vec.y = length of journey in minutes
     */
    public static ArrayList<PVector> getLengthJourneyTimes(String bus,int day,int direction){
        ArrayList<PVector> dpbc = new ArrayList<>();
        int[] stops;
        try {
            if (direction == 0) {
                stops = getBus(bus).getStops0();
            } else {
                stops = getBus(bus).getStops1();
            }
            ResultSet journeyResults = dbManager.makeQuery(String.format(
                    "SELECT journeyId FROM mainTable WHERE busId = '%s' and time >= %s AND time <= %s AND (stopId = %s OR stopId = %s) GROUP BY journeyId",
                    bus, dayToTimeStamp(day), dayToTimeStamp(day + 1),stops[1],stops[stops.length-2]
            ));
            ArrayList<Integer> journeys = new ArrayList<>();
            while (journeyResults.next()){
                journeys.add(journeyResults.getInt(1));
            }
            for(int journey:journeys) {
                ResultSet times = dbManager.makeQuery(String.format(
                        "SELECT DISTINCT stopId,time FROM mainTable WHERE journeyId = %s AND busId = '%s' and time >= %s AND time <= %s AND (stopId = %s OR stopId = %s) GROUP BY stopId ORDER BY time",
                        journey,bus, dayToTimeStamp(day), dayToTimeStamp(day + 1),stops[1],stops[stops.length-2]
                        ));
                long time1 = -1;
                long time2 = -1;
                int totalTimeSeconds = 0;
                if(times.next()){
                    time1 = times.getLong(2);
                }
                if(times.next()){
                    time2 = times.getLong(2);
                }
                if(time1 != -1 && time2 != -1){
                    totalTimeSeconds = (int) ( (time2 - time1) / 1000);
                    dpbc.add(new PVector(Float.parseFloat(new SimpleDateFormat("HH.mm").format(new Date(time1))),totalTimeSeconds/60));
                    Collections.sort(dpbc, (v1 , v2) -> (v1.x > v2.x)? 1 : (v1.x == v2.x)? 0 : -1 );
                }
            }
        }catch (DataNotFoundException e){
            System.out.println(e.getMessage());
        }catch (SQLException e){
            System.out.print(e.getMessage());
        }
        return dpbc;

    }






}
