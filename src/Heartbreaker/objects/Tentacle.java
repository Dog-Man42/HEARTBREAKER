package Heartbreaker.objects;

import java.awt.*;
import java.awt.geom.Point2D;


public class Tentacle extends BaseObject {
     private double frames = 0;


    public Tentacle(double x, double y, double scale){
        this.xPosition = x;
        this.yPosition = y;
        this.scale = scale;


        vertices = new Point2D.Double[]{
                new Point2D.Double(2,2),
                new Point2D.Double(2,-2),
                new Point2D.Double(-2,-2),
                new Point2D.Double(-2,2)};

        transformedVertices = new Point2D.Double[]{
                new Point2D.Double(2,2),
                new Point2D.Double(2,-2),
                new Point2D.Double(-2,-2),
                new Point2D.Double(-2,2)};




    }
    public void update(){
        frames += .15;

        scale = 2*-(Math.abs(Math.sin(frames)) * Math.abs(Math.cos(frames) - 1)) + 8;
        scale *= -1;
    }
    public void draw(Graphics2D g){

        g.setColor(Color.green);
        g.fillPolygon(realizePoly(vertices));
    }







}


