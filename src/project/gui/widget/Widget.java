package project.gui.widget;

import processing.core.*;
import project.gui.GuiEventsEnum;

/**
 * Widget
 * Parent class of sub-widget classes.
 *
 * @author  Seng Leung
 * @version 1.00
 * @since   2016-03-13
 */

public class Widget{

    public PApplet processing;
    public GuiEventsEnum event;
    public int xPos;
    public int yPos;
    protected boolean error = false;

    /**
     * Constructs the essential widget parameters.
     *
     * @param p
     * @param xPos
     * @param yPos
     * @param event
     */
    public Widget(PApplet p, int xPos, int yPos, GuiEventsEnum event){
        this.processing = p;
        this.xPos = xPos;
        this.yPos = yPos;
        this.event = event;
    }

    /**
     * Draws.
     */
    public void draw(){

    }

    /**
     * Returns the widget event.
     *
     * @return event
     */
    public GuiEventsEnum getEvent() {
        return this.event;
    }

    public void update(){

    }

    public void setxPos(int x){
        this.xPos = x;
    }

    public int getXPos(){
        return this.xPos;
    }

    public void setYPos(int y){
        this.yPos = y;
    }

    public int getYPos(){
        return this.yPos;
    }

    public String getString(){
        return null;
    }

    public void setString(String newString){}

    public int getInt(){
        return 0;
    }

    public void makeError() {
        error = true;
    }

    public int getMin() {
        return 0;
    }

    public int getMax() {
        return 0;
    }

    public boolean[] getCheckbox() {
        return null;
    }

    public String[] getOptions(){return null;}

    public void setInt(int newValue){

    }

    public void setMinMax(int newMin , int newMax){

    }

    public void setCheckBox(boolean[] newSelections) {

    }


}