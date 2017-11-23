package project.gui.dataDisplay;

/**
 * Defines what a data display should hold
 * @author Eddie
 * @author Samuel
 */
public interface DataDisplayI {

    //method that will be called to say it should draw itself
    void draw();

    //method that will get called in a thread separate to draw to handle any numeric changes
    void update();

    //method that will be called by Controller when it needs to get its data
    void getData();

    //method that will return what type of DataDisplay this is
    DataDisplayEnum getType();

    //method that will return if this is an interactive display 
    boolean isInteractive();


}
