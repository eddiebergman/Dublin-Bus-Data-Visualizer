package project.gui.dataDisplay.RouteExplorer;

import processing.core.*;
import project.gui.dataDisplay.DataDisplayEnum;
import project.gui.dataDisplay.DataDisplayI;
import project.gui.dataDisplay.dataMap.Map;

/**
 * clock to display timed data
 * @author Eddie
 */
public class REClock implements DataDisplayI {

    //---Constants---
    private static final boolean INTERACTIVE = false;
    private static final int STROKE_COLOUR = 50;
    private static final int FILL_COLOUR = 200;
    private static final int HOURS_PER_CLOCK = 12;
    private static final int ALPHA_FOR_ARCS = 255;
    private static final float ARC_FINAL_ANGLE = PConstants.PI*1.5f;
    private static final float ARC_DEGREES = PConstants.PI/(HOURS_PER_CLOCK/2);
    private static final float ACCEPTABLE_DELAY_RANGE = 0.3f;
    private static final int TEXT_PADDING = 30;
    private static final float ROTATIONAL_INCREMENTS_AMOUNT = 90;
    private static final float RADIUS_INCREMENTs_AMOUNT = (ROTATIONAL_INCREMENTS_AMOUNT/4)*3;
    private static final int MOVE_SPEED = 5;
    private static final DataDisplayEnum TYPE = DataDisplayEnum.DELAY_CLOCK;

    //---Variables---
    //Quick fix to get it to work after a few years
    private static int WHITE;
    
    private PGraphics screen;
    private PApplet processing;
    private String label;
    private int spacing;
    private int halfSpacing;
    private int xClock1;
    private int yClock1;
    private int xClock2;
    private int yClock2;
    private int diameter;
    private float currentRadius;
    private float currentDiamater;
    private float finalRadius;
    private float radiusIncrement;
    private float currentArcAngle = 0;
    private float arcIncrementAmount;
    private int legendAmountToDraw;
    private int timeStampSpacing;
    private float timeDistance;
    private float smallestDelay = -1;
    private float largestDelay = 1;
    private int[] delayData;
    private int[] legendData;
    private int[] delayColours;
    private int[] legendColours;
    private int distanceBetweenClocks;
    private PFont arialSmall;
    private PFont arialBold;
    private int worstTimeIndex;
    private int bestTimeIndex;
    public boolean lateDay;

    private static boolean reset = false;   //temp
    //---Constructor---

    /**
     * Creates a DelayClock object
     * @param delayData the averaged data to display , use ModelMain methods specifically for clocks for this
     * @param label label to display over the clocks
     * @param processing might use later for mouse over detection
     * @param screen screen to draw on
     * @param xPos where to position , slightly off atm
     * @param yPos ^^ but with y
     * @param radius radius of the clock
     */
    public REClock(PApplet processing, PGraphics screen , int[] delayData, String label , int xPos , int yPos , int radius , boolean lateDay){
        this.delayData = delayData;
        this.label = label;
        this.screen = screen;
        this.processing = processing;
        this.xClock1 = xPos+radius;
        this.yClock1 = yPos+TEXT_PADDING+radius;
        this.xClock2 = xClock1;
        this.finalRadius = radius;
        this.currentRadius = 0;
        this.radiusIncrement = radius/RADIUS_INCREMENTs_AMOUNT;
        this.arcIncrementAmount = PConstants.PI*2/ROTATIONAL_INCREMENTS_AMOUNT;
        this.diameter = radius*2;
        this.spacing = radius/2;
        this.halfSpacing = spacing/2;
        this.distanceBetweenClocks = diameter + spacing + TEXT_PADDING;
        this.yClock2 = yClock1 + distanceBetweenClocks;
        this.timeStampSpacing = spacing/3;
        this.legendAmountToDraw = (diameter*2+spacing)/TEXT_PADDING;
        this.lateDay = lateDay;
        getBoundaryDelays();
        this.arialSmall = processing.createFont("Arial",12,true);
        this.arialBold = processing.createFont("Arial Bold",14,true);
        this.delayColours = getDelayColours(delayData);
        this.legendData = getLegendData(smallestDelay,largestDelay);
        this.legendColours = getLegendColours(legendData);

        this.WHITE = processing.color(255,255,255,ALPHA_FOR_ARCS);

    }

