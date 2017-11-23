import processing.core.*;
import processing.event.MouseEvent;
import project.ArtificialProcessingMain;
import project.gui.dataDisplay.dataMap.MapBus;

/**
 * MainExecute
 * Executes the entire program.
 *
 * @author  Eddie Bergman
 * @author  Conal Cosgrove
 * @author  Seng Leung
 * @author  Samuel McCann
 * @since   2016-03-08
 */
public class MainExecute extends PApplet {

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int FRAME_RATE = 60;

    ArtificialProcessingMain artificialProcessingMain;

    /**
     * Displays Processing as a Java application.
     *
     * @param args The class of MainExecute.
     */
    public static void main(String[] args) {
        PApplet.main(new String[]{/*"--present",*/ "MainExecute"});
    }

    /**
     * Declares the screen width and height.i
     */
    public void settings() {
        size(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    /**
     * Initialises the fundamental methods.
     */
    public void setup() {
        frameRate(FRAME_RATE);
        artificialProcessingMain = new ArtificialProcessingMain(this);
        artificialProcessingMain.setup();
    }

    /**
     * Displays the GUI elements.
     */
    public void draw() {
        artificialProcessingMain.draw();
    }

    public void mouseReleased() {
        artificialProcessingMain.mouseReleased();
    }

    public void mousePressed() {
        artificialProcessingMain.mousePressed();
    }

    public void mouseMoved() {
        artificialProcessingMain.mouseMoved();
    }

    public void mouseDragged() {
        artificialProcessingMain.mouseDragged();
    }

    public void mouseWheel(MouseEvent event){
        artificialProcessingMain.mouseWheel(event);
    }

}