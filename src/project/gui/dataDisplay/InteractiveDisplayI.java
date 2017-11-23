package project.gui.dataDisplay;

/**
 * Interface for defining an interactive display
 */
public interface InteractiveDisplayI {

    /**
     * Method that allows the object to be told if a mouse event occurs
     * Will be called as long as it is a DataDisplay and returns true for isInteractive()
     * @param mouseEvent type of mouse event that occured
     */
    void mouseEventOccurred(int mouseEvent);


}
