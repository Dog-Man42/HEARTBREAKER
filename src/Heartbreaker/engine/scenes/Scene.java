package Heartbreaker.engine.scenes;

import Heartbreaker.engine.*;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionManager;
import Heartbreaker.objects.*;

import java.awt.*;
import java.util.ArrayList;

public abstract class Scene {

    public Point origin;
    public CollisionManager collisionManager;

    private ArrayList<GameObject> objects = new ArrayList<>();
    private ArrayList<GameObject> killedObjects = new ArrayList<>();


    public void windowResized(){
        origin = new Point(GameFrame.GAME_WIDTH/2, GameFrame.GAME_HEIGHT/2);
    }
    public abstract boolean initialize();


    public void updateScene(){}

    public void updateObjects(){
        //System.out.println("Object Count: " + objects.size());
        //System.out.println("Collider Count: " + collisionManager.size());
        if(objects.size() > 0){
            int length = objects.size();
            for(int i = 0; i < length; i++){
                GameObject object = objects.get(i);
                if(!killedObjects.contains(object)){
                    object.update();
                }
            }
        }
        collisionManager.updateColliders();
        clearRemovedObjects();
    }
    public abstract void draw(Graphics2D g);

    public void drawObjects(Graphics2D g){
        if(objects.size() > 0){
            for(int i = objects.size() - 1; i >= 0; i--){
                GameObject object = objects.get(i);
                if(object.isDrawnByScene()) {
                    object.draw(g);
                }
            }
        }
    }

    public void addObject(GameObject gameObject){
        objects.add(gameObject);
        if(gameObject instanceof Collider){
            collisionManager.addCollider((Collider) gameObject);
        }
    }
    public void removeObject(GameObject gameObject){
        killedObjects.add(gameObject);
    }

    public void clearRemovedObjects(){
        if(killedObjects.size() > 0 ){
            for(int i = killedObjects.size() - 1; i >= 0; i--){
                GameObject object = killedObjects.get(i);
                objects.remove(object);
                if(object instanceof Collider){
                    collisionManager.removeCollider((Collider) object);
                }
                killedObjects.remove(object);
            }
        }
    }
    public ArrayList<GameObject> getObjects(){
        return objects;
    }

    public void clearObjects(){
        objects.clear();
    }
}
