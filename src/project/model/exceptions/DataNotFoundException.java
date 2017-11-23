package project.model.exceptions;

/**
 * To be thrown if no data was found for a request
 * @author Eddie
 */
public class DataNotFoundException extends Exception {
    public DataNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
