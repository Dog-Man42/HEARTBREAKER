package Heartbreaker.objects;

import Heartbreaker.engine.Camera;
import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.GameObject;
import Heartbreaker.engine.GameWindow;
import Heartbreaker.engine.input.KeyInput;
import Heartbreaker.engine.input.MouseInput;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;
import Heartbreaker.engine.vectors.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.security.Key;
import java.util.ArrayList;
import java.util.Iterator;

public class PhysicsBall extends GameObject implements Collider {

    private double radius;
    private double xVel;
    private double yVel;

    private int mass = 1;

    private Vector2 accel = new Vector2(0,0);
    private Vector2 tempV = null;

    public ArrayList<Link> links = new ArrayList<>();
    private Vector2 normal = null;
    private Vector2 velocity = null;
    private Vector2 friction = null;

    public int linkLimit = 4;

    private boolean followed = false;
    private int followCool = 0;
    private double originalRadius = 0;
    private double originalDensity = 1;

    private Vector2 velLast = new Vector2(0,0);

    public static class Link{
        
        public PhysicsBall a;
        public PhysicsBall b;
        public double restLength;
        public double k;

        public double strength;



        
        public Link(PhysicsBall a, PhysicsBall b, double restLength, double force){
            this.a = a;
            this.b = b;
            this.restLength = restLength;
            this.strength = ((a.mass + b.mass) / 4.0);
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
                double dampingFactor = .5; // Adjust this value as needed
                Vector2 dampingForce = Vector2.multiply(relativeVelocity, -dampingFactor);

                // Combine spring force and damping force
                Vector2 totalForce = Vector2.sum(force, dampingForce);

                //plasticity
                if(getLength() > restLength){

                    double factor = ((k / (restLength - strength)) / 10) * GameFrame.delta;

                    restLength += factor/2;
                    strength -= factor;
                    k -= factor;
                }

                a.applyImpulse(Vector2.multiply(totalForce,b.mass));
                b.applyImpulse(Vector2.multiply(totalForce,-a.mass));
            }
        }

        public PhysicsBall getOther(PhysicsBall getter){
            if(getter.equals(a)){
                return b;
            } else {
                return a;
            }
        }

        public double getLength(){
            double temp = VectorMath.length(Vector2.difference(new Vector2(b.getPosition()), new Vector2(a.getPosition())));
            temp = temp - a.radius - b.radius;
            return temp;
        }

