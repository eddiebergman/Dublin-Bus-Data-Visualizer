package project.gui.dataDisplay.dataTable;

import processing.core.*;
import project.ArtificialProcessingMain;
import project.gui.GuiMainController;
import project.gui.ScreenNames;
import project.gui.dataDisplay.DataDisplayEnum;
import project.gui.dataDisplay.DataDisplayI;
import project.gui.widget.Widget;
import project.model.ModelMain;

import java.util.ArrayList;

/**
 * Table
 * Creates a scrollable data table.
 *
 * @author  Seng Leung
 * @version 1.00
 * @since   2016-03-26
 */
public class Table implements DataDisplayI{

    private static final DataDisplayEnum TYPE = DataDisplayEnum.TABLE;

    //talk to eddie to learn more , nice way of the screen letting the data display know whats going on with the mouse
    private static final boolean INTERACTIVE = false;

    private final float TEXT_SPACING = 14;
    private final float LINES_PER_WINDOW = 52;
    private final int SCROLL_SPEED = 60;
    private final int SLIDER_SIZE = 12;
    private final int COLUMN_SPACING = 120;
    private final int DATA_SPACING_OFFSET = 3;
    private final int LABEL_WIDTH = 115;
    private final int LABEL_HEIGHT = 15;
    private final int START_X_POS = 340;
    private final float SCALE_FACTOR_CONSTANT = 0.08657f;

    private PApplet processing;
    PGraphics screen;
    private String[][] data;
    private String[] label;

    private float handleX;
    private float handleY;
    private float handleW = 20;
    private float handleH = 50;
    private int handleFill = 150;
    private int windowWidth;
    private float[] xPos;
    private float[] yPos;
    private float sizeScaleFactor;
    private float textStart;
    private float nodeX, nodeY, nodeYmax, nodeYmin;
    private PFont arial;

    /**
     * Constructor.
     *
     * @param processing PApplet
     * @param screen     PGraphics
     * @param data       2-D array of containing data to be displayed
     * @param label      1-D array of data names
     */
    public Table(PApplet processing, PGraphics screen, String[][] data, String[] label) {
        this.processing = processing;
        this.screen = screen;
        this.data = data;
        this.label = label;
        set();
    }

    /**
     * Sets the values needed for the table.
     * Values are calculated in proportion to the data array length.
     * Highly mathematical.
     */
    private void set() {
        sizeScaleFactor = ((data[0].length / LINES_PER_WINDOW) +
                          ((data[0].length / LINES_PER_WINDOW) * SCALE_FACTOR_CONSTANT));

        xPos = new float[data[0].length];
        yPos = new float[data[0].length];

        handleX = processing.width - SLIDER_SIZE - 1;
        handleY = 0;

        nodeX = START_X_POS;
        nodeYmax = processing.height * (sizeScaleFactor / 2);
        nodeYmin = (processing.height / 2) - ((sizeScaleFactor - 1) * ((processing.height / 2) - handleH));
        nodeY = nodeYmax;

        windowWidth = processing.width - SLIDER_SIZE - 1;
        textStart = -(nodeYmax) + TEXT_SPACING + LABEL_HEIGHT;

        // Position of all data values
        for (int i = 0; i < data[0].length; i++) {
            yPos[i] = textStart + (i * TEXT_SPACING);
        }

        // Font
        arial = processing.createFont("Arial", 12);
    }

    /**
     * Draws the table and performs calculations
     * based on user input.
     */
    public void draw() {
        drawData();
        scrollBar();
        mouseWheelKeyboard();
        keyboardShortcuts();
    }

