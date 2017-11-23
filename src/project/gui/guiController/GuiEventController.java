package project.gui.guiController;

import project.controllers.Controller;
import project.gui.GuiAnimationEnum;
import project.gui.GuiEventsEnum;
import project.gui.GuiMainController;
import project.gui.GuiStatusEnum;

/**
 * GuiEventController
 * The core class that controls all GUI interactions. This class updates the states of all other core GUI classes
 * @author Samuel
 * Eddie edited to implement query events
 */

public class GuiEventController {

    public static int nextScreenIndex = GuiMainController.currentScreenIndex;
    public static int previousScreenIndex = GuiMainController.currentScreenIndex;

    public static void guiEventOccurred(GuiEventsEnum event) {     //Temp method name, author samuel doesn't like it, change if you like
        switch (event)
        {
            case PUSH_NAV_SIDE_BAR:
                if(GuiMainController.currentNavSideBar.getStatus() == GuiStatusEnum.PUSHED_IN) {     //If the navSideBar is in, push it out
                    GuiMainController.currentNavSideBar.setAnimation(GuiAnimationEnum.PUSH_OUT_NAV_SIDE_BAR);   //set object status's
                    GuiMainController.currentNavSideBar.setStatus(GuiStatusEnum.ANIMATING);
                    GuiAnimationController.add(GuiMainController.currentNavSideBar);  //add to animation list
                }
                else if(GuiMainController.currentNavSideBar.getStatus() == GuiStatusEnum.PUSHED_OUT) {   //If the navSidebar is out, push it in
                    GuiMainController.currentNavSideBar.setAnimation(GuiAnimationEnum.PUSH_IN_NAV_SIDE_BAR);
                    GuiMainController.currentNavSideBar.setStatus(GuiStatusEnum.ANIMATING);
                    GuiAnimationController.add(GuiMainController.currentNavSideBar);
                }
                else if(GuiMainController.currentNavSideBar.getStatus() == GuiStatusEnum.ANIMATING){     //If it's in animation, switch the animation
                    if(GuiMainController.currentNavSideBar.getAnimation() == GuiAnimationEnum.PUSH_OUT_NAV_SIDE_BAR){
                        GuiMainController.currentNavSideBar.setAnimation(GuiAnimationEnum.PUSH_IN_NAV_SIDE_BAR);
                    }
                    else if(GuiMainController.currentNavSideBar.getAnimation() == GuiAnimationEnum.PUSH_IN_NAV_SIDE_BAR){
                        GuiMainController.currentNavSideBar.setAnimation(GuiAnimationEnum.PUSH_OUT_NAV_SIDE_BAR);
                    }
                }
                break;

            case SWITCH_TO_TABLESCREEN:
                if(nextScreenIndex != 0) {
                    nextScreenIndex = 0;
                    if (GuiMainController.currentNavSideBar.getStatus() == GuiStatusEnum.PUSHED_OUT) {
                        GuiMainController.currentNavSideBar.setStatus(GuiStatusEnum.ANIMATING);
                        GuiMainController.currentNavSideBar.setAnimation(GuiAnimationEnum.PUSH_IN_NAV_SIDE_BAR);
                        GuiAnimationController.add(GuiMainController.currentNavSideBar);
                    }
                    GuiMainController.animatingScreen = GuiMainController.screens.get(nextScreenIndex);
                    GuiMainController.animatingScreen.setXPos(1280);
                    GuiMainController.setStatus(GuiStatusEnum.LOADING);
                    GuiMainController.setAnimation(GuiAnimationEnum.FORWARD_SCREEN);
                    previousScreenIndex = GuiMainController.currentScreenIndex;
                }
                break;

            case SWITCH_TO_GRAPHSCREEN:
                if(nextScreenIndex != 1) {
                    nextScreenIndex = 1;
                    if (GuiMainController.currentNavSideBar.getStatus() == GuiStatusEnum.PUSHED_OUT) {

                        GuiMainController.currentNavSideBar.setStatus(GuiStatusEnum.ANIMATING);
                        GuiMainController.currentNavSideBar.setAnimation(GuiAnimationEnum.PUSH_IN_NAV_SIDE_BAR);
                        GuiAnimationController.add(GuiMainController.currentNavSideBar);
                    }
                    GuiMainController.animatingScreen = GuiMainController.screens.get(nextScreenIndex);
                    GuiMainController.animatingScreen.setXPos(1280);
                    GuiMainController.setStatus(GuiStatusEnum.LOADING);
                    GuiMainController.setAnimation(GuiAnimationEnum.FORWARD_SCREEN);
                    previousScreenIndex = GuiMainController.currentScreenIndex;
                }
                break;

            case SWITCH_TO_MAPSCREEN:
                if(nextScreenIndex != 2) {
                    nextScreenIndex = 2;
                    if (GuiMainController.currentNavSideBar.getStatus() == GuiStatusEnum.PUSHED_OUT) {
                        GuiMainController.currentNavSideBar.setStatus(GuiStatusEnum.ANIMATING);
                        GuiMainController.currentNavSideBar.setAnimation(GuiAnimationEnum.PUSH_IN_NAV_SIDE_BAR);
                        GuiAnimationController.add(GuiMainController.currentNavSideBar);
                    }
                    GuiMainController.animatingScreen = GuiMainController.screens.get(nextScreenIndex);
                    GuiMainController.animatingScreen.setXPos(1280);
                    GuiMainController.setStatus(GuiStatusEnum.LOADING);
                    GuiMainController.setAnimation(GuiAnimationEnum.FORWARD_SCREEN);
                    previousScreenIndex = GuiMainController.currentScreenIndex;
                }
                break;

            case SWITCH_TO_ROUTESCREEN:
                if(nextScreenIndex != 3) {
                    nextScreenIndex = 3;
                    if (GuiMainController.currentNavSideBar.getStatus() == GuiStatusEnum.PUSHED_OUT) {
                        GuiMainController.currentNavSideBar.setStatus(GuiStatusEnum.ANIMATING);
                        GuiMainController.currentNavSideBar.setAnimation(GuiAnimationEnum.PUSH_IN_NAV_SIDE_BAR);
                        GuiAnimationController.add(GuiMainController.currentNavSideBar);
                    }
                    GuiMainController.animatingScreen = GuiMainController.screens.get(nextScreenIndex);
                    GuiMainController.animatingScreen.setXPos(1280);
                    GuiMainController.setStatus(GuiStatusEnum.LOADING);
                    GuiMainController.setAnimation(GuiAnimationEnum.FORWARD_SCREEN);
                    previousScreenIndex = GuiMainController.currentScreenIndex;
                }
                break;

            case BACK_BUTTON:
                if(nextScreenIndex != previousScreenIndex) {
                    nextScreenIndex = previousScreenIndex;
                    if (GuiMainController.currentNavSideBar.getStatus() == GuiStatusEnum.PUSHED_OUT) {
                        GuiMainController.currentNavSideBar.setStatus(GuiStatusEnum.ANIMATING);
                        GuiMainController.currentNavSideBar.setAnimation(GuiAnimationEnum.PUSH_IN_NAV_SIDE_BAR);
                        GuiAnimationController.add(GuiMainController.currentNavSideBar);
                    }
                    GuiMainController.animatingScreen = GuiMainController.screens.get(GuiMainController.currentScreenIndex);
                    GuiMainController.animatingScreen.setXPos(0);
                    GuiMainController.currentScreen = GuiMainController.screens.get(nextScreenIndex);
                    GuiMainController.setAnimation(GuiAnimationEnum.BACK_SCREEN);
                    GuiMainController.setStatus(GuiStatusEnum.LOADING);
                }
                break;

            //these all go down to the same Controller.queryMade(event) - Eddie
            case DELAY_GRAPH_LINE:
            case OPERATIONAL_BUSES:
            case TABLE_QUERY:
            case ROUTE_QUERY:
            case MAP_QUERY_VIEW_ROUTES:
            case BUSES_AT_A_STOP:
            case MAP_QUERY_VIEW_ROUTE:
            case DELAYS_BY_TIME_OF_DAY:
            case VIEW_ARRIVAL_TIMES:
                Controller.queryMade(event);
                break;
        }
    }