        public boolean shouldBreak(){
            return this.getLength() > (1.5*restLength + strength);
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


    public PhysicsBall(int x, int y, double radius, int mass, double xvel, double yvel){
        setXPosition(x);
        setYPosition(y);
        this.mass = mass;
        this.radius = radius;
        this.xVel = xvel;
        this.yVel = yvel;
        this.originalRadius = radius;
        this.originalDensity = getDensity();
    }
    
    public void addLink(Link link){
        if(!links.contains(link) && links.size() < linkLimit) {
            links.add(link);

        }
    }

    public boolean hasLink(Link link){
        return links.contains(link) || links.size() >= linkLimit;
    }

    public void applyImpulse(Vector2 impulse){
        Vector2 temp = Vector2.divide(impulse,mass);
        accel = Vector2.sum(accel, Vector2.multiply(temp, GameFrame.delta));
    }

    //Compression
    public void applyImpulseShrink(Vector2 impulse){
        Vector2 temp = Vector2.divide(impulse,mass);
        accel = Vector2.sum(accel, Vector2.multiply(temp, GameFrame.delta));
        double sqrt = Math.sqrt(VectorMath.length(Vector2.multiply(temp, GameFrame.delta)));
        double density = getDensity();
        double factor =  (sqrt / (density));
        factor *= GameFrame.delta;



        //factor = Math.min(0,factor);
        if(radius > .5){
            radius = radius - factor;
        }
        if(radius < .5){
            //System.out.println(factor);
            radius = .5;
        }
    }

    private double getDensity(){
        return (mass / (Math.PI * radius * radius));
    }

    @Override
    public void update(double delta) {
        if(tempV != null){
            xVel = tempV.x;
            yVel = tempV.y;
            tempV = null;
        }
        friction = new Vector2(0,0);
        xVel += accel.x;
        yVel += accel.y;
        accel = new Vector2(0,0);

        changeXPos(xVel * delta);
        changeYPos(yVel * delta);


        if(followed){
            double x = (getXPosition() + getScene().getCamera().getxPosition())/2;
            double y = (getYPosition() + getScene().getCamera().getyPosition())/2;
            getScene().camera.setPosition(new Point2D.Double(x,y));
            if(followCool <= 0) {
                if (KeyInput.isKeyPressed(KeyEvent.VK_E)) {
                    followed = false;
                    getScene().getCamera().setPosition(new Point2D.Double(0, 0));
                } else if(MouseInput.isButtonPressed(MouseEvent.BUTTON1)){
                    followed = false;
                }
            } else {
                followCool--;
            }
        } else {
            if(followCool <= 0) {
                if (Math.abs(VectorMath.length(Vector2.difference(new Vector2(getPositionCameraSpace()), new Vector2(MouseInput.getPosition())))) <= radius * 4 * getScene().getCamera().getZoom()) {
                    if(KeyInput.isKeyPressed(KeyEvent.VK_K)){
                        links.clear();
                    }
                    if (MouseInput.isButtonPressed(MouseEvent.BUTTON1)) {
                        followed = true;
                        followCool = 60;
                    }

                }
            }
        }


        //Jank ass Pseudo-pressure
        if(accel.equals(new Vector2(0,0))){
            if(radius < originalRadius) {

                radius += (originalRadius*2) * delta;
                if(radius > originalRadius){
                    radius = originalRadius;
                }
            }
        } else {
            if( radius < originalRadius){
                radius += (originalRadius/1.5) * delta;
                if(radius > originalRadius){
                    radius = originalRadius;
                }
            }
        }

        for(Iterator<Link> it = links.iterator(); it.hasNext();){
            Link link = it.next();
            PhysicsBall other = link.getOther(this);
            Vector2 displacement = Vector2.difference(new Vector2(getPosition()),new Vector2(other.getPosition()));
            double length = VectorMath.length(displacement);


            if(link.shouldBreak() || links.size() > linkLimit || (getDensity() - originalDensity) >= .1){
                it.remove();
            } else {
                link.updateForce(this);
            }


        }
 
        Vector2 originV = Vector2.difference(new Vector2(0,0), new Vector2(getPosition()));
        originV = VectorMath.normalize(originV);


        double distance = VectorMath.length(new Vector2(getPosition()));
        if(KeyInput.isKeyPressed(KeyEvent.VK_J) || KeyInput.isKeyPressed(KeyEvent.VK_H)) {

            Point mp = MouseInput.getPosition();
            Camera cam = getScene().getCamera();
            Point origin = getScene().origin;

            // Step 1: Subtract the origin
            double adjustedX = (mp.x - origin.x);
            double adjustedY = (mp.y - origin.y);

            // Step 2: Divide by the zoom factor to undo scaling
            adjustedX /= cam.getZoom();
            adjustedY /= cam.getZoom();

            // Step 3: Add the camera's position to adjust back to world space
            double worldX = adjustedX + cam.getxPosition();
            double worldY = adjustedY + cam.getyPosition();
            Vector2 forcePosition = new Vector2(worldX, worldY);
            Vector2 forceVector = Vector2.difference(forcePosition, new Vector2(getPosition()));
            double mDist = VectorMath.length(forceVector);
            forceVector = VectorMath.normalize(forceVector);
            if (mDist > 10) {
                if (KeyInput.isKeyPressed(KeyEvent.VK_J))
                    applyImpulse(Vector2.multiply(forceVector, mass * 1000000 * (1 / (mDist * mDist))));
                else if (KeyInput.isKeyPressed(KeyEvent.VK_H))
                    applyImpulse(Vector2.multiply(forceVector, -mass * 1000000 * (1 / (mDist * mDist))));
            }

        }

        if(distance >= .5) {
            if(!KeyInput.isKeyPressed(KeyEvent.VK_G)) {
                applyImpulse(Vector2.multiply(originV, mass * 10 * (1 / (.001 * distance + .005))));
            } else {
                applyImpulse(Vector2.multiply(originV, -mass * 10 * (1 / (.001 * distance + .005))));
            }
        }
        if(distance > 7500){
            applyImpulse(Vector2.multiply(originV,   mass * 100));
        }



    }

    @Override
    public void draw(Graphics2D g, double delta) {
        Point2D.Double p = getPositionCameraSpace();
        double zoom = getScene().getCamera().getZoom();
        double drawRadius = (zoom * radius);
        boolean conditionW = p.x > GameFrame.GAME_WIDTH + drawRadius || p.x < 0 - drawRadius;
        boolean conditionH = p.y > GameFrame.GAME_HEIGHT + drawRadius || p.y < 0 - drawRadius;
        if(!conditionH && !conditionW) {

            if (followed) {

                g.setColor(Color.WHITE);
                g.fillOval((int) (p.x - drawRadius), (int) (p.y - drawRadius), (int) (drawRadius * 2.0), (int) (drawRadius * 2.0));
                if (GameWindow.DEBUG) {
                    Vector2 vel = new Vector2(xVel, yVel);
                    g.setColor(vectorToColor(vel));
                    g.drawLine((int) p.x, (int) p.y, (int) (p.x + vel.x * zoom), (int) (p.y + vel.y * zoom));
                    if (friction != null) {
                        g.setColor(Color.CYAN);
                        g.drawLine((int) p.x, (int) p.y, (int) (p.x + friction.x * zoom), (int) (p.y + friction.y * zoom));
                    }
                    if (normal != null) {
                        g.setColor(Color.pink);
                        g.drawLine((int) p.x, (int) p.y, (int) (p.x + normal.x * zoom), (int) (p.y + normal.y * zoom));
                    }
                }

            } else {
                Vector2 vel = new Vector2(xVel, yVel);


                velLast = new Vector2((vel.x + velLast.x) / 2.0, (vel.y + velLast.y) / 2.0);
                //vel = Vector2.difference(vel,velLast);

                Color col = vectorToColor(Vector2.divide(velLast,1));
                double massFact = mapToRange(mass,1,25,0.1,1);
                double deltaDensity = mapToRange(getDensity() - originalDensity,0,.1,.1,1);
                double massDense = deltaDensity * massFact * 5;
                massDense = mapToRange(massDense,0,2,0.1,1);
                col = new Color((int) (col.getRed() * massDense), (int) (col.getGreen() * massDense), (int) (col.getBlue() * massDense));
                g.setColor(col);
                g.fillOval((int) (p.x - drawRadius), (int) (p.y - drawRadius), (int) drawRadius * 2, (int) drawRadius * 2);
            }

            g.setColor(Color.white);
            if (GameWindow.DEBUG) {
                for (Iterator<Link> it = links.iterator(); it.hasNext(); ) {
                    Link link = it.next();
                    double hue = mapToRange(link.getLength(), 0, 2 * link.restLength, 0, 300);

                    g.setColor(Color.getHSBColor(((float) hue) / 255, 1, 1));
                    PhysicsBall other = link.getOther(this);
                    Point2D.Double p2 = other.getPositionCameraSpace();
                    g.setStroke(new BasicStroke(1));
                    g.drawLine((int) p.x, (int) p.y, (int) p2.x, (int) p2.y);
                }
            }
        }

    }


    private Color vectorToColor(Vector2 v){
        float red = (float) mapToColorRange(v, "red");
        float green = (float) mapToColorRange(v, "green");
        float blue = (float) mapToColorRange(v, "blue");
        return new Color(red,green,blue);
    }


    private double mapToColorRange(Vector2 vec2, String color) {
        // Map value to the specific color range
        double mappedValue;
        if (color.equals("red")) {
            double r = VectorMath.dot(vec2,new Vector2(-Math.sqrt(3)/2.0,-0.5));
            mappedValue = mapToRange(r, -1, 1, 0, 1);
        } else if (color.equals("green")) {
            double g = VectorMath.dot(vec2,new Vector2(0,1));
            mappedValue = mapToRange(g, -1, 1, 0, 1);
        } else if (color.equals("blue")) {
            double b = VectorMath.dot(vec2,new Vector2(Math.sqrt(3)/2.0,-0.5));
            mappedValue = mapToRange(b, -1, 1, 0, 1);
        } else {
            throw new IllegalArgumentException("Invalid color");
        }

        // Ensure the mapped value is within [0, 1] range
        return Math.max(0, Math.min(1, mappedValue));
    }
    private double mapToRange(double input, double minOriginal, double maxOriginal, double min, double max) {


        double ratio = (input - minOriginal) / (maxOriginal - minOriginal);
        ratio = ratio * (max - min) + min;
        if(ratio > max){
            return max;
        }
        return Math.max(ratio, min);
    }

    @Override
    public void collided(CollisionData colData) {

        Vector2 correction = Vector2.multiply(colData.getNormal(),colData.getDepth() *  (colData.getCollider().getMass() / (double) (mass + colData.getCollider().getMass())));;
        applyImpulseShrink(correction);
        normal = correction;
        changeXPos(correction.x);
        changeYPos(correction.y);


        if (colData.getCollider() instanceof PhysicsBall collider){

            Vector2 p = new Vector2(getPosition());
            Vector2 p2 = new Vector2(collider.getPosition());
            Vector2 n = Vector2.difference(p,p2);
            normal = n;

            Vector2 v = new Vector2(xVel,yVel);
            Vector2 v2 = new Vector2(collider.getXVelocity(),collider.getYVelocity());

            int m = mass;
            int m2 = collider.getMass();


            double momentumBefore = VectorMath.length(v) * mass + VectorMath.length(v2) * m2;

            //restitution
            double e = .85;

            double massComp = ((1 + e) * m2) / (double) (m + m2);
            double numerator = VectorMath.dot(Vector2.difference(v,v2), n);
            double lengthSquared = VectorMath.lengthSquare(n);
            double factor =  massComp * (numerator / lengthSquared);

            Vector2 vFinal = Vector2.multiply(n,factor);
            vFinal = Vector2.difference(v,vFinal);
            tempV = vFinal;




            double frictionCoefficient = .75;



            Vector2 relativeVelocity = Vector2.difference(v, v2);
            double multiplier = VectorMath.dot(VectorMath.normalize(v),VectorMath.normalize(v2));
            double normalForceMagnitude = (colData.getDepth()) * ( multiplier);

            Vector2 vNormalized = VectorMath.normalize(relativeVelocity);

            double frictionForceMagnitude = frictionCoefficient * normalForceMagnitude;

            // Calculate the friction force
            Vector2 frictionForce = Vector2.multiply(vNormalized, -frictionForceMagnitude);


            friction = Vector2.divide(Vector2.sum(friction,frictionForce),2);
            tempV = Vector2.difference(tempV,frictionForce);
            //applyImpulse(frictionForce);



            double dMomentum = (VectorMath.length(v) * mass) - (VectorMath.length(v2) * m2);

            if(Math.abs(dMomentum) <= 1000 && (getDensity() - originalDensity) <= .2) {
                Link link = new Link(this, collider, radius + collider.getRadius(), 1 * Math.min(.8,Math.max(Math.random(),.85)));
                if(!hasLink(link) && !collider.hasLink(link)) {
                    if(collider.links.size() < collider.linkLimit && links.size() < linkLimit) {
                        addLink(link);
                        collider.addLink(link);
                        //experimental averaging of spring links
                        double sum = 0;
                        for (Link l : links) {
                            sum += l.restLength;
                        }
                        sum = sum / links.size();
                        for (Link l : links) {
                            l.restLength = sum;
                        }
                    }
                }
            }

        } else {

            Vector2 normal = VectorMath.normalize(colData.getNormal());
            xVel = xVel * normal.x;
            yVel = yVel * normal.y;
        }

    }

    @Override
    public int getCollisionLayer() {
        return LAYER_ALL;
    }

    @Override
    public int getCollisionMask() {
        return MASK_All;
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
