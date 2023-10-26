package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.scenes.*;

import java.awt.*;
import java.awt.geom.Point2D;


/**
 * @author Tucker Agee
 */

public abstract class BaseObject {

    /**
     * Stores the parent object if there is one
     */
    private BaseObject parent;

    /**
     * x-position of the object
     */
    protected double xPosition;

    /**
     * y-position of the object
     */
    protected double yPosition;

    /**
     * Rotation of the object in degrees
     */
    protected double rotation;

    /**
     * Scale of the object, defaulted at 10
     */
    protected double scale = 10;

    /**
     * Scene the object belongs to
     */
    Level currentScene = GameFrame.getCurrentScene();

    /**
     * Represents the shape of the object as a polygon. Should not be changed otherwise inaccuracies could build.
     */
    protected Point2D.Double[] vertices;

    /**
     * Transformed shape of the object. This is what gets drawn to the screen. Repeated transforms or modifications should not be
     * done on this array, otherwise inaccuracies could build.
     */
    public Point2D.Double[] transformedVertices;

    /**
     * Returns an array of given points rotated by theta.
     *
     * @param theta radians to rotate by.
     * @param inputPoints points to be rotated.
     * @return Array of rotated points
     */
    public Point2D.Double[] rotatePoints(double theta, Point2D.Double[] inputPoints){
        Point2D.Double[] output = new Point2D.Double[inputPoints.length];
        for(int i = 0; i < inputPoints.length; i++){
            double sin = Math.sin(theta);
            double cos = Math.cos(theta);
            double x = inputPoints[i].x * cos- inputPoints[i].y * sin;
            double y = inputPoints[i].x * sin + inputPoints[i].y * cos;
            output[i] = new Point2D.Double(x,y);
        }
        return output;
    }

    /**
     * Returns a given point rotated by theta.
     *
     * @param theta radians to rotate by.
     * @param inputPoint point to be rotated.
     * @return rotated point
     */
    public Point2D.Double rotatePoint(double theta, Point2D.Double inputPoint){
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        double x = inputPoint.x * cos - inputPoint.y * sin;
        double y = inputPoint.x * sin + inputPoint.y * cos;
        return new Point2D.Double(x,y);
    }

    /**
     * Rotates a given point around another point by theta.
     *
     * @param theta radians to rotate by.
     * @param inputPoint point to be rotated.
     * @param parent the point to be rotated around.
     * @return rotated point
     */
    public Point2D.Double rotatePointAroundPoint(double theta, Point2D.Double inputPoint, Point2D.Double parent){
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        double x = (inputPoint.x - parent.x) * cos - (inputPoint.y - parent.y) * sin + parent.x;
        double y = (inputPoint.x - parent.x) * sin + (inputPoint.y - parent.y) * cos + parent.y;
        return new Point2D.Double(x,y);
    }

    /**
     * Returns a copy of a Point2D.Double array.
     *
     * @param baseVerts Point2D.Double array to be copied
     * @return array of copied vertices
     */
    public Point2D.Double[] copyVertices(Point2D.Double[] baseVerts){
        Point2D.Double[] temp = new Point2D.Double[baseVerts.length];
        System.arraycopy(baseVerts, 0, temp, 0, baseVerts.length);
        return temp;
    }

    /**
     * Calculates the distance between x1,y1 and x2,y2.
     *
     * @param x1 x position of point 1
     * @param y1 y position of point 1
     * @param x2 x position of point 2
     * @param y2 y position of point 2
     * @return distance between two points
     */
    public double calculateDistance(double x1,double y1, double x2, double y2){
        return Math.sqrt( Math.pow((x2 - x1),2.0) +
                Math.pow(y2 - y1,2.0) );
    }

    /**
     * Calculates the distance between two points.
     *
     * @param p1 x position of point 1
     * @param p2 y position of point 1
     * @return distance between two points
     */
    public double calculateDistance(Point2D.Double p1, Point2D.Double p2){
        return Math.sqrt( Math.pow((p2.x - p1.x),2.0) +
                Math.pow(p2.y - p1.y,2.0) );
    }

    /**
     * Generates a Polygon from a given Point2D.Double array
     *
     * @param points Point2D.Double array
     * @return polygon consisting of given points
     */
    public Polygon realizePoly(Point2D.Double[] points){

        Polygon temp = new Polygon();
        for (Point2D.Double point : points) {
            if(parent != null){
                //4.5 hours spent here
                Point2D.Double tempPoint = new Point2D.Double((point.x * (scale * parent.scale)),(point.y * (scale * parent.scale)));
                tempPoint = rotatePoint(Math.toRadians(rotation),tempPoint);
                tempPoint.setLocation(tempPoint.x + xPosition,tempPoint.y + yPosition);
                tempPoint = rotatePoint(Math.toRadians(parent.rotation),tempPoint);
                temp.addPoint((int) (tempPoint.x + parent.getXPosition()),(int) (tempPoint.y + parent.getYPosition()));
            } else {
                Point2D.Double tempPoint = new Point2D.Double(point.x * scale,point.y * scale);
                tempPoint = rotatePoint(Math.toRadians(rotation),tempPoint);
                temp.addPoint((int) (tempPoint.x + xPosition),(int) (tempPoint.y + yPosition));
            }
        }
        return temp;
    }

