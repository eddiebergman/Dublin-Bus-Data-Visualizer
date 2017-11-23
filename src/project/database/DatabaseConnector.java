package project.database;

import java.sql.*;

/**
 * Class used for communicating with the database directly
 * @author Eddie
 * @since 08/3/16
 */
class DatabaseConnector {

    //---Instance Variables---
    private Connection connection;
    private String fileDirectory;

    //---Constructors---

    /**
     * connects to the database
     * @param fileDirectory directory of the database (.db) to connect to
     */
    protected DatabaseConnector(String fileDirectory){
        this.fileDirectory = fileDirectory;
        connection = connectToDatabase(fileDirectory);
    }

    //---Methods---

    /**
     * executes an SQL query on the database and returns results
     * @param queryToMake an SQL query as a String
     * @return ResultSet containing data from the query
     */
    protected ResultSet executeQuery(String queryToMake){
        try {
            System.out.println("Executing : " + queryToMake);
            //ResultSet rs =  connection.createStatement().executeQuery(queryToMake);
            //System.out.println("Finished : " + queryToMake);
            //return rs;
            return connection.createStatement().executeQuery(queryToMake);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * executes a statement on the database (method to call if assuming no return)
     * @param statementToMake an SQL query as a String
     */
    protected void executeStatement(String statementToMake){
        executeQuery(statementToMake);
    }


    /**
     * Closes the connection the database
     */
    protected void close(){
       try {
           connection.close();
           System.out.print("Connection to " + fileDirectory + " closed.");
       }catch(SQLException e){
           System.out.print(e.getMessage());
       }
    }

    /**
     * Establishes a connection to the given database
     * @param fileDirectory directory of the .db
     * @return Connection object connected to the .db
     */
    private Connection connectToDatabase(String fileDirectory) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection =  DriverManager.getConnection("jdbc:sqlite:" + fileDirectory);
            System.out.println("Connection to " + fileDirectory + " established");
            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to find driver class");
            System.out.println(e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Connection to " + fileDirectory + " failed");
            return null;
        }

    }


}
