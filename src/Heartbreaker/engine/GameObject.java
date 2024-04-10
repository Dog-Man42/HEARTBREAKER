package Heartbreaker.engine;

import Heartbreaker.engine.scenes.Scene;

import java.awt.*;
import java.awt.geom.Point2D;


/**
 * GameObject is the base abstract class for all objects to be updated and rendered by engine each frame.
 *
 * @author tuckt
 */

public abstract class GameObject {

    private boolean drawnByScene = true;

    /**
     * Stores the parent object if there is one
     */
    private GameObject parent;

    /**
     * x-position of the object
     */
    private double xPosition;

    /**
     * y-position of the object
     */
    private double yPosition;

    /**
     * Rotation of the object in degrees
     */
    private double rotation;

    /**
     * Scale of the object, defaulted at 10
     */
    private double scale = 10;

    /**
     * Scene the object belongs to
     */
    private Scene currentScene = GameFrame.getCurrentScene();

    /**
     * Represents the shape of the object as a polygon. Should not be changed otherwise inaccuracies could build.
     */
    private Point2D.Double[] vertices;



    /**
     * Returns an array of given points rotated by theta.
     *
     * @param theta       radians to rotate by.
     * @param inputPoints points to be rotated.
     * @return Array of rotated points
     */
    public Point2D.Double[] rotatePoints(double theta, Point2D.Double[] inputPoints) {
        Point2D.Double[] output = new Point2D.Double[inputPoints.length];
        for (int i = 0; i < inputPoints.length; i++) {
            double sin = Math.sin(theta);
            double cos = Math.cos(theta);
            double x = inputPoints[i].x * cos - inputPoints[i].y * sin;
            double y = inputPoints[i].x * sin + inputPoints[i].y * cos;
            output[i] = new Point2D.Double(x, y);
        }
        return output;
    }

    /**
     * Returns a given point rotated by theta.
     *
     * @param theta      radians to rotate by.
     * @param inputPoint point to be rotated.
     * @return rotated point
     */
    public Point2D.Double rotatePoint(double theta, Point2D.Double inputPoint) {
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        double x = inputPoint.x * cos - inputPoint.y * sin;
        double y = inputPoint.x * sin + inputPoint.y * cos;
        return new Point2D.Double(x, y);
    }

    /**
     * Rotates a given point around another point by theta.
     *
     * @param theta      radians to rotate by.
     * @param inputPoint point to be rotated.
     * @param parent     the point to be rotated around.
     * @return rotated point
     */
    public Point2D.Double rotatePointAroundPoint(double theta, Point2D.Double inputPoint, Point2D.Double parent) {
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        double x = (inputPoint.x - parent.x) * cos - (inputPoint.y - parent.y) * sin + parent.x;
        double y = (inputPoint.x - parent.x) * sin + (inputPoint.y - parent.y) * cos + parent.y;
        return new Point2D.Double(x, y);
    }


