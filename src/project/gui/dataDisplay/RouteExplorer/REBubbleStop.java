package project.gui.dataDisplay.RouteExplorer;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * bubble intractable for to represent a bus stop
 * @author Eddie
 */
class REBubbleStop extends REAbstractBubble {

    //---Constants---
    private static final int BUS_LOCATION_X = 800;
    private static final int BUS_LOCATION_Y = 100;

    //---variables
    protected int stopNumber;
    protected REBubbleBus[] busesThatVisit;
    protected REInternalEvents type;
    protected REClock currentClock;
    protected REClock delayClockMin;
    protected REClock delayClockAvg;
    protected REClock delayClockMax;
    protected int direction;

    //---Constructor---

    /**
     * @param processing PApplet to use
     * @param screen screen to draw on
     * @param label label to display in bubble
     * @param stopNumber number this stop represents
     * @param x x center of circle
     * @param y y center of circle
     * @param radius radius of circle
     */
    REBubbleStop(PApplet processing , PGraphics screen , String label, int stopNumber, int x, int y, int radius ,int direction){
        super(processing,screen,label,x,y,radius);
        this.stopNumber = stopNumber;
        this.type = REInternalEvents.AVG;
        this.direction = direction;
    }

    /**
     * gets the right delayClock variable for associated enum typeClock
     * @param type typeClock of clock
     * @return delayClock
     */
    protected REClock getClockForEnum(REInternalEvents type){
        switch (type){
            case MIN: return delayClockMin;
            case AVG: return delayClockAvg;
            case MAX: return delayClockMax;
        }
        return null;
    }

    /**
     * sets the current clock to the passed enum typeClock
     * @param type typeClock of clock
     */
    protected void setClockFromEnum(REInternalEvents type){
        switch (type){
            case MIN: currentClock = delayClockMin; break;
            case AVG: currentClock = delayClockAvg; break;
            case MAX: currentClock = delayClockMax; break;
        }
    }

    /**
     * Creates the busBubbles associated with this stop
     * @param busIds the buses that visit this stop
     */
    protected void createBusBubbles(String[] busIds){
        busesThatVisit = new REBubbleBus[busIds.length];
        for(int i = 0 ; i < busIds.length ; i++){
            busesThatVisit[i] = new REBubbleBus(
                    processing,
                    screen,
                    busIds[i],
                    (int) (BUS_LOCATION_X + (i%3)*Math.sqrt(busIds.length)*40),
                    BUS_LOCATION_Y + 50*(i/3),
                    30
            );
        }
    }





}
