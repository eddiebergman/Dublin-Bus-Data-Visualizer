package project.gui;

/**
 * Different states for the GUI
 * @author Samuel
 */
public enum GuiStatusEnum {
    //GuiMainController
    READY, LOADING,

    //NavSideBar
    PUSHED_OUT, PUSHED_IN, ANIMATING,

    //NavSideBar Menu Items
    CLOSED, OPEN, DOWN   //Also uses Animating from NavSideBar events
}
