package project.gui.dataDisplay.dataMap;

import processing.core.*;

/**
 * MapPoint
 * A map point data typeClock containing information
 * related to bus properties and position.
 *
 * @author  Seng Leung
 * @version 1.00
 * @since   2016-04-01
 * @version 1.01
 * @since   2016-04-04
 * - Support for bus stop information
 *   when given.
 */
public class MapPoint {

    private PApplet processing;
    private PGraphics screen;

    private String busID;
    private double longitude;
    private double latitude;
    private int xPos;
    private int yPos;
    private final double LAT_CALIBRATION = 0.0021;
    private final double LON_CALIBRATION = 0.0019;
    private final double LAT_MAX = 53.410320 + LAT_CALIBRATION;
    private final double LAT_MIN = 53.269298 - LAT_CALIBRATION;
    private final double LON_MAX = -6.479567 + LON_CALIBRATION;
    private final double LON_MIN = -6.039278 - LON_CALIBRATION;
    private final double LAT_DIST = (LAT_MAX) - (LAT_MIN);
    private final double LON_DIST = Math.abs(LON_MAX) - Math.abs(LON_MIN);
    private final int RADIUS = 5;
    private final int PULSE_RADIUS = 12;
    private int radius;
    private int clickDelay = 0;
    protected boolean showInfo;

    private String[] information;
    private int colour;

    protected int stopID;

    private double flashSin;

    /**
     * MapPoint constructor.
     *
     * @param processing    PApplet.
     * @param screen        PGraphics.
     * @param busID         String of bus ID number.
     * @param latitude      Latitude value.
     * @param longitude     Longitude value.
     * @param colour        Colour value.
     */
    public MapPoint(PApplet processing, PGraphics screen, String busID, double stopID, double longitude, double latitude, int colour) {
        // Parameter assignments.
        this.processing = processing;
        this.screen = screen;
        this.busID = busID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.colour = colour;
        this.stopID = (int) stopID;

        // Random starting sine value.
        flashSin = Math.random() * 10;

        // Calculates the x and y positions on the screen in relation to their longitude and latitude.
        xPos = (int) Math.round((((Math.abs(LON_MAX) - Math.abs(longitude)) / LON_DIST) * processing.width));
        yPos = (int) Math.round((((LAT_MAX - latitude) / LAT_DIST) * processing.height));

        // String array of information about the point.
        information = new String[stopID != 0 ? 4 : 3];
        information[0] = busID;
        if (stopID != 0) {
            information[1] = "stop " + this.stopID;
        }
        information[stopID != 0 ? 2 : 1] = "lat  " + Double.toString((double)Math.round(latitude * 100000d) / 100000d);
        information[stopID != 0 ? 3 : 2] = "lon  " + Double.toString((double)Math.round(longitude * 100000d) / 100000d);
    }

    /**
     * Displays the point.
     */
    public void drawPoint() {
        screen.noStroke();
        screen.fill(colour, 200);
        if (stopID != 0) {
            // Flash for bus stops.
            screen.ellipse(xPos, yPos, (float) (Math.abs(Math.sin(flashSin) * PULSE_RADIUS )), (float) (Math.sin(flashSin) * PULSE_RADIUS ));
        } else {
            // Static random bus position.
            screen.ellipse(xPos, yPos, radius, radius);

        }
        flashSin+=0.05;
    }

    /**
     * Detects if mouse is clicked on a point.
     */
    protected void update() {
        if (processing.dist(processing.mouseX, processing.mouseY, xPos, yPos) < RADIUS) {
            radius = 10;
            if (processing.mousePressed && clickDelay > 10) {
                if (showInfo) {
                    fadeIn = false;
                    fadeOut = true;
                } else {
                    showInfo = true;
                    fadeIn = true;
                    Map.setInfoState(this.toString());
                    if (processing.mouseX < processing.width / 4 || processing.mouseY < processing.height / 4) {
                        direction = 1;
                    }
                }
                clickDelay = 0;
            }
        } else {
            radius = RADIUS;
            if (processing.mousePressed) {
                fadeIn = false;
                fadeOut = true;
            }
        }
        clickDelay++;
    }

    private int alpha = 0; // 180
    private boolean fadeIn = false;
    private boolean fadeOut = false;
    private int direction = -1;

    /**
     * Displays the information window.
     */
    public void drawInfo() {
        if (showInfo && Map.getInfoState().equals(this.toString())) {
            screen.textAlign(PConstants.LEFT, PConstants.CENTER);
            if (fadeIn && alpha < 180) {
                alpha+=20;
            }
            for (int i = 0; i < information.length; i++) {
                screen.stroke(100, alpha);
                screen.fill(255,alpha);
                screen.rect(xPos, yPos + ((20*direction) * i), (80 * direction), (20 * direction));
                screen.fill(50, alpha + 80);
                screen.text(information[(direction == -1 ? (information.length-1)-i : i)], xPos +
                           (direction == -1 ? ((80*direction) + 2) : 2), yPos + (direction*10) + ((20*i)*direction));
            }
            screen.textAlign(PConstants.LEFT, PConstants.BASELINE);
        }
        if (fadeOut) {
            if (alpha > 0) {
                alpha -=20;
            }
            if (alpha == 0) {
                showInfo = false;
                fadeOut = false;
            }
        }
    }

}
