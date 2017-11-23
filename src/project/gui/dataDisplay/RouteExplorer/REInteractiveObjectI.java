package project.gui.dataDisplay.RouteExplorer;

/**
 * interface to define something interactive
 * @author Eddie
 */
interface REInteractiveObjectI {

    //should be called every frame to handle all logic
    void update();

    //to draw themselves
    void draw();

    //to reposition
    void moveTo(int destX , int destY);

    //to know its position relative to the mouse
    boolean isMouseOver();

    //to know if it has been clicked before
    boolean isSelected();

    //should be used to indicate this object was clicked
    void clicked();

    //returns if it is at a sepcified location
    boolean isAtPosition(int x , int y);

    //tell the intractble to move to a new location
    void newDestination(int x , int y);

}
