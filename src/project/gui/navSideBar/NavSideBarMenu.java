package project.gui.navSideBar;

import processing.core.*;
import project.gui.GuiAnimationEnum;
import project.gui.GuiEventsEnum;
import project.gui.GuiStatusEnum;
import project.gui.widget.*;
import project.gui.AnimateI;

import java.util.ArrayList;
import java.util.List;

/**
 * NavSideBarMenu
 * The core class for all menu items within the navigation side bars
 * @author Samuel
 */

public class NavSideBarMenu implements AnimateI {

    PApplet processing;
    PGraphics parentGraphicWindow;

    public ArrayList<Widget> widgets;
    List<Integer> initialWidgetPositions;
    List<Integer> currentWidgetPositions;
    List<Integer> finalWidgetPositions;

    private GuiStatusEnum currentStatus;
    private GuiAnimationEnum currentAnimation;

    private int yPosOriginal;
    private int xPos, yPos;
    private int yPosFinal;

    private int titleWidth, titleHeight;
    private int lengthX, lengthY;

    private int rectXPos, rectYPos;
    private int rectHeight, rectWidth;
    private int rectExtension;

    private boolean isFocused;

    private String menuTitle;

    private double velocity;

    public NavSideBarMenu(PApplet p, PGraphics pg, String title, int xpos, int ypos, int w, int h, int lengthX, int lengthY) {
        this.processing = p;
        this.parentGraphicWindow = pg;

        this.menuTitle = title;

        this.xPos = xpos;
        this.yPos = ypos;
        this.yPosOriginal = this.yPos;
        this.yPosFinal = this.yPosOriginal + lengthY;
        this.titleWidth = w;
        this.titleHeight = h;

        this.rectXPos = this.xPos;
        this.rectYPos = this.yPos + this.titleHeight;
        this.rectWidth = parentGraphicWindow.width;
        this.rectHeight = 0;
        this.rectExtension = lengthY;

        this.lengthX = lengthX;
        this.lengthY = lengthY;
        this.rectHeight = 0;
        this.velocity = 30;

        currentStatus = GuiStatusEnum.CLOSED;
        currentAnimation = GuiAnimationEnum.PUSH_OUT_MENU_ITEM;
        this.isFocused = false;

        initialiseWidgets();
    }

    private void initialiseWidgets() {
        widgets = new ArrayList<Widget>(0);
        initialWidgetPositions = new ArrayList<>(0);
        currentWidgetPositions = new ArrayList<>(0);
        finalWidgetPositions = new ArrayList<>(0);

        initialWidgetPositions.add(yPos);
        currentWidgetPositions.add(yPos);
        finalWidgetPositions.add(yPos);
        widgets.add(new WidgetButton(processing, parentGraphicWindow, GuiEventsEnum.EXTEND_CURRENT_MENU, menuTitle, xPos, initialWidgetPositions.get(0), titleWidth, titleHeight));
    }

    public void addWidget(Widget widget, int finalPositionOffSet) {
        initialWidgetPositions.add(yPos);
        currentWidgetPositions.add(yPos);
        finalWidgetPositions.add(rectYPos + finalPositionOffSet);
        widgets.add(widget);
    }

    public void draw() {
        drawWidgets();
    }

    public void drawWidgets() {
        if (isFocused) {
            widgets.get(widgets.size()-1).draw();
            for (int i = 1; i < widgets.size()-1; i++) {
                parentGraphicWindow.fill(220);
                parentGraphicWindow.stroke(0);
                parentGraphicWindow.strokeWeight(1);
                widgets.get(i).draw();
            }
        }
        widgets.get(0).draw();
    }

    public void update() {
        widgets.get(0).update();
        if (isFocused) {
            for (int i = 1; i < widgets.size(); i++) {
                widgets.get(i).update();
            }
        }
    }

    public GuiEventsEnum getEvent() {
        GuiEventsEnum event = null;
        for(int i = 0; i < widgets.size(); i++){
            event = widgets.get(i).getEvent();
            if(event != null)
            {
                return event;
            }
        }
        return event;
    }

    public void animate() {
        if (currentAnimation == GuiAnimationEnum.REVEAL_MENU_OPTIONS) {
            double easing = Math.sin(Math.PI * (rectHeight) / (rectExtension));
            int dy = (int) (velocity * (easing * easing));  //v * e^2
            rectHeight += (dy + 2);

            if(isFocused){
                for(int i = 1; i < widgets.size(); i++){
                    if((currentWidgetPositions.get(i) + (dy+2)) <= finalWidgetPositions.get(i)) {
                        currentWidgetPositions.set(i, (currentWidgetPositions.get(i) + dy + 2));
                        widgets.get(i).setYPos(currentWidgetPositions.get(i));
                    }
                    else {
                        currentWidgetPositions.set(i, finalWidgetPositions.get(i));
                    }
                }
            }

            if (rectHeight >= rectExtension) {
                rectHeight = rectExtension;
                setStatus(GuiStatusEnum.OPEN);
            }
        } else if (currentAnimation == GuiAnimationEnum.HIDE_MENU_OPTIONS) {
            double easing = Math.sin(Math.PI * (rectHeight) / (rectExtension));
            int dy = (int) (velocity * (easing * easing));  //v * e^2
            rectHeight -= (dy + 4);

            if(isFocused){
                for(int i = 1; i < widgets.size(); i++){
                    if((currentWidgetPositions.get(i) - (dy + 4)) >= initialWidgetPositions.get(i)) {
                        currentWidgetPositions.set(i, (currentWidgetPositions.get(i) - (dy + 4)));
                        widgets.get(i).setYPos(currentWidgetPositions.get(i));
                    }
                    else {
                        currentWidgetPositions.set(i, initialWidgetPositions.get(i));
                    }
                }
            }

            if (rectHeight <= 0) {
                rectHeight = 0;
                setStatus(GuiStatusEnum.CLOSED);
                setFocus(false);
            }
        } else if (currentAnimation == GuiAnimationEnum.PUSH_OUT_MENU_ITEM) {
            double easing = Math.sin(Math.PI * (yPos - yPosOriginal) / (yPosFinal - yPosOriginal));
            int dy = (int) (velocity * (easing * easing));
            yPos += (dy + 3);
            widgets.get(0).setYPos(yPos);

            if (yPos >= yPosFinal) {
                yPos = yPosFinal;
                setStatus(GuiStatusEnum.DOWN);
            }
        } else if (currentAnimation == GuiAnimationEnum.PUSH_IN_MENU_ITEM) {
            double easing = Math.sin(Math.PI * (yPos - yPosFinal) / (yPosFinal - yPosOriginal));
            int dy = (int) (velocity * (easing * easing));
            yPos -= (dy + 3);
            widgets.get(0).setYPos(yPos);

            if (yPos <= yPosOriginal) {
                yPos = yPosOriginal;
                setStatus(GuiStatusEnum.CLOSED);
            }
        }
    }

    public void setAnimation(GuiAnimationEnum animation) {
        this.currentAnimation = animation;
    }

    public GuiAnimationEnum getAnimation() {
        return currentAnimation;
    }

    public void setStatus(GuiStatusEnum status) {
        this.currentStatus = status;
    }

    public GuiStatusEnum getStatus() {
        return currentStatus;
    }

    public void setFocus(boolean focus) {
        isFocused = focus;
    }
}
