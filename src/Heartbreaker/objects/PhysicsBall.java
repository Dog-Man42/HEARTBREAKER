package Heartbreaker.objects;

import Heartbreaker.engine.GameObject;
import Heartbreaker.engine.GameWindow;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;
import Heartbreaker.engine.vectors.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

public class PhysicsBall extends GameObject implements Collider {

    private int radius;
    private double xVel;
    private double yVel;

    private int mass = 1;

    private Vector2 accel = new Vector2(0,0);
    private Vector2 tempV = null;

    private ArrayList<Link> links = new ArrayList<>();
    private Vector2 normal = null;

    private class Link{
        
        public PhysicsBall a;
        public PhysicsBall b;
        public double restLength;
        public double k;
        
        public Link(PhysicsBall a, PhysicsBall b, double restLength, double force){//
            this.a = a;
            this.b = b;
            this.restLength = restLength;
            this.k = force;

        }

        public void updateForce(PhysicsBall source){
            if(source.equals(a)){

                Vector2 force = Vector2.difference(new Vector2(b.getPosition()), new Vector2(a.getPosition()));
                double x = VectorMath.length(force) - restLength;
                force = Vector2.multiply(VectorMath.normalize(force), k * x);

                // Calculate relative velocity
                Vector2 vA = new Vector2(a.getXVelocity(),a.getYVelocity());
                Vector2 vB = new Vector2(b.getXVelocity(), b.getYVelocity());

                Vector2 relativeVelocity = Vector2.difference(vA, vB);

                // Apply damping force (opposes relative velocity)
                double dampingFactor = 0.05; // Adjust this value as needed
                Vector2 dampingForce = Vector2.multiply(relativeVelocity, -dampingFactor);

                // Combine spring force and damping force
                Vector2 totalForce = Vector2.sum(force, dampingForce);

                a.applyForce(totalForce);
                b.applyForce(Vector2.negate(totalForce));
            }
        }

        public PhysicsBall getOther(PhysicsBall getter){
            if(getter.equals(a)){
                return b;
            } else {
                return a;
            }
        }

        @Override
        public boolean equals(Object obj){
            if(obj == null){
                return false;
            }
            if(obj.getClass() != this.getClass()){
                return false;
            }
            Link other = (Link) obj;

            if(this.a == other.a && this.b == other.b){
                return true;
            }
            if(this.a == other.b && this.b == other.a){
                return true;
            }
            return false;

        }
        
    }


    public PhysicsBall(int x, int y, int radius, int mass, double xvel, double yvel){
        setXPosition(x);
        setYPosition(y);
        this.mass = mass;
        this.radius = radius;
        this.xVel = xvel;
        this.yVel = yvel;
    }
    
    public void addLink(Link link){
        if(!links.contains(link) && links.size() < 6) {
            links.add(link);
        }
    }

    public void applyForce(Vector2 force){

        accel = Vector2.sum(accel, Vector2.divide(force,mass));
    }

    @Override
    public void update(double delta) {
        if(tempV != null){
            xVel = tempV.x;
            yVel = tempV.y;
            tempV = null;
        }
        xVel += accel.x;
        yVel += accel.y;
        accel = new Vector2(0,0);

        changeXPos(xVel * delta);
        changeYPos(yVel * delta);

        for(Iterator<Link> it = links.iterator(); it.hasNext();){
            Link link = it.next();
            PhysicsBall other = link.getOther(this);
            Vector2 displacement = Vector2.difference(new Vector2(getPosition()),new Vector2(other.getPosition()));
            double length = VectorMath.length(displacement);


            if(length > link.restLength * 4){
                it.remove();
            } else {
                link.updateForce(this);
            }


        }

        Vector2 originV = Vector2.difference(new Vector2(0,0), new Vector2(getPosition()));
        originV = VectorMath.normalize(originV);


        double distance = VectorMath.lengthSquare(new Vector2(getPosition()));
        if(Math.sqrt(distance) > 1000) {
            double gForce = 10000 / (1000 + distance);
            applyForce(Vector2.multiply(originV, 20 * gForce));

        }
        /*
        if(calculateDistance(getPosition(),new Point2D.Double(0,0)) > 5000){
            double angle = Math.atan2(getYPosition(), getXPosition());
            // Set the new position of the circle to be on the boundary of the circle with radius 5000
            setXPosition(5000 * Math.cos(angle));
            setYPosition(5000 * Math.sin(angle));

            applyForce(new Vector2(-1.2 * xVel,-1.2 * yVel));
        }

         */


    }

    @Override
    public void draw(Graphics2D g, double delta) {
        Point2D.Double p = getPositionCameraSpace();
        double zoom = getScene().getCamera().getZoom();
        int drawRadius = (int) (getScene().getCamera().getZoom() * radius);
        g.setColor(Color.ORANGE);
        g.drawOval((int) (p.x - drawRadius/2), (int) (p.y - drawRadius/2), drawRadius*2,drawRadius*2);
        if(GameWindow.DEBUG) {
            if(normal != null){
                int xM = (int) (p.x + drawRadius/2);
                int yM = (int) (p.y + drawRadius/2);
                //g.drawLine(xM, yM, (int) (xM + normal.x), (int) (yM + normal.y));
            }
            for (Iterator<Link> it = links.iterator(); it.hasNext(); ) {
                Link link = it.next();
                PhysicsBall other = link.getOther(this);
                Point2D.Double p2 = other.getPositionCameraSpace();
                g.setStroke(new BasicStroke(1));
                g.drawLine((int) (p.x + drawRadius / 2), (int) (p.y + drawRadius / 2), (int) (p2.x + drawRadius / 2), (int) (p2.y + drawRadius / 2));
            }
        }

    }

    @Override
    public void collided(CollisionData colData) {

        Vector2 correction = Vector2.multiply(colData.getNormal(),-colData.getDepth());
        normal = correction;
        changeXPos(correction.x);
        changeYPos(correction.y);


        if (colData.getCollider() instanceof PhysicsBall collider){

            Vector2 p = new Vector2(getPosition());
            Vector2 p2 = new Vector2(collider.getPosition());
            Vector2 n = Vector2.difference(p,p2);

            Vector2 v = new Vector2(xVel,yVel);
            Vector2 v2 = new Vector2(collider.getXVelocity(),collider.getYVelocity());

            int m = mass;
            int m2 = collider.getMass();


            double momentumBefore = VectorMath.length(v) * mass + VectorMath.length(v2) * m2;

            //restitution
            double e = .5;

            double massComp = ((1 + e) * m2) / (double) (m + m2);
            double numerator = VectorMath.dot(Vector2.difference(v,v2), n);
            double lengthSquared = VectorMath.lengthSquare(n);
            double factor =  massComp * (numerator / lengthSquared);
            System.out.println(factor);

            Vector2 vFinal = Vector2.multiply(n,factor);
            vFinal = Vector2.difference(v,vFinal);
            tempV = vFinal;


            //accel = Vector2.sum(accel,vFinal);


            double momentumAfter = VectorMath.length(Vector2.sum(v,vFinal)) * mass + VectorMath.length(v2) * m2;
            //System.out.println("Momentum Difference: " + Math.abs(momentumAfter - momentumBefore));

            if(momentumAfter < 9000) {
                addLink(new Link(this, collider, radius * 2.5, 1.5));
            }

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