    //---methods---

    /**
     * gets the boundaries of the the delay data
     */
    private void getBoundaryDelays(){
        worstTimeIndex = -1;
        bestTimeIndex = -1;
        for(int i = 0 ;i < delayData.length ; i++) {
            if(delayData[i] < smallestDelay){
                smallestDelay = delayData[i];
            }else if(delayData[i] > largestDelay){
                largestDelay = delayData[i];
                worstTimeIndex = i;
            }

        }
        for(int i = 0 ; i < delayData.length ; i++){
            if(i > 8){
                if(bestTimeIndex == -1 && delayData[i] >= smallestDelay*ACCEPTABLE_DELAY_RANGE && delayData[i] <= largestDelay*ACCEPTABLE_DELAY_RANGE ) {
                    bestTimeIndex = i;
                }else if(delayData[i] >= smallestDelay*ACCEPTABLE_DELAY_RANGE && delayData[i] <= largestDelay*ACCEPTABLE_DELAY_RANGE
                        && Math.abs(delayData[i]) < Math.abs(delayData[bestTimeIndex])){
                    bestTimeIndex = i;
                }
            }
        }
    }

    /**
     * Resets the clock animation
     */
    public void resetAnimation(){
        //currentRadius = 0;
        //currentArcAngle = 0;

        reset = true;
    }


    /**
     * draws the clock to the screen
     */
    public void draw(){
        //update(); multithreaded
        screen.textAlign(PConstants.LEFT);
        screen.stroke(STROKE_COLOUR,255);
        screen.strokeWeight(1);
        screen.fill(STROKE_COLOUR,255);
        screen.textFont(arialBold);
        screen.text(label,xClock1-timeDistance,yClock1-timeDistance-halfSpacing-10);
        screen.textFont(arialSmall);
        screen.text("[ 00:00am → 11:59am ]" ,xClock1-timeDistance,yClock1-timeDistance-halfSpacing-10 + arialBold.getSize());
        screen.text("[ 12:00pm → 23:59pm ]" ,xClock2-timeDistance,yClock2-timeDistance-halfSpacing);
        screen.fill(FILL_COLOUR);
        screen.ellipse(xClock1,yClock1,currentDiamater,currentDiamater);
        screen.ellipse(xClock2,yClock2,currentDiamater,currentDiamater);
        for(int time = 0; time < 12 ; time++){ //draws the 0am-11:59pm clock
            int c = delayColours[time];
            screen.fill(processing.red(c),processing.green(c),processing.blue(c), processing.alpha(c));
            screen.arc(xClock1,yClock1,currentDiamater,currentDiamater,currentArcAngle + time*ARC_DEGREES,currentArcAngle + (time+1)*ARC_DEGREES ,PConstants.PIE);
            screen.fill(STROKE_COLOUR);
            screen.textAlign(PConstants.CENTER);
            screen.text(Integer.toString(time) + "h", xClock1+timeDistance*PApplet.cos(currentArcAngle + time * ARC_DEGREES), yClock1+timeDistance*PApplet.sin(ARC_FINAL_ANGLE + time * ARC_DEGREES));
        }
        for(int time = 12; time < 24 ; time++){ //draws the 12pm-23:59pm clock
            int c = delayColours[time];
            screen.fill(processing.red(c),processing.green(c),processing.blue(c), processing.alpha(c));
            screen.arc(xClock2,yClock2,currentDiamater,currentDiamater,currentArcAngle + (time*ARC_DEGREES),currentArcAngle + ((time+1)*ARC_DEGREES),PConstants.PIE );
            screen.fill(STROKE_COLOUR);
            screen.textAlign(PConstants.CENTER);
            screen.text(Integer.toString(time) + "h", xClock2 + timeDistance*PApplet.cos(currentArcAngle + time * ARC_DEGREES), yClock2 + timeDistance*PApplet.sin(ARC_FINAL_ANGLE + time * ARC_DEGREES));
        }
        screen.stroke(0);
        screen.strokeWeight(1);
        screen.fill(245);
        screen.ellipse(xClock1,yClock1,currentRadius,currentRadius);
        screen.ellipse(xClock2,yClock2,currentRadius,currentRadius);
        if(worstTimeIndex != -1) {
            int c = delayColours[worstTimeIndex];
            screen.fill(processing.red(c), processing.green(c), processing.blue(c), processing.alpha(c));
            if (worstTimeIndex < 12) {
                screen.arc(xClock1, yClock1, currentRadius, currentRadius, currentArcAngle + (worstTimeIndex * ARC_DEGREES), currentArcAngle + ((worstTimeIndex + 1) * ARC_DEGREES), PConstants.PIE);
            } else {
                screen.arc(xClock2, yClock2, currentRadius, currentRadius, currentArcAngle + (worstTimeIndex * ARC_DEGREES), currentArcAngle + ((worstTimeIndex + 1) * ARC_DEGREES), PConstants.PIE);
            }
        }

        if(bestTimeIndex != -1) {
            int c = delayColours[bestTimeIndex];
            screen.fill(processing.red(c), processing.green(c), processing.blue(c), processing.alpha(c));
            if (bestTimeIndex < 12) {
                screen.arc(xClock1, yClock1, currentRadius, currentRadius, currentArcAngle + (bestTimeIndex * ARC_DEGREES), currentArcAngle + ((bestTimeIndex + 1) * ARC_DEGREES), PConstants.PIE);
            } else {
                screen.arc(xClock2, yClock2, currentRadius, currentRadius, currentArcAngle + (bestTimeIndex * ARC_DEGREES), currentArcAngle + ((bestTimeIndex + 1) * ARC_DEGREES), PConstants.PIE);
            }
        }
        screen.fill(20);
        drawLegend();


    }

