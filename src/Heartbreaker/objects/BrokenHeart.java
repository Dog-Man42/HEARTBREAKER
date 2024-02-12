package Heartbreaker.objects;

import Heartbreaker.engine.GameObject;

import java.awt.geom.Point2D;
import java.awt.*;

public class BrokenHeart extends GameObject {

    int side;

    BrokenHeart(int hs, double x, double y,double scale){
        this.side = hs;
        setXPosition(x);
        setYPosition(y);
        setScale(scale);

        setVertices(new Point2D.Double[]{
                new Point2D.Double(0 * hs,-13),
                new Point2D.Double(14 * hs,0),
                new Point2D.Double(16 * hs,8),
                new Point2D.Double(14 * hs,13),
                new Point2D.Double(10 * hs,15),
                new Point2D.Double(5 * hs,12),
                new Point2D.Double(0 * hs,8)});

    }
    public void update(){
        changeXPos(-5 * side);
        changeScale(.1);
    }
    public void draw(Graphics2D g){
        g.draw(realizePoly());
        System.out.println("Parent = " + getParent());


    }
}
