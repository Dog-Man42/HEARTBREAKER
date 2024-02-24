package Heartbreaker.objects;

import Heartbreaker.engine.GameObject;

import java.awt.*;
import java.awt.geom.Point2D;

public class TentacleSegment extends GameObject {

    private int radius;

    private double baseX;
    private double baseY;



    public TentacleSegment(int radius, double scale, double baseX, double baseY){

        setXPosition(baseX);
        setYPosition(baseY);
        setScale(scale);
        this.radius = radius;
        this.baseX = baseX;
        this.baseY = baseY;

    }

    public void setPosition(Point2D.Double p){
        setXPosition(p.x);
        setYPosition(p.y);
    }




    @Override
    public void update(double delta) {}

    public void draw(Graphics2D g, double delta){
        g.setColor(Color.ORANGE);
        g.fillOval((int) (getXPosition() - radius), (int) (getYPosition() - radius), radius*2,radius*2);



    }
}