    /**
     * Drawing of data array and label array.
     */
    private void drawData() {
        // Draw array of data
        screen.fill(0);
        screen.textSize(12);
        screen.textFont(arial);
        screen.textAlign(PConstants.LEFT, PConstants.BASELINE);
        try {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    // Only draws data within viewable range
                    if (nodeY + yPos[j] > 0 && nodeY + yPos[j] < processing.height + TEXT_SPACING) {
                        screen.text(data[i][j], nodeX + (COLUMN_SPACING * i) + DATA_SPACING_OFFSET, nodeY + yPos[j]);
                    }
                }
            }
        } catch (Exception e) {
        }

        // Draw array of labels
        screen.stroke(180);
        for (int i = 0; i < label.length; i++) {
            screen.fill(230);
            screen.rect(START_X_POS + (i * COLUMN_SPACING), 0, LABEL_WIDTH, LABEL_HEIGHT);
            screen.fill(0);
            screen.text(label[i], START_X_POS + 2 + (i * COLUMN_SPACING) + DATA_SPACING_OFFSET, 12);
        }
    }

    /**
     * Scroll bar detection and scaling of contents.
     */
    private void scrollBar() {
        //Scroll bar background
        screen.noStroke();
        screen.fill(220);
        screen.rect(windowWidth, 0, processing.width - 2, processing.height - 1);

        //Scroll bar
        screen.fill(handleFill);
        screen.rect(handleX, handleY, handleW, handleH);

        // Scroll bar detection and scaling
        if (processing.mouseX > processing.width - SLIDER_SIZE - 30 && processing.mouseX < processing.width &&
                processing.mouseY > (handleH / 2) && processing.mouseY < processing.height - (handleH / 2) &&
                processing.mousePressed) {
            handleY = processing.mouseY - handleH / 2;
            nodeY = (handleY) + ((handleY - (processing.height / 2)) * (-sizeScaleFactor));
            handleFill = 150;
        } else if (processing.mouseX > handleX && processing.mouseX < handleX + handleW &&
                processing.mouseY > handleY && processing.mouseY < handleY + handleH) {
            handleFill = 165;
        } else {
            handleFill = 180;
        }
    }

    /**
     * Mouse wheel and keyboard detection for scrolling.
     */
    private void mouseWheelKeyboard() {
        if (ArtificialProcessingMain.getMouseWheel() == 1 ||
                (processing.keyCode == PConstants.DOWN && processing.keyPressed)) {
            if (nodeY - SCROLL_SPEED < nodeYmin) {
                nodeY = nodeYmin;
            } else {
                nodeY -= SCROLL_SPEED;
            }
        }
        if (ArtificialProcessingMain.getMouseWheel() == -1 ||
                (processing.keyCode == PConstants.UP && processing.keyPressed)) {
            if (nodeY + SCROLL_SPEED > nodeYmax) {
                nodeY = nodeYmax;
            } else {
                nodeY += SCROLL_SPEED;
            }
        }
        handleY = ((nodeY - sizeScaleFactor * (processing.height / 2)) / (1 - sizeScaleFactor));
    }

    /**
     * Keyboard shortcuts.
     * RIGHT ARROW to scroll to top.
     * LEFT ARROW to scroll to bottom.
     */
    private void keyboardShortcuts() {
        if (processing.keyCode == PConstants.RIGHT && processing.keyPressed) {
            nodeY = nodeYmin;
        }
        if (processing.keyCode == PConstants.LEFT && processing.keyPressed) {
            nodeY = nodeYmax;
        }
    }

    public void setLabels(String[] newLabels){
        this.label = newLabels;
    }

    public void setData(String[][] newData){
        this.data = newData;
    }

    //METHODS FOR INTERFACE---------------
    public void update() {

    }

    /**
     * Gets the data needed for this datadisplay
     * @author Seng
     * @author Eddie
     */
    public void getData() {
        ArrayList<Widget> widgets = GuiMainController.getNavSideBar(ScreenNames.TABLE).getMenuItem(0).widgets;
        int limit = widgets.get(6).getInt();
        String[] routeNumbers = widgets.get(1).getString().split(" ");
        String[] stopNumbers = widgets.get(2).getString().split(" ");
        boolean atStop = widgets.get(3).getCheckbox()[0];
        int hour1 = widgets.get(4).getMin();
        int hour2 = widgets.get(4).getMax();
        int day1 = widgets.get(5).getMin();
        int day2 = widgets.get(5).getMax();
        boolean[] choices = widgets.get(7).getCheckbox();
        String[] options = widgets.get(7).getOptions();
        ArrayList<String> chosenOptionsList = new ArrayList<>();
        for(int i = 0 ; i < options.length ; i++){
            System.out.println(options[i]  +" " + choices[i]);
            if(choices[i] == true){
                chosenOptionsList.add(options[i]);
            }
        }
        String[] chosenOptions = chosenOptionsList.toArray(new String[chosenOptionsList.size()]);
        this.label = chosenOptions;
        data = ModelMain.populateTableData(chosenOptions,limit,routeNumbers,stopNumbers,atStop,hour1,hour2,day1,day2);
        set();
    }

    public DataDisplayEnum getType() {
        return TYPE;
    }

    public boolean isInteractive(){
        return INTERACTIVE;
    }
    //METHODS FOR INTERFACE---------------

}
