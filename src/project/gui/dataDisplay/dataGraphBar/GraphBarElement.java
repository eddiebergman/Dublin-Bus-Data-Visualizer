package project.gui.dataDisplay.dataGraphBar;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Created by Conal on 28/03/2016.
 */
public class GraphBarElement {
    private PApplet processing;
    private PGraphics graphBarWindow;

    private int xPos;
    private float yPos;
    private float finalHeight;
    private float currentHeight;
    private int width;
    private int value;

    private String title;

    public GraphBarElement(PApplet p, PGraphics pg, DataPointBarChart data, int x, float y, int w, float h){

        this.processing = p;
        this.graphBarWindow = pg;

        this.xPos = x;
        this.yPos = y;

        this.finalHeight = h;
        this.currentHeight = 0;
        this.width = w;

        this.value = data.dataValue;
        this.title = data.barName;

    }

    public void draw() {
        graphBarWindow.rect(xPos, yPos, width, -currentHeight);

        graphBarWindow.stroke(0);
        graphBarWindow.fill(0);
        graphBarWindow.text(title, xPos + width / 2, yPos + 15);

        if (processing.mouseX > xPos
                && processing.mouseX < xPos + width
                && processing.mouseY < yPos
                && processing.mouseY > yPos - finalHeight) {

            graphBarWindow.line(xPos, yPos - finalHeight, (int) (graphBarWindow.width * 0.1), yPos - finalHeight);
            graphBarWindow.text(value, xPos + width/2, yPos - finalHeight - 15);
        }
    }

    public void update(){
        if(currentHeight < finalHeight) {
            int velocity = 15;
            double easing = (Math.sin(Math.PI * (currentHeight/finalHeight)));
            int dy = (int) (velocity * (easing) + 2);
            currentHeight += dy;
            if(currentHeight >= finalHeight-3){
                currentHeight = finalHeight;
            }
        }
    }
}
