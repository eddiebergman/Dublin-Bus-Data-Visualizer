package project.gui.navSideBar;

import processing.core.*;
import project.gui.GuiAnimationEnum;
import project.gui.GuiEventsEnum;
import project.gui.GuiStatusEnum;
import project.gui.AnimateI;
import project.gui.guiController.GuiEventController;
import project.gui.widget.*;

import java.util.ArrayList;

/**
 * NavSideBar
 * The core class for all navigation side bars in the program
 * @author Samuel
 */

public class NavSideBar implements AnimateI {

    public PApplet processing;
    public PGraphics sideBarWindowGraphics;

    ArrayList<Widget> widgets;
    ArrayList<NavSideBarMenu> menus;

    public GuiAnimationEnum currentAnimation;   //change from public when events setup
    public GuiStatusEnum currentStatus;  //change from public when events setup

    private final float EASING = 0.15f;
    private int xPos, yPos;

    public int windowWidth, windowHeight;

    private PImage dublinBusLogo;

    public NavSideBar(PApplet p){
        this.processing = p;
        windowWidth = 300;
        windowHeight = 720;

        currentAnimation = GuiAnimationEnum.PUSH_OUT_NAV_SIDE_BAR;
        currentStatus = GuiStatusEnum.PUSHED_IN;

        sideBarWindowGraphics = processing.createGraphics(windowWidth, windowHeight);

        xPos = -windowWidth;
        yPos = 0;

        dublinBusLogo = processing.loadImage("navigation/dublinBusLogo.png");
        dublinBusLogo.resize(117,30);
        initialiseWidgets();
        initialiseMenus();
    }

    private void initialiseWidgets(){
        int yPos = 50;
        int buttonWidth = 22;
        int colorDefault = processing.color(230);
        int colorHover = processing.color(220);
        widgets = new ArrayList<>(0);
        widgets.add(new WidgetButton(processing, sideBarWindowGraphics, GuiEventsEnum.SWITCH_TO_ROUTESCREEN, "Explorer", 1, yPos, 298, 22, colorDefault, colorHover));
        widgets.add(new WidgetButton(processing, sideBarWindowGraphics, GuiEventsEnum.SWITCH_TO_TABLESCREEN, "Table", 1, yPos + (buttonWidth*1), 298, 22, colorDefault, colorHover));
        widgets.add(new WidgetButton(processing, sideBarWindowGraphics, GuiEventsEnum.SWITCH_TO_GRAPHSCREEN, "Graph", 1, yPos + ((buttonWidth*2)), 298, 22, colorDefault, colorHover));
        widgets.add(new WidgetButton(processing, sideBarWindowGraphics, GuiEventsEnum.SWITCH_TO_MAPSCREEN, "Map", 1, yPos + (buttonWidth*3), 298, 22, colorDefault, colorHover));
    }

    private void initialiseMenus(){
        menus = new ArrayList<NavSideBarMenu>();
    }

    public void draw(){
        sideBarWindowGraphics.beginDraw();
        sideBarWindowGraphics.fill(220);
        sideBarWindowGraphics.stroke(0);
        sideBarWindowGraphics.strokeWeight(1);
        sideBarWindowGraphics.rect(1, 1, windowWidth-2, windowHeight-3);

        // Dublin Bus logo
        sideBarWindowGraphics.fill(240);
        sideBarWindowGraphics.rect(1, 1, 298, 37);
        sideBarWindowGraphics.image(dublinBusLogo, 5,5);


        drawScreenWidgets();

        drawMenuItems();


        sideBarWindowGraphics.endDraw();
        processing.image(sideBarWindowGraphics, xPos, yPos);
    }

    private void drawScreenWidgets(){
        for(int i = 0; i < widgets.size(); i++){
            sideBarWindowGraphics.fill(220);
            sideBarWindowGraphics.stroke(0);
            sideBarWindowGraphics.strokeWeight(1);
            widgets.get(i).draw();
        }
    }

    private void drawMenuItems(){
        for(int i = 0; i < menus.size(); i++){
            sideBarWindowGraphics.fill(220);
            sideBarWindowGraphics.stroke(0);
            sideBarWindowGraphics.strokeWeight(1);
            menus.get(i).draw();
        }
    }

    public void animate(){
        if(currentAnimation == GuiAnimationEnum.PUSH_OUT_NAV_SIDE_BAR){
            int dx = (int) ((0- xPos)*EASING);
            xPos += dx+1;

            if(xPos >= 0) {
                xPos = 0;
                setStatus(GuiStatusEnum.PUSHED_OUT);
            }
        }

        else if(currentAnimation == GuiAnimationEnum.PUSH_IN_NAV_SIDE_BAR)
        {
            int dx = (int) ((-windowWidth - xPos)*EASING);
            xPos += (dx-1);

            if(xPos <= -windowWidth){
                xPos = -windowWidth;
                setStatus(GuiStatusEnum.PUSHED_IN);
            }
        }
    }

    public void setAnimation(GuiAnimationEnum animationEnum){
        this.currentAnimation = animationEnum;
    }

    public GuiAnimationEnum getAnimation(){
        return currentAnimation;
    }

    public void setStatus(GuiStatusEnum status){
        this.currentStatus = status;
    }

    public GuiStatusEnum getStatus(){
        return currentStatus;
    }

    public GuiEventsEnum getEvents(){
        if(currentStatus == GuiStatusEnum.PUSHED_IN){
            return null;
        }
        GuiEventsEnum currentEvent;
        for(int i = 0;i < menus.size(); i++){       //Events of menu items
            currentEvent = menus.get(i).getEvent();
            if(currentEvent != null){
                GuiEventController.extendMenu(i);   //Might need to move this up above to another class
                return currentEvent;
            }
        }

        for(int i = 0; i < widgets.size(); i++){        //events of widgets
            currentEvent = widgets.get(i).getEvent();
            if(currentEvent != null){
                return currentEvent;
            }
        }
        return null;
    }

    public void update(){
        for(int i = 0; i < widgets.size(); i++){
            widgets.get(i).update();
        }

        for(int i = 0; i < menus.size(); i++){
            menus.get(i).update();
        }
    }

    public int getXPos(){
        return xPos;
    }

    public NavSideBarMenu getMenuItem(int i){
        return menus.get(i);
    }
    public ArrayList getMenuSet(){
        return menus;
    }

    public void addWidget(Widget widget){
        widgets.add(widget);
    }

    public void addMenuItem(NavSideBarMenu menu){
        menus.add(menu);
    }

    public void addWidgetMenuItem(Widget widget, int index, int finalPos){
        menus.get(index).addWidget(widget, finalPos);
    }
}