    /**
     * handles the updating of numerics during each draw period
     */
    public void update() {
        if((currentRadius < finalRadius) || (currentArcAngle < ARC_FINAL_ANGLE)){
            animate();
        }
        currentDiamater = currentRadius*2;
        timeDistance = currentRadius+timeStampSpacing;
    }

    /**
     * Handles the animation of the DelayClock
     * @Samuel
     */
    private void animate(){
        if(!reset) {
            float dR = radiusIncrement;
            float dA = arcIncrementAmount;

            currentRadius += dR;
            currentArcAngle += dA;
        }
        else if (reset){
            float dR = -radiusIncrement;
            float dA = -arcIncrementAmount;

            currentRadius += dR;
            currentArcAngle += dA;

            if(currentRadius <=2 || currentArcAngle <= 2){
                currentRadius = 0;
                currentArcAngle = 0;
                reset = false;
            }
        }
    }

    /**
     * Gets the colour associated with each delay
     * @param delayData the delayData provided
     * @return Colour[] with mapped values of the colours for each delay
     */
    private int[] getDelayColours(int[] delayData){
        int[] colours = new int[delayData.length];
        for(int i = 0 ; i < delayData.length ; i++){
            colours[i] = getDelayColor(delayData[i]);
        }
        return colours;
    }

    /**
     * returns a shade of blue to show it's early
     * @param delay the delay to get a shade of
     * @return Color blue
     */
    private int getEarlyShade(int delay){
        if(delay == 0){ return WHITE;}
        return processing.color( 0,PApplet.map(delay,smallestDelay*ACCEPTABLE_DELAY_RANGE,smallestDelay,255,50),PApplet.map(delay,smallestDelay*ACCEPTABLE_DELAY_RANGE,smallestDelay,255,50),ALPHA_FOR_ARCS );
    }

    /**
     * returns a shade of red to show it's late
     * @param delay the delay to get a shade of
     * @return Colour red
     */
    private int getLateShade(int delay){
        if(delay == 0){ return WHITE;}
        return processing.color( PApplet.map(delay,largestDelay*ACCEPTABLE_DELAY_RANGE,largestDelay,255,50),0,0,ALPHA_FOR_ARCS );
    }

