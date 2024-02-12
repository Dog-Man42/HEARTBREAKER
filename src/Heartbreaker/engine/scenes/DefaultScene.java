package Heartbreaker.engine.scenes;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.scenes.Level;
import Heartbreaker.scenes.MainMenu;

import java.awt.*;

public class DefaultScene extends Level{

    public Level mainScene;

    public DefaultScene(boolean scrollable) {
        super(scrollable);
    }


    @Override
    public boolean initialize() {
        return true;
    }

    @Override
    public void updateScene() {
        GameFrame.setCurrentScene(mainScene);
    }


    @Override
    public void draw(Graphics2D g) {

    }

    @Override
    public void levelBeaten() {

    }
}
