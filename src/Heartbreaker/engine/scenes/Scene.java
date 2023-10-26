package Heartbreaker.engine.scenes;

import Heartbreaker.engine.*;
import Heartbreaker.objects.*;

import java.awt.*;
import java.util.ArrayList;

public abstract class Scene {

    public Point origin;
    public ArrayList<BaseObject> objects = new ArrayList<>();

    public void windowResized(){
        origin = new Point(GameFrame.GAME_WIDTH/2, GameFrame.HEIGHT/2);
    }


    public void updateScene(){}

    public void updateObjects(){
        if(objects.size() > 0){
            for(BaseObject object : objects){
                object.update();
            }
        }
    }
    public abstract void draw(Graphics2D g);

    public void drawObjects(Graphics2D g){
        if(objects.size() > 0){
            for(BaseObject object : objects){
                object.draw(g);
            }
        }
    }

}
