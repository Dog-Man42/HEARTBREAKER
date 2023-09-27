package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.scenes.*;

import java.awt.*;
import java.awt.geom.Point2D;



public class BaseObject {

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
            double x = inputPoints[i].x * Math.cos(theta) - inputPoints[i].y * Math.sin(theta);
            double y = inputPoints[i].x * Math.sin(theta) + inputPoints[i].y * Math.cos(theta);
            output[i] = new Point2D.Double(x,y);
        }
        return output;
    }
    public Point2D.Double rotatePoint(double theta, Point2D.Double inputPoint){
        double x = inputPoint.x * Math.cos(theta) - inputPoint.y * Math.sin(theta);
        double y = inputPoint.x * Math.sin(theta) + inputPoint.y * Math.cos(theta);
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
            temp.addPoint((int) ((point.x * scale) + xPosition), (int) ((point.y * scale) + yPosition));
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
            temp[i].setLocation( ((transformedVertices[i].x * scale) + xPosition), ((transformedVertices[i].y * scale) + yPosition));
        }
        return temp;
    }

    public Polygon realizePoly(){
        Polygon temp = new Polygon();
        for (Point2D.Double point : transformedVertices) {
            temp.addPoint((int) ((point.x * scale) + xPosition), (int) ((point.y * scale) + yPosition));
        }
        return temp;
    }

    public double getxPosition() {
        return xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }
}
