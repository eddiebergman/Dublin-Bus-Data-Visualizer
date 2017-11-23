package project.gui.guiController;

import project.gui.GuiStatusEnum;
import project.gui.AnimateI;

import java.util.ArrayList;

/**
 * GuiAnimationController
 * The class that control's which animatable objects on screen are being animated or not.
 * @Author Samuel
 */

public class GuiAnimationController {

    public static ArrayList<AnimateI> objectsToAnimate = new ArrayList<AnimateI>(0);

    public static void animate(){
        for(int i = 0; i < objectsToAnimate.size(); i++){
            objectsToAnimate.get(i).animate();
            if(objectsToAnimate.get(i).getStatus() != GuiStatusEnum.ANIMATING){
                objectsToAnimate.remove(i);
            }
        }
    }

    public static void add(AnimateI object){
        objectsToAnimate.add(object);
    }

}