    /**
     * returns a shade of green to show its in tolerable range
     * @param delay the delay to get a shade of
     * @return Colour green
     */
    private int getWellTimedShade(int delay){
        if(delay == 0){ return WHITE;}
            return (delay <= largestDelay * ACCEPTABLE_DELAY_RANGE && delay > 0) ?
                    processing.color(0, PApplet.map(delay, 0, largestDelay * ACCEPTABLE_DELAY_RANGE, 255, 100), 0, ALPHA_FOR_ARCS) :
                    processing.color(0, PApplet.map(delay, 0, smallestDelay * ACCEPTABLE_DELAY_RANGE, 255, 100), 0, ALPHA_FOR_ARCS);
    }

    /**
     * decides which catergory the delay falls into and get appropriate colour
     * @param delay the delay to get a colour from
     * @return colour corresponding to how late/early it is
     */
    private int getDelayColor(int delay){
        if(delay >= smallestDelay*ACCEPTABLE_DELAY_RANGE && delay <= largestDelay*ACCEPTABLE_DELAY_RANGE){
            return getWellTimedShade(delay);
        }else if(delay <= smallestDelay*ACCEPTABLE_DELAY_RANGE){
            return getEarlyShade(delay);
        }else {
            return getLateShade(delay);
        }
    }

    /**
     * draws the legend to explain the colour charts
     */
    private void drawLegend(){
        screen.textAlign(PConstants.LEFT);
        screen.text("Delay",xClock1+currentRadius+TEXT_PADDING , yClock1-currentRadius-10);
        screen.textAlign(PConstants.LEFT,PConstants.TOP);
        for(int i = 0 ; i < legendAmountToDraw ; i++){
            screen.stroke(STROKE_COLOUR);
            int c = legendColours[i];
            int delayData = legendData[i];
            screen.fill(processing.red(c), processing.green(c), processing.blue(c), processing.alpha(c));
            screen.rect(xClock1 + timeDistance + TEXT_PADDING, yClock1 - currentRadius + i * (diameter * 2 + spacing) / legendAmountToDraw, 15, 15, 4);
            screen.fill(STROKE_COLOUR);
            screen.text(((delayData < 0) ? '-' : ' ') + Integer.toString(Math.abs(delayData / 60)) + "m " +
                    Integer.toString(Math.abs(delayData % 60)) + "s", xClock1 + timeDistance + TEXT_PADDING + 20, yClock1 - currentRadius + i * (diameter * 2 + spacing) / legendAmountToDraw);
        }
    }

    /**
     * Gets the Colour[] mmapped to each legend data generated
     * @param legendDataPassed the legend data to get colour mappings for
     * @return Colour[] of legend data colour values
     */
    private int[] getLegendColours(int[] legendDataPassed){
        int[] colours = new int[legendAmountToDraw];
        for(int i = 0 ; i < colours.length ; i++){
            colours[i] = getDelayColor(legendDataPassed[i]);
        }
        return colours;
    }

    /**
     * Returns equally spaced legend data depending on the size of the clock
     * @param smallest smallest delay value present
     * @param largest largest delay value present
     * @return array of equally spaced points betweeen smallest and largest
     */
    private int[] getLegendData(float smallest , float largest){
        int[] data = new int[legendAmountToDraw];
        int closestIndex = 0;
        int delay;
        for(int i = 0; i<data.length; i++){
            delay = (int) (smallest + i*(Math.abs(largest)+Math.abs(smallest))/(legendAmountToDraw-1));
            if(Math.abs(delay) < Math.abs(data[closestIndex])){
                closestIndex = i;
            }
            data[i] = delay;
        }
        data[closestIndex] = 0;
        return data;
    }

    /**
     * Needed by interface , normally should get the data it needs for itself
     */
    public void getData() {

    }

    /**
     * Returns if this is an interactive
     * @return false , is not interactive
     */
    public boolean isInteractive(){
        return false;
    }

    /**
     * @return the type of DataDisplay this is
     */
    public DataDisplayEnum getType(){
        return TYPE;
    }


}
