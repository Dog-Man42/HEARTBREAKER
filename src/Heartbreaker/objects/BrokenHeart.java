package Heartbreaker.objects;

import java.awt.geom.Point2D;
import java.awt.*;

public class BrokenHeart extends GameObject {

    int side;

    BrokenHeart(int hs, double x, double y,double scale){
        this.side = hs;
        this.xPosition = x;
        this.yPosition = y;
        this.scale = scale;
        vertices = new Point2D.Double[]{
                new Point2D.Double(0 * hs,-13),
                new Point2D.Double(14 * hs,0),
                new Point2D.Double(16 * hs,8),
                new Point2D.Double(14 * hs,13),
                new Point2D.Double(10 * hs,15),
                new Point2D.Double(5 * hs,12),
                new Point2D.Double(0 * hs,8)};
        transformedVertices = new Point2D.Double[vertices.length];
        transformedVertices = copyVertices(vertices);


    }
    public void update(){
        xPosition -= 5 * side;
        scale +=.1;
    }
    public void draw(Graphics2D g){
        g.draw(realizePoly(transformedVertices));
        System.out.println("Parent = " + getParent());


    }
}
