package project;

import processing.event.MouseEvent;
import project.gui.GuiMainController;
import processing.core.*;

/**
 * ArtificialProcessingMain
 * Emulates a Processing main class.
 *
 * @author  Eddie Bergman
 * @author  Conal Cosgrove
 * @author  Seng Leung
 * @author  Samuel McCann
 * @since   2016-03-08
 */
public class ArtificialProcessingMain {

    public static PApplet processing;
    public static GuiMainController guiMainController;

    private static int eventMouseWheel = 0;
    private static int timer = 0;
    private static int prevCount;

    //Thread Variables
    public static Thread initialDrawThread;

    int frameRateInitialDrawThread = 60;
    int lastCallInitialDrawThread = 0;
    //-------------

    /**
     * Initialises the GUI controller and multithreading.
     *
     * @param p Processing PApplet
     */
    public ArtificialProcessingMain(PApplet p) {
        this.processing = p;
        guiMainController = new GuiMainController(processing);
        initialDrawThread = new Thread(new initialDrawThread());    //Thread to draw up loading screen. I put it in the constructor because I want it to run as soon as possible

    }

    /**
     * Performs GUI controller and multithreading methods.
     */
    public void setup() {
        initialDrawThread.start();
        guiMainController.setup();
        //ModelMain.makeMap();
        //ModelMain.runTestQuery();

        //make this last thing to run in setup();
        initialDrawThread.interrupt();
    }

    /**
     * Displays the GUI elements.
     */
    public void draw() {
        guiMainController.draw();
    }

    public void mouseReleased() {
        guiMainController.mouseReleasedThread.run();
    }

    public void mousePressed(){
    }

    public void mouseMoved(){
    }

    public void mouseDragged(){
    }

    public void mouseWheel(MouseEvent event){
        eventMouseWheel += event.getCount();
    }

    /**
     * A timing algorithm is required for obtaining the
     * scroll wheel event as the mouseWheel() method does
     * not detect inactivity of scroll wheel.
     *
     * @return eventMouseWheel
     */
    public static int getMouseWheel() {
        if (timer % 6 == 0) {
            prevCount = eventMouseWheel;
        }
        timer++;
        if (prevCount == eventMouseWheel) {
            return 0;
        } else if (prevCount > eventMouseWheel) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Provides multithreading capabilities.
     */
    private class initialDrawThread implements Runnable {
        boolean drawing = true;
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": The initialDrawThread is running");
            try {
            while(drawing) {
                //-----------------Draw Code--------------------

                //screenLoading.draw();
                processing.background(255, 255, 255);
                //--------------------------------------------

                //-----------------Frame Limiter------------------
                int timeToWait = 1000 / frameRateInitialDrawThread - (processing.millis() - lastCallInitialDrawThread);
                if (timeToWait > 1) {

                        //Sleep the thread for long enough to match framerate
                        Thread.currentThread().sleep(timeToWait);
                }

                lastCallInitialDrawThread = processing.millis();

                //End of initialDrawThread Main Loop
            }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ": Interrupting Thread");
                drawing = false;
            }
            System.out.println(Thread.currentThread().getName() + ": The initialDrawThread is ending");
        }
    }

}
