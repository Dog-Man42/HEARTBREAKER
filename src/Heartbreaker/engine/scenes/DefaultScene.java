package Heartbreaker.engine.scenes;

import Heartbreaker.engine.GameFrame;

import java.awt.*;

public class DefaultScene extends Scene{

    public Scene mainScene;

    public DefaultScene() {}


    @Override
    public boolean initialize() {
        return true;
    }

    @Override
    public void updateScene(double delta) {
        GameFrame.setCurrentScene(mainScene);
    }


    @Override
    public void draw(Graphics2D g, double delta) {

    }


}