    /**
     * Generates a Polygon consisting of the points contained in transformedVertices
     *
     * @return polygon consisting of points within transformedVertices
     */
    public Point2D.Double[] realizePoints(){
        if (transformedVertices.length == 0) {
            return new Point2D.Double[0];
        }
        Point2D.Double[] temp = new Point2D.Double[transformedVertices.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = new Point2D.Double();
            if(parent != null){
                Point2D.Double tempPoint = new Point2D.Double((transformedVertices[i].x * (scale * parent.scale)),(transformedVertices[i].y * (scale * parent.scale)));
                tempPoint = rotatePoint(Math.toRadians(rotation),tempPoint);
                tempPoint.setLocation(tempPoint.x + xPosition,tempPoint.y + yPosition);
                tempPoint = rotatePoint(Math.toRadians(parent.rotation),tempPoint);
                temp[i].setLocation((int) (tempPoint.x + parent.getXPosition()),(int) (tempPoint.y + parent.getYPosition()));
            } else {
                Point2D.Double tempPoint = new Point2D.Double(transformedVertices[i].x * scale,transformedVertices[i].y * scale);
                tempPoint = rotatePoint(Math.toRadians(rotation),tempPoint);
                temp[i].setLocation((int) (tempPoint.x + xPosition),(int) (tempPoint.y + yPosition));
            }
        }
        return temp;
    }

    /**
     * Returns the x-position of the object. If parent is not null, transforms are added
     *
     * @return x-position in world space
     */
    public double getXPosition() {
        if(parent != null) {
            return xPosition + parent.getXPosition();
        }
        return xPosition;
    }

    /**
     * Returns the x-position
     *
     * @param worldSpace if true, add the parent's positon, if false, only return object's position
     * @return x-position of object
     * @throws NullPointerException worldSpace is true and parent null
     */
    public double getXPosition(boolean worldSpace) {
        if(worldSpace){
            if(parent == null){
                throw new NullPointerException();
            }
            return xPosition + parent.getXPosition();
        } else {
            return xPosition;
        }
    }

    /**
     * Returns the y-position of the object. If parent is not null, transforms are added
     *
     * @return y-position in world space
     */
    public double getYPosition() {
        if(parent != null){
            return yPosition + parent.getYPosition();
        }
        return yPosition;

    }

    /**
     * Returns the y-position
     *
     * @param worldSpace if true, add the parent's positon, if false, only return object's position
     * @return y-position of object
     * @throws NullPointerException worldSpace is true and parent null
     */
    public double getYPosition(boolean worldSpace) {
        if(worldSpace){
            if(parent == null){
                throw new NullPointerException();
            }
            return yPosition + parent.getYPosition();
        } else {
            return yPosition;
        }
    }

    /**
     * Returns the position as a Point2D.Double
     *
     * @return Point2D.Double with position of the object
     */
    public Point2D.Double getPosition(){
        if(parent != null) {
            return new Point2D.Double(xPosition + parent.getXPosition(), yPosition + parent.getYPosition());
        }
        return new Point2D.Double(xPosition, yPosition);

    }

    /**
     * Returns the position as a Point2D.Double
     *
     * @param worldSpace if true, add the parent's positon, if false, only return object's position
     * @return Point2D.Double with position of the object
     * @throws NullPointerException worldSpace is true and parent null
     */
    public Point2D.Double getPosition(boolean worldSpace){
        if(worldSpace){
            if(parent == null){
                throw new NullPointerException();
            }
            return new Point2D.Double(xPosition + parent.getXPosition(),yPosition + parent.getYPosition());
        }
        return new Point2D.Double(xPosition,yPosition);
    }

    /**
     * Sets the X-position of the object
     *
     * @param x new x-position
     */
    public void setXPosition(double x){
        xPosition = x;
    }

    /**
     * Sets the Y-position of the object
     *
     * @param y new y-position
     */
    public void setYPosition(double y){
        yPosition = y;
    }
    public void setRotation(double theta){this.rotation = theta;}

    /**
     * Sets the parent of this BaseObject
     *
     * @param p new parent
     * @param keepTransforms adds parent's transforms to this object before changing parent
     * @throws NullPointerException keepTransforms is true and parent null
     */
    public void setParent(BaseObject p, boolean keepTransforms){
        if(keepTransforms){
            if(parent == null){
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
    public void removeParent(boolean keepTransforms){
        if(keepTransforms){
            if(parent == null){
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
     * @return BaseObject
     */
    public BaseObject getParent(){
        return parent;
    }

    /**
     * Called every frame. Used to update Game Objects.
     */
    public abstract void update();

    /**
     * Called every frame. Draws to the screen.
     *
     * @param g Graphics2D object
     */
     public abstract void draw(Graphics2D g);
}
