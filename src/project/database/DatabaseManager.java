package project.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Manages all the database related classes, main class to access when requesting information for Model
 * @author Eddie
 * @since 08/3/16
 */
public class DatabaseManager {

    //---instance variables---

    private DatabaseConnector dbConnector;
    private String databaseDirectory;

    //---Constructors---

    /**
     * Constructs the DatabaseManager connected to given databaseDirectory
     * @param databaseDirectory directory of the database.
     */
    public DatabaseManager(String databaseDirectory){
        this.databaseDirectory = databaseDirectory;
        dbConnector = new DatabaseConnector(databaseDirectory);
    }

    //---Methods---

    /**
     * returns the databaseDirector
     * @return databaseDirectory String
     */
    public String getDatabaseDirectory(){
        return databaseDirectory;
    }

    /**
     * Executes a string SQL query , use if expecting results
     * @param query SQL query as string
     */
    public ResultSet makeQuery(String query){
        return dbConnector.executeQuery(query);
    }

    /**
     * Executes a string SQL query , use if expecting no results
     * @param statement SQL query as string
     */
    public void makeStatement(String statement){
        dbConnector.executeStatement(statement);
    }


}
