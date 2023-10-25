package Heartbreaker.objects;

import java.awt.*;
import java.awt.geom.Point2D;

public class TentacleSegment extends BaseObject{

    private int radius;

    private double baseX;
    private double baseY;



    public TentacleSegment(int radius, double scale, double baseX, double baseY){
        this.scale = scale;
        this.radius = radius;
        this.baseX = baseX;
        this.baseY = baseY;
        xPosition = baseX;
        yPosition = baseY;
    }

    public void setPosition(Point2D.Double p){
        xPosition = p.x;
        yPosition = p.y;
    }




    @Override
    public void update() {}

    public void draw(Graphics2D g){
        g.setColor(Color.ORANGE);
        g.fillOval((int) (getXPosition() - radius), (int) (getYPosition() - radius), radius*2,radius*2);



    }
}
