package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.scenes.*;

import java.awt.*;
import java.awt.geom.Point2D;



public abstract class BaseObject {

    private BaseObject parent;

    protected double xPosition;
    protected double yPosition;
    protected double rotation;
    protected double scale = 10;

    Level currentScene = GameFrame.getCurrentScene();

    protected Point2D.Double[] vertices;
    public Point2D.Double[] transformedVertices;


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
    public Point2D.Double rotatePoint(double theta, Point2D.Double inputPoint){
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        double x = inputPoint.x * cos - inputPoint.y * sin;
        double y = inputPoint.x * sin + inputPoint.y * cos;
        return new Point2D.Double(x,y);
    }
    public Point2D.Double rotatePointAroundPoint(double theta, Point2D.Double inputPoint, Point2D.Double parent){
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        double x = (inputPoint.x - parent.x) * cos - (inputPoint.y - parent.y) * sin + parent.x;
        double y = (inputPoint.x - parent.x) * sin + (inputPoint.y - parent.y) * cos + parent.y;
        return new Point2D.Double(x,y);
    }

    public Point2D.Double[] copyVertices(Point2D.Double[] baseVerts){
        Point2D.Double[] temp = new Point2D.Double[baseVerts.length];
        System.arraycopy(baseVerts, 0, temp, 0, baseVerts.length);
        return temp;
    }

    public double calculateDistance(double x1,double y1, double x2, double y2){
        return Math.sqrt( Math.pow((x2 - x1),2.0) +
                Math.pow(y2 - y1,2.0) );
    }

    public Polygon realizePoly(Point2D.Double[] points){

        Polygon temp = new Polygon();
        for (Point2D.Double point : points) {
            if(parent != null){
                //4.5 hours spent here
                Point2D.Double tempPoint = new Point2D.Double((point.x * (scale * parent.scale)),(point.y * (scale * parent.scale)));
                tempPoint = rotatePoint(Math.toRadians(rotation),tempPoint);
                tempPoint.setLocation(tempPoint.x + xPosition,tempPoint.y + yPosition);
                tempPoint = rotatePoint(Math.toRadians(parent.rotation),tempPoint);
                temp.addPoint((int) (tempPoint.x + parent.xPosition),(int) (tempPoint.y + parent.yPosition));
            } else {
                Point2D.Double tempPoint = new Point2D.Double(point.x * scale,point.y * scale);
                tempPoint = rotatePoint(Math.toRadians(rotation),tempPoint);
                temp.addPoint((int) (tempPoint.x + xPosition),(int) (tempPoint.y + yPosition));
            }
        }
        return temp;
    }
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
                temp[i].setLocation((int) (tempPoint.x + parent.xPosition),(int) (tempPoint.y + parent.yPosition));
            } else {
                Point2D.Double tempPoint = new Point2D.Double(transformedVertices[i].x * scale,transformedVertices[i].y * scale);
                tempPoint = rotatePoint(Math.toRadians(rotation),tempPoint);
                temp[i].setLocation((int) (tempPoint.x + xPosition),(int) (tempPoint.y + yPosition));
            }
        }
        return temp;
    }

    public double getxPosition() {
        return xPosition;
    }
    public double getyPosition() {
        return yPosition;

    }

    public double getxPosition(boolean worldSpace) {
        if(worldSpace){
            return xPosition + parent.xPosition;
        } else {
            return xPosition;
        }

    }
    public double getyPosition(boolean worldSpace) {
        if(worldSpace){
            return yPosition + parent.yPosition;
        } else {
            return yPosition;
        }
    }
    public Point2D.Double getPosition(){
        return new Point2D.Double(xPosition,yPosition);
    }

    public void setXPosition(double x){
        xPosition = x;
    }
    public void setYPosition(double y){
        yPosition = y;
    }
    public void setRotation(double theta){this.rotation = theta;}

    public void setParent(BaseObject p, boolean keepTransforms){
        if(keepTransforms){
            if(parent == null){
                throw new NullPointerException();
            }
            xPosition += parent.xPosition;
            yPosition += parent.yPosition;
            scale = parent.scale;
            rotation = parent.rotation;
        }
        this.parent = p;
    }
    public void removeParent(boolean keepTransforms){
        if(keepTransforms){
            if(parent == null){
                throw new NullPointerException();
            }
            xPosition += parent.xPosition;
            yPosition += parent.yPosition;
            scale = parent.scale;
            rotation = parent.rotation;
        }
        this.parent = null;
    }
    public BaseObject getParent(){
        return parent;
    }

    public abstract void update();

     public abstract void draw(Graphics2D g);
}
