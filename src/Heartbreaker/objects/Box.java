package Heartbreaker.objects;

import Heartbreaker.engine.GameObject;

import java.awt.*;
import java.awt.geom.*;


public class Box extends GameObject {
     private double frames = 0;


    public Box(double x, double y, double scale){
        setXPosition(x);
        setYPosition(y);
        setScale(scale);


        setVertices(new Point2D.Double[]{
                new Point2D.Double(2,2),
                new Point2D.Double(2,-2),
                new Point2D.Double(-2,-2),
                new Point2D.Double(-2,2)});





    }
    public void update(){
        frames += .15;

        setScale(-2*-(Math.abs(Math.sin(frames)) * Math.abs(Math.cos(frames) - 1)) + 8);
        changeRotation(1);

    }
    public void draw(Graphics2D g){

        g.setColor(Color.green);
        g.fillPolygon(realizePolyCameraSpace(getScene().getCamera()));
    }







}