    /**
     * Calculates the distance between x1, y1 and x2, y2.
     *
     * @param x1 x position of point 1
     * @param y1 y position of point 1
     * @param x2 x position of point 2
     * @param y2 y position of point 2
     * @return distance between two points
     */
    public double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2.0) +
                Math.pow(y2 - y1, 2.0));
    }

    /**
     * Calculates the distance between two points.
     *
     * @param p1 x position of point 1
     * @param p2 y position of point 1
     * @return distance between two points
     */
    public double calculateDistance(Point2D.Double p1, Point2D.Double p2) {
        return Math.sqrt(Math.pow((p2.x - p1.x), 2.0) +
                Math.pow(p2.y - p1.y, 2.0));
    }

    /**
     * Generates a Polygon from an a transformed array of provided points
     *
     * @param points Point2D.Double array
     * @return polygon consisting of given points
     */
    public Polygon realizePoly(Point2D.Double[] points) {

        Polygon temp = new Polygon();
        for (Point2D.Double point : points) {
            if (parent != null) {
                //4.5 hours spent here
                Point2D.Double tempPoint = new Point2D.Double((point.x * (scale * parent.scale)), (point.y * (scale * parent.scale)));
                tempPoint = rotatePoint(Math.toRadians(rotation), tempPoint);
                tempPoint.setLocation(tempPoint.x + xPosition, tempPoint.y + yPosition);
                tempPoint = rotatePoint(Math.toRadians(parent.rotation), tempPoint);
                temp.addPoint((int) (tempPoint.x + parent.getXPosition()), (int) (tempPoint.y + parent.getYPosition()));
            } else {
                Point2D.Double tempPoint = new Point2D.Double(point.x * scale, point.y * scale);
                tempPoint = rotatePoint(Math.toRadians(rotation), tempPoint);
                temp.addPoint((int) (tempPoint.x + xPosition), (int) (tempPoint.y + yPosition));
            }
        }
        return temp;
    }

    /**
     * Generates a Polygon of transformed vertices
     *
     * @return
     */
    public Polygon realizePoly() {

        Polygon temp = new Polygon();
        for(Point2D.Double point : realizePoints()){
            temp.addPoint( (int) Math.round( point.x ), (int) Math.round( point.y ) );
        }
        return temp;
    }

    /**
     * Gets the polygon made from transformed points in camera space
     * @param camera Scene Camera
     * @return transformed polygon accounting for camera offset
     */
    public Polygon realizePolyCameraSpace(Camera camera) {

        Polygon temp = new Polygon();
        for(Point2D.Double point : realizePointsCameraSpace(camera)){
            temp.addPoint( (int) Math.round( point.x ), (int) Math.round( point.y ) );
        }
        return temp;
    }


    /**
     * Generates an array of transformed vertices
     *
     * @return polygon consisting of points within transformedVertices
     */
    public Point2D.Double[] realizePoints() {
        if (vertices.length == 0) {
            return new Point2D.Double[0];
        }
        Point2D.Double[] temp = new Point2D.Double[vertices.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = new Point2D.Double();
            if (parent != null) {
                //4.5 hours spent here
                Point2D.Double tempPoint = new Point2D.Double((vertices[i].x * (scale * parent.scale)), (vertices[i].y * (scale * parent.scale)));
                tempPoint = rotatePoint(Math.toRadians(rotation), tempPoint);
                tempPoint.setLocation(tempPoint.x + xPosition, tempPoint.y + yPosition);
                tempPoint = rotatePoint(Math.toRadians(parent.rotation), tempPoint);
                temp[i].setLocation((int) (tempPoint.x + parent.getXPosition()), (int) (tempPoint.y + parent.getYPosition()));
            } else {
                Point2D.Double tempPoint = new Point2D.Double(vertices[i].x * scale, vertices[i].y * scale);
                tempPoint = rotatePoint(Math.toRadians(rotation), tempPoint);
                temp[i].setLocation((int) (tempPoint.x + xPosition), (int) (tempPoint.y + yPosition));
            }
        }
        return temp;
    }

    /**
     * Gets the transformed points in camera space
     * @param camera Scene Camera
     * @return transformed points accounting for camera offset
     */
    public Point2D.Double[] realizePointsCameraSpace(Camera camera){
        Point2D.Double[] points = realizePoints();
        if(camera == null){
            return points;
        }
        Point origin = getScene().origin;

        for( Point2D.Double point : points){
            double x = origin.x + (point.getX() - camera.getxPosition()) * camera.zoom;
            double y = origin.y + (point.getY() - camera.getyPosition()) * camera.zoom;
            point.setLocation(x,y);
        }

        return points;
    }



    /**
     * Returns the x-position of the object. If parent is not null, transforms are added
     *
     * @return x-position in world space
     */
    public double getXPosition() {
        if (parent != null) {
            return xPosition + parent.getXPosition();
        }
        return xPosition;
    }

    /**
     * Sets the X-position of the object
     *
     * @param x new x-position
     */
    public void setXPosition(double x) {
        xPosition = x;
    }

    /**
     * Returns the x-position without considering a parent's transforms
     *
     * @return x-position of object
     */
    public double getXPositionWorld() {
        return xPosition;
    }

    /**
     * Returns the y-position of the object. If parent is not null, transforms are added
     *
     * @return y-position in world space
     */
    public double getYPosition() {
        if (parent != null) {
            return yPosition + parent.getYPosition();
        }
        return yPosition;

    }

    /**
     * Sets the Y-position of the object
     *
     * @param y new y-position
     */
    public void setYPosition(double y) {
        yPosition = y;
    }

    /**
     * Returns the y-position without considering a parent's transforms
     *
     * @return y-position of object
     */
    public double getYPositionWorld() {
        return yPosition;
    }

    /**
     * Returns the position as a Point2D.Double
     *
     * @return Point2D.Double with position of the object
     */
    public Point2D.Double getPosition() {
        if (parent != null) {
            return new Point2D.Double(xPosition + parent.getXPosition(), yPosition + parent.getYPosition());
        }
        return new Point2D.Double(xPosition, yPosition);

    }

    /**
     * Returns the position as a Point2D.Double
     *
     * @return Point2D.Double with position of the object
     * @throws NullPointerException worldSpace is true and parent null
     */
    public Point2D.Double getLocalPos() {
        return new Point2D.Double(xPosition, yPosition);
    }

    /**
     * Gets the object position in camera space.
     * @return position - camera offset
     */
    public Point2D.Double getPositionCameraSpace(){
        Camera cam = currentScene.camera;
       /*
        if(parent != null) {
            return new Point2D.Double((xPosition + parent.getXPosition()) - currentScene.getCamera().xPosition, (yPosition + parent.getYPosition()) - currentScene.getCamera().yPosition);
        } else {
            return new Point2D.Double(xPosition - currentScene.getCamera().xPosition, yPosition - currentScene.getCamera().yPosition);

        }

        */
        Point origin = getScene().origin;
        return new Point2D.Double(origin.x +(getXPosition() - cam.getxPosition()) * cam.getZoom(), origin.y +(getYPosition() - cam.getyPosition()) * cam.getZoom());
    }

    /**
     * Returns object's rotation in degrees
     *
     * @return
     */
    public double getRotation() {
        return this.rotation;
    }

    /**
     * Sets Rotation
     *
     * @param theta Rotation in degrees
     */
    public void setRotation(double theta) {
        this.rotation = theta;
    }

    /**
     * Returns the scale of the object
     *
     * @return
     */
    public double getScale() {
        return scale;
    }

    /**
     * Sets Scale. Effects scale and position of children
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * Changes xposition by n amount
     *
     * @param change
     */
    public void changeXPos(double change) {
        xPosition += change;
    }

    /**
     * Changes yposition by n amount
     *
     * @param change
     */
    public void changeYPos(double change) {
        yPosition += change;
    }

    /**
     * Changes scale by n amount
     *
     * @param change
     */
    public void changeScale(double change) {
        scale += change;
    }

    /**
     * Changes rotation by n amount
     *
     * @param change
     */
    public void changeRotation(double change) {
        rotation += change;
    }


    /**
     * Sets the parent of this GameObject
     *
     * @param p              new parent
     * @param keepTransforms adds parent's transforms to this object before changing parent
     * @throws NullPointerException keepTransforms is true and parent null
     */
    public void setParent(GameObject p, boolean keepTransforms) {
        if (keepTransforms) {
            if (parent == null) {
                throw new NullPointerException();
            }
            xPosition += parent.getXPosition();
            yPosition += parent.getYPosition();
            scale = parent.scale;
            rotation = parent.rotation;
        }
        this.parent = p;
    }

    /**
     * Un-parents Object from parent.
     *
     * @param keepTransforms if true, all transforms from the parent are kept,
     *                       if false, parent's transforms are ignored.
     */
    public void removeParent(boolean keepTransforms) {
        if (keepTransforms) {
            if (parent == null) {
                throw new NullPointerException();
            }
            xPosition += parent.getXPosition();
            yPosition += parent.getYPosition();
            scale = parent.scale;
            rotation = parent.rotation;
        }
        this.parent = null;
    }

    /**
     * Returns the parent Object
     *
     * @return GameObject
     */
    public GameObject getParent() {
        return parent;
    }

    /**
     * Called every frame. Used to update Game Objects.
     */
    public abstract void update(double delta);

    /**
     * Called every frame. Draws to the screen.
     *
     * @param g     Graphics2D object
     * @param delta
     */
    public abstract void draw(Graphics2D g, double delta);

    /**
     * Returns whether the scene draws this GameObject when drawing the objects ArrayList
     *
     * @return if scene draws object
     */
    public boolean isDrawnByScene() {
        return drawnByScene;
    }

    /**
     * Sets if the scene can draw the object
     *
     * @param drawnByScene
     */
    public void setDrawnByScene(boolean drawnByScene) {
        this.drawnByScene = drawnByScene;
    }

    /**
     * Returns the base vertices array
     *
     * @return
     */
    public Point2D.Double[] getVerticies() {
        return this.vertices;
    }

    /**
     * Replaces the base vertices and transformed vertices array with a new Point2D.Double array
     *
     * @param verts new vertices array
     */
    public void setVertices(Point2D.Double[] verts) {
        this.vertices = verts;

    }


    public Scene getScene() {
        return currentScene;
    }

}
