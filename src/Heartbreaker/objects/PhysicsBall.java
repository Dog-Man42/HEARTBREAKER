package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.GameObject;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;

import java.awt.*;
import java.awt.geom.Point2D;

public class PhysicsBall extends GameObject implements Collider {

    private int radius;
    private double xVel;
    private double yVel;

    private double mass;


    public PhysicsBall(int x, int y, int radius, int mass, double xvel, double yvel){
        setXPosition(x);
        setYPosition(y);
        this.mass = mass;
        this.radius = radius;
        this.xVel = xvel;
        this.yVel = yvel;
    }

    @Override
    public void update(double delta) {
        changeXPos(xVel * delta);
        changeYPos(yVel * delta);
        if(calculateDistance(getPosition(),new Point2D.Double(0,0)) > 500){
            xVel *= -1;
            yVel *= -1;
        }

    }

    @Override
    public void draw(Graphics2D g, double delta) {
        Point2D.Double p = getPositionCameraSpace();
        int drawRadius = (int) (getScene().getCamera().getZoom() * radius);
        g.setColor(Color.ORANGE);
        g.drawOval((int) (p.x - drawRadius/2), (int) (p.y - drawRadius/2), drawRadius*2,drawRadius*2);

    }

    @Override
    public void collided(CollisionData colData) {}

    @Override
    public int getCanHit() {
        return 0;
    }

    @Override
    public int getHitBy() {
        return 0;
    }

    @Override
    public int getHitBoxType() {
        return 0;
    }

    @Override
    public boolean getStatic() {
        return false;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public double getXVelocity() {
        return 0;
    }

    @Override
    public double getYVelocity() {
        return 0;
    }

    @Override
    public double getRadius() {
        return 0;
    }

    @Override
    public Point2D.Double[] getPoints() {
        return new Point2D.Double[0];
    }
}
