package project.gui.widget;

import processing.core.*;
import project.gui.GuiEventsEnum;

/**
 * WidgetTextInput
 * Creates a text input box.
 *
 * @author  Seng Leung
 * @version 1.00
 * @since   2016-03-14
 * @version 1.01
 * @since   2016-03-21
 * - Implemented a flashing caret which
 *   adjusts as the user types.
 * @version 1.02
 * @since   2016-03-23
 * - Key entry only works if mouse is
 *   clicked inside the widget.
 * - Feature to restrict keys to numbers.
 * - Disabled need to press enter to
 *   confirm text entry.
 * - Improved code readability.
 * @version 1.03
 * @since   2016-03-24
 * - When restricting input to numbers,
 *   A, B, C, D and X are enabled.
 * - Letters are automatically converted
 *   to uppercase.
 * @version 1.04
 * @since   2016-03-30
 * - Added subtle animations.
 * @version 1.05
 * @since   2016-04-01
 * - Added error method makeError(). Turns the
 *   text field red until the user clicks on it.
 * @version 1.06
 * @since   2016-04-04
 * - Hover over colour change.
 */
public class WidgetTextInput extends Widget {

    private PGraphics pg;
    private String name;
    private int xSize;
    private int ySize;
    private PFont arial;
    private boolean restrictNumbers = false;

    /**
     * Constructor.
     * @param processing    Main PApplet
     * @param pg            PGraphics
     * @param event         Event enumerator
     * @param name          Text field label
     * @param xPos          x coordinates
     * @param yPos          y coordinates
     * @param xSize         length
     * @param ySize         width
     */
    public WidgetTextInput(PApplet processing, PGraphics pg,
                           GuiEventsEnum event, String name,
                           int xPos, int yPos,
                           int xSize, int ySize) {
        super(processing, xPos, yPos, event);
        this.event = event;
        this.pg = pg;
        this.name = name;
        this.xSize = xSize;
        this.ySize = ySize;
        consolas = processing.createFont("Consolas", 12);
        arial = processing.createFont("Arial", 12);
    }

    /**
     * Constructor with number restriction.
     * @param processing      Main PApplet
     * @param pg              PGraphics
     * @param event           Event enumerator
     * @param name            Text field label
     * @param xPos            x coordinates
     * @param yPos            y coordinates
     * @param xSize           length
     * @param ySize           width
     * @param restrictNumbers enable only number keys
     */
    public WidgetTextInput(PApplet processing, PGraphics pg,
                           GuiEventsEnum event, String name,
                           int xPos, int yPos,
                           int xSize, int ySize,
                           boolean restrictNumbers) {
        this(processing, pg, event, name,xPos, yPos,xSize, ySize);
        this.restrictNumbers = restrictNumbers;
    }

    private final int keyDelay = 5;
    private int keyCount = 0;
    private int caretCount = 0;
    private PFont consolas;
    private String textInput = "";
    private String whiteSpace = "";
    private boolean keyParsed = false;
//    private boolean completed = false;
    private boolean keyEnable = false;
    private boolean hoverColour = false;


    /**
     * Draws the text input box.
     */
    public void draw () {
        //logicEnable();

        //logicKeystroke();

        // Title
        pg.fill(200);
        pg.rect(xPos, yPos, xSize, ySize / 2);
        pg.textAlign(PConstants.LEFT, PConstants.CENTER);
        pg.fill(0);
        pg.textFont(arial);
        pg.text(name, xPos + 2, yPos + ySize / 4);

        // Text input field
        if (keyEnable || hoverColour) {
            pg.fill(245);
        } else if (error) {
            pg.fill(255, 204, 204);
        } else {
            pg.fill(230);
        }
        pg.rect(xPos, yPos + ySize / 2, xSize, ySize / 2);
        pg.fill(0);
        pg.textAlign(PConstants.LEFT, PConstants.CENTER);
        //pg.textFont(consolas);

        logicCaret();
    }

