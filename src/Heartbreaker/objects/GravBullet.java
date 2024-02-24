package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.UsesPolar;
import Heartbreaker.engine.collision.CollisionData;

import java.awt.*;
import java.awt.geom.Point2D;

public class GravBullet extends Bullet implements UsesPolar {

    private double xvel;
    private double yvel;
    private double age = 0;
    private int ageLimit;
    private Polygon polygon;
    private boolean playerBullet;
    private boolean dieNextFrame = false;
    private int dieInFrames = -1;

    private double theta;
    private double angularVelocity;

    private double radialPosition;
    private double radialVelocity;
    private int hits;
    private int hitBy;



    public GravBullet(double xpos,double ypos, double shooterXvel, double shooterYvel, double angle,double speed, int limit,boolean player){
        damage = 2;
        playerBullet = player;
        if(player){
            hits = Collider.HITS_ENEMY;
            hitBy = Collider.HIT_BY_ENEMY;
        } else {
            hits = Collider.HITS_PLAYER;
            hitBy = Collider.HIT_BY_PLAYER;
        }
        setScale(10);
        setRotation(angle);
        ageLimit = limit;


        setXPosition(xpos);
        setYPosition(ypos);
        double xval = xpos - getScene().origin.x;
        double yval = ypos - getScene().origin.y;

        radialPosition = cartesianToRadius(xval,yval);
        theta = cartesianToTheta(yval,xval);

        double tempRot = Math.toRadians(getRotation() + theta);

        xvel = speed * Math.cos(Math.toRadians(getRotation()));
        yvel = speed * Math.sin(Math.toRadians(getRotation()));

        radialVelocity = 3 * xvel * Math.cos(Math.toRadians(theta)) + 3 * yvel * Math.sin(Math.toRadians(theta));
        angularVelocity = (-xvel * Math.sin(Math.toRadians(theta)) + yvel * Math.cos(Math.toRadians(theta)));


        setVertices(new Point2D.Double[]{
                new Point2D.Double(-1,-4),
                new Point2D.Double(-1,4),
                new Point2D.Double(1,4),
                new Point2D.Double(1,-4)
        });

    }

    public void update(double delta){
        radialVelocity -= 200/radialPosition;

        theta += angularVelocity * delta;
        radialPosition += radialVelocity * delta;
        //theta += 1;
        Point2D.Double position = rotatePoint(Math.toRadians(theta),new Point2D.Double(0,radialPosition));

        setXPosition(position.x + getScene().origin.x);
        setYPosition(position.y + getScene().origin.y);


        if(radialPosition < 20  || radialPosition > 1000 ){
            getScene().missedCount++;
            getScene().removeObject(this);
        }


        //collisionDetection();

        if(ageLimit > 0){
            if(age >= ageLimit-20){
                if(getScale() <= 1.25) {
                    getScene().missedCount++;
                    getScene().removeObject(this);
                } else{
                    setScale(-1.25);
                }
            }
        }
        age+= 60 * delta;




    }

    public void draw(Graphics2D g, double delta){
        g.setColor(Color.cyan);
        g.drawOval((int) getXPosition() - (int) (getScale() /2),(int) getYPosition() - (int) (getScale()/2),(int) getScale(),(int) getScale());


    }
    @Override
    public void collided(CollisionData colData) {
        if(colData.getCollider().getClass() == ShieldCircle.class){
            radialVelocity *= -.9;
            angularVelocity *= -.9;
            if(playerBullet){
                hits = Collider.HITS_PLAYER;
                hitBy = Collider.HIT_BY_PLAYER;
            }
            return;

        }
        if(!(colData.getCollider() instanceof Bullet)) {
            getScene().removeObject(this);
            dieNextFrame = true;
            dieInFrames = 10;
        }
    }
    @Override
    public int getCanHit() {
        return hits;
    }

    @Override
    public int getHitBy() {
        return hitBy;
    }
    @Override

    public int getDamage() {
        return damage;
    }

    @Override
    public double getXVelocity() {
        return xvel;
    }

    @Override
    public double getYVelocity() {
        return yvel;
    }

    @Override
    public int getHitBoxType(){
        return Collider.CIRCLE;
    }

    @Override
    public double getRadius(){
        return getScale()/2.0;
    }

}
