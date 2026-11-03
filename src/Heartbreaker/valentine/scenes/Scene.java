package Heartbreaker.valentine.scenes;

import Heartbreaker.valentine.*;
import Heartbreaker.valentine.collision.Collider;
import Heartbreaker.valentine.collision.CollisionManager;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public abstract class Scene {

    public Point origin;
    public CollisionManager collisionManager;

    private ArrayList<GameObject> objects = new ArrayList<>();
    private ArrayList<GameObject> killedObjects = new ArrayList<>();

    private boolean initialized = false;

    public int WIDTH = GameFrame.GAME_WIDTH;
    public int HEIGHT = GameFrame.GAME_HEIGHT;
    public int DIAGONAL = (int)Math.round(Math.sqrt(WIDTH * WIDTH + HEIGHT * HEIGHT));

    public Camera camera;

    public void windowResized(){
        origin = new Point(GameFrame.GAME_WIDTH/2, GameFrame.GAME_HEIGHT/2);
        WIDTH = GameFrame.GAME_WIDTH;
        HEIGHT = GameFrame.GAME_HEIGHT;
    }
    public abstract boolean initialize();


    public abstract void updateScene(double delta);

    public void updateObjects(double delta){
        //System.out.println("Object Count: " + objects.size());
        //System.out.println("Collider Count: " + collisionManager.size());
        if(objects.size() > 0){
            int length = objects.size();
            for(int i = 0; i < length; i++){
                GameObject object = objects.get(i);
                if(!killedObjects.contains(object)){
                    object.update(delta);
                }
            }
        }
        if(camera != null){
            camera.update(delta);
        }
        collisionManager.updateColliders();
        clearRemovedObjects();
    }
    public abstract void draw(Graphics2D g, double delta);

    public void drawObjects(Graphics2D g, double delta){
        if(objects.size() > 0){
            for(int i = objects.size() - 1; i >= 0; i--){
                GameObject object = objects.get(i);
                if(object.isDrawnByScene()) {
                    object.draw(g, delta);
                }
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(3));
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

    public Camera getCamera(){
        return camera;
    }

    /**
     * Converts a world position to a screen position using this scene's origin and camera.
     * @param x world x
     * @param y world y
     * @return position on screen in pixels
     */
    public Point2D.Double worldToScreen(double x, double y){
        if(camera == null){
            return new Point2D.Double(x, y);
        }
        return new Point2D.Double(
                origin.x + (x - camera.getxPosition()) * camera.getZoom(),
                origin.y + (y - camera.getyPosition()) * camera.getZoom());
    }

    /**
     * Converts a screen position to a world position. Inverse of {@link #worldToScreen(double, double)}.
     * @param x screen x in pixels
     * @param y screen y in pixels
     * @return position in world space
     */
    public Point2D.Double screenToWorld(double x, double y){
        if(camera == null){
            return new Point2D.Double(x, y);
        }
        return new Point2D.Double(
                camera.getxPosition() + (x - origin.x) / camera.getZoom(),
                camera.getyPosition() + (y - origin.y) / camera.getZoom());
    }

    /**
     * Converts a screen position to a world position, such as the mouse position.
     * @param p screen position in pixels
     * @return position in world space
     */
    public Point2D.Double screenToWorld(Point p){
        return screenToWorld(p.x, p.y);
    }

}
