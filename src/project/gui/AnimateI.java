package project.gui;

/**
 * The interface for animatable objects. Not all animatable objects implement this, only ones writted by Samuel
 */
public interface AnimateI {

    void animate();

    void setAnimation(GuiAnimationEnum animation);
    GuiAnimationEnum getAnimation();

    void setStatus(GuiStatusEnum status);
    GuiStatusEnum getStatus();
}