    public static void extendMenu(int index){
        System.out.println(GuiMainController.currentNavSideBar.getMenuItem(index).getStatus());

        if(GuiMainController.currentNavSideBar.getMenuItem(index).getStatus() == GuiStatusEnum.CLOSED) {
            GuiMainController.currentNavSideBar.getMenuItem(index).setStatus(GuiStatusEnum.ANIMATING);
            GuiMainController.currentNavSideBar.getMenuItem(index).setAnimation(GuiAnimationEnum.REVEAL_MENU_OPTIONS);
            GuiAnimationController.add(GuiMainController.currentNavSideBar.getMenuItem(index));
            for(int i = index+1; i < GuiMainController.currentNavSideBar.getMenuSet().size(); i++){
                GuiMainController.currentNavSideBar.getMenuItem(i).setStatus(GuiStatusEnum.ANIMATING);
                GuiMainController.currentNavSideBar.getMenuItem(i).setAnimation(GuiAnimationEnum.PUSH_OUT_MENU_ITEM);
                GuiAnimationController.add(GuiMainController.currentNavSideBar.getMenuItem(i));
            }
            for(int i = 0; i < GuiMainController.currentNavSideBar.getMenuSet().size(); i++){
                if(i != index){
                    GuiMainController.currentNavSideBar.getMenuItem(i).setFocus(false);
                }
                else
                    GuiMainController.currentNavSideBar.getMenuItem(i).setFocus(true);
            }
        }
        else if(GuiMainController.currentNavSideBar.getMenuItem(index).getStatus() == GuiStatusEnum.OPEN) {
            GuiMainController.currentNavSideBar.getMenuItem(index).setStatus(GuiStatusEnum.ANIMATING);
            GuiMainController.currentNavSideBar.getMenuItem(index).setAnimation(GuiAnimationEnum.HIDE_MENU_OPTIONS);
            GuiAnimationController.add(GuiMainController.currentNavSideBar.getMenuItem(index));
            for(int i = index+1; i < GuiMainController.currentNavSideBar.getMenuSet().size(); i++){
                GuiMainController.currentNavSideBar.getMenuItem(i).setStatus(GuiStatusEnum.ANIMATING);
                GuiMainController.currentNavSideBar.getMenuItem(i).setAnimation(GuiAnimationEnum.PUSH_IN_MENU_ITEM);
                GuiAnimationController.add(GuiMainController.currentNavSideBar.getMenuItem(i));
            }
        }
        else if(GuiMainController.currentNavSideBar.getMenuItem(index).getStatus() == GuiStatusEnum.ANIMATING){
            if(GuiMainController.currentNavSideBar.getMenuItem(index).getAnimation() == GuiAnimationEnum.REVEAL_MENU_OPTIONS){
                GuiMainController.currentNavSideBar.getMenuItem(index).setAnimation(GuiAnimationEnum.HIDE_MENU_OPTIONS);
                for(int i = index+1; i < GuiMainController.currentNavSideBar.getMenuSet().size(); i++){
                    GuiMainController.currentNavSideBar.getMenuItem(i).setAnimation(GuiAnimationEnum.PUSH_IN_MENU_ITEM);

                    if(GuiMainController.currentNavSideBar.getMenuItem(i).getStatus()==GuiStatusEnum.DOWN){
                        GuiMainController.currentNavSideBar.getMenuItem(i).setStatus(GuiStatusEnum.ANIMATING);
                        GuiAnimationController.add(GuiMainController.currentNavSideBar.getMenuItem(i));
                    }
                }
            }
            else if (GuiMainController.currentNavSideBar.getMenuItem(index).getAnimation() == GuiAnimationEnum.HIDE_MENU_OPTIONS){
                GuiMainController.currentNavSideBar.getMenuItem(index).setAnimation(GuiAnimationEnum.REVEAL_MENU_OPTIONS);
                for(int i = index+1; i < GuiMainController.currentNavSideBar.getMenuSet().size(); i++){
                    GuiMainController.currentNavSideBar.getMenuItem(i).setAnimation(GuiAnimationEnum.PUSH_OUT_MENU_ITEM);

                    if(GuiMainController.currentNavSideBar.getMenuItem(i).getStatus()==GuiStatusEnum.CLOSED){
                        GuiMainController.currentNavSideBar.getMenuItem(i).setStatus(GuiStatusEnum.ANIMATING);
                        GuiAnimationController.add(GuiMainController.currentNavSideBar.getMenuItem(i));
                    }
                }
            }
        }
    }

}
