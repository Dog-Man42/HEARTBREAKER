package Heartbreaker.objects;

import Heartbreaker.engine.GameObject;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;
import Heartbreaker.engine.vectors.*;

import java.awt.*;
import java.awt.geom.Point2D;

public class PhysicsBall extends GameObject implements Collider {

    private int radius;
    private double xVel;
    private double yVel;

    private int mass = 1;

    private boolean vNextFrame = false;
    private Vector2 tempV = new Vector2(0,0);


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
        if(vNextFrame){
            xVel = tempV.x;
            yVel = tempV.y;
            vNextFrame = false;
        }

        changeXPos(xVel * delta);
        changeYPos(yVel * delta);
        if(calculateDistance(getPosition(),new Point2D.Double(0,0)) > 5000){
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
    public void collided(CollisionData colData) {
        Vector2 correction = Vector2.multiply(colData.getNormal(),colData.getDepth()/2);
        changeXPos(correction.x);
        changeYPos(correction.y);


        if (colData.getCollider() instanceof PhysicsBall){


            Collider collider = colData.getCollider();

            Vector2 n = colData.getNormal();

            Vector2 v = new Vector2(xVel,yVel);
            Vector2 p = new Vector2(getXPositionWorld(),getYPositionWorld());

            int m2 = collider.getMass();
            Vector2 v2 = new Vector2(collider.getXVelocity(),collider.getYVelocity());
            Vector2 p2 = new Vector2(collider.getXPosition(),collider.getYPosition());

            double momentumBefore = VectorMath.length(v) * mass + VectorMath.length(v2) * m2;

            double massComp = (double) (1* m2) / (mass + m2) * 2;
            double velComp = VectorMath.dot(Vector2.difference(v,v2), n);
            double lengthSquared = VectorMath.lengthSquare(Vector2.difference(p,p2));
            double factor = massComp * velComp;

            double vFMagnitude = VectorMath.length(v) - factor;
            Vector2 vFinal = Vector2.multiply(n, vFMagnitude);
            tempV = vFinal;
            vNextFrame = true;
            double momentumAfter = VectorMath.length(v) * mass + VectorMath.length(v2) * m2;

            System.out.println("Before: " + momentumBefore + " After: " + momentumAfter);
            /*
            xVel = vFinal.x;
            yVel = vFinal.y;



             */

        } else {
            Vector2 normal = VectorMath.normalize(colData.getNormal());
            xVel = xVel * normal.x;
            yVel = yVel * normal.y;
        }

    }

    @Override
    public int getCanHit() {
        return HITS_ALL;
    }

    @Override
    public int getHitBy() {
        return HIT_BY_ALL;
    }

    @Override
    public int getHitBoxType() {
        return CIRCLE;
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
        return xVel;
    }

    @Override
    public double getYVelocity() {
        return yVel;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public int getMass() {
        return mass;
    }

    @Override
    public Point2D.Double[] getPoints() {
        return new Point2D.Double[0];
    }
}