    public void update(){
        logicEnable();
        logicKeystroke();
    }

    /**
     * If mouse is clicked inside the widget, key entry is enabled.
     */
    private void logicEnable() {
        if (processing.mouseX > xPos && processing.mouseX < xPos + xSize &&
            processing.mouseY > yPos + (ySize /2) && processing.mouseY < yPos + ySize
            && !processing.mousePressed) {
            hoverColour = true;
        } else {
            hoverColour = false;
        }
        if (processing.mouseX > xPos && processing.mouseX < xPos + xSize &&
            processing.mouseY > yPos + (ySize /2) && processing.mouseY < yPos + ySize &&
            processing.mousePressed) {
                keyEnable = true;
                error = false;
        }
        if ((processing.mouseX < xPos || processing.mouseX > xPos + xSize ||
             processing.mouseY < + yPos + (ySize /2) || processing.mouseY > yPos + ySize) && processing.mousePressed) {
                keyEnable = false;
        }
    }

    /**
     * Detects keystrokes.
     */
    private void logicKeystroke() {
        if (/*!completed &&*/ keyEnable){
            if (processing.key == PConstants.BACKSPACE && processing.keyPressed &&
                    keyCount > keyDelay && !keyParsed) {
                if (textInput.length() > 0) {
                    textInput = textInput.substring(0, textInput.length() - 1);
                    keyCount = 0;
                    caretCount = 0;
                }
            } else if (processing.key == PConstants.DELETE && processing.keyPressed && !keyParsed) {
                textInput = "";
            } else if (processing.keyCode != PConstants.SHIFT &&
                    processing.keyCode != PConstants.CONTROL &&
                    processing.keyCode != PConstants.ALT &&
                    processing.key != PConstants.BACKSPACE &&
                    processing.key != PConstants.ENTER &&
                    processing.key != PConstants.TAB &&
                    processing.key != PConstants.RETURN &&
                    processing.keyPressed && !keyParsed) {
                if (restrictNumbers) {
                    if ((processing.key >= '0' && processing.key <= '9') ||
                            processing.key == 'a' || processing.key == 'A' ||
                            processing.key == 'b' || processing.key == 'B' ||
                            processing.key == 'c' || processing.key == 'C' ||
                            processing.key == 'd' || processing.key == 'D' ||
                            processing.key == 'x' || processing.key == 'X' ||
                            processing.key == ' ') {
                        textInput = textInput + processing.key;
                    }
                } else {
                    textInput = textInput + processing.key;
                }
                keyCount = 0;
                caretCount = 0;
                keyParsed = true;
            }
            if (!processing.keyPressed) {
                keyParsed = false;
            }
        }
    }

    /**
     * Flashing caret when mouse is clicked inside widget.
     */
    private void logicCaret() {
        if (restrictNumbers) {
            textInput = textInput.toUpperCase();
        }
        if (keyEnable) {
            whiteSpace = "";
//            for (int i = 0; i < textInput.length(); i++) {
//                whiteSpace += " ";
//            }
            if (caretCount % 60 < 30) {
                pg.fill(0);
            } else {
                pg.fill(255, 0);
            }
              pg.noStroke();
              pg.rect(xPos + pg.textWidth(textInput) + 3, yPos + ySize/2 + 3, 1, 15);
              pg.stroke(150);
//            pg.text(whiteSpace + "|", xPos + 2, yPos + (ySize * 3 / 4));
            pg.fill(0);
            caretCount++;
        }
        pg.text(textInput, xPos + 2, yPos + (ySize * 3 / 4));
        keyCount++;
    }

    /**
     * Returns the text input the user has entered.
     *
     * @return textInput
     */
    @Override
    public String getString() {
        return textInput;
    }

    public void setString(String newString){
        textInput = newString;
    }

    /**
     * Turns the text field red until the user clicks on it.
     */
    @Override
    public void makeError() {
        this.error = true;
    }

}
