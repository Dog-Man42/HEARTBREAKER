package Heartbreaker.objects;

import java.awt.*;
import java.awt.geom.Point2D;

import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.Collision;
import Heartbreaker.engine.collision.CollisionData;
import Heartbreaker.engine.vectors.*;

public class Bullet extends BaseObject implements Collider{

    private double xvel;
    private double yvel;
    private int age = 0;
    private int ageLimit;
    private Polygon polygon;
    private boolean playerBullet;
    private boolean dieNextFrame;
    private int dieInFrames= -1;
    protected int damage = 1;
    private int hits;
    private int hitBy;

    public Bullet(double xpos,double ypos, double shooterXvel, double shooterYvel, double angle,double speed, int limit,boolean player){
        playerBullet = player;
        if(player){
            hits = Collider.HITS_ENEMY;
            hitBy = Collider.HIT_BY_ENEMY;
        } else {
            hits = Collider.HITS_PLAYER;
            hitBy = Collider.HIT_BY_PLAYER;
        }
        xPosition = xpos;
        yPosition = ypos;
        scale = 2;

        ageLimit = limit;

        xvel = Math.sin(Math.toRadians(angle)) * -speed;
        yvel = Math.cos(Math.toRadians(angle)) * speed;
        if(shooterXvel != 0 && shooterYvel != 0) {
            xvel += shooterXvel;
            yvel += shooterYvel;
            Vector v = VectorMath.normalize(new Vector(xvel, yvel));
            rotation = Math.toDegrees(-Math.atan2(v.x, v.y));
        } else {
            rotation = angle;
        }
        vertices = new Point2D.Double[]{
                new Point2D.Double(-1,-4),
                new Point2D.Double(-1,4),
                new Point2D.Double(1,4),
                new Point2D.Double(1,-4)
        };
        transformedVertices = copyVertices(vertices);
        //transformedVertices = rotatePoints(Math.toRadians(rotation),vertices);
        polygon = realizePoly(transformedVertices);

    }

    public Bullet() {
    }

    public void update(){
        xPosition += xvel;
        yPosition += yvel;
        int negative = -(currentScene.DIAGONAL - currentScene.WIDTH);

        if(xPosition < negative || currentScene.DIAGONAL < xPosition || yPosition < negative || currentScene.DIAGONAL < yPosition ){
            currentScene.missedCount++;
            currentScene.removeObject(this);
        }
        //collisionDetection();
        if(ageLimit > 0){
            if(age >= ageLimit-20){
                if(scale <= 0.1) {
                    if(playerBullet)
                        currentScene.missedCount++;
                    currentScene.removeObject(this);
                } else{
                    scale -= .1;
                }
            }
        }
        polygon = realizePoly(transformedVertices);
        age++;
        if(dieNextFrame) {
            if (dieInFrames <= 0) {
                if (playerBullet)
                    currentScene.missedCount++;
                currentScene.removeObject(this);
            } else {
                dieInFrames--;
            }
        }
    }


    public boolean isPlayerBullet() {
        return playerBullet;
    }

    public void draw(Graphics2D g){
        g.setColor(Color.yellow);
        g.drawPolygon(polygon);
    }

    @Override
    public void collided(CollisionData colData) {
        currentScene.removeObject(this);
        dieNextFrame = true;
        dieInFrames = 10;
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
    public int getHitBoxType() {
        return Collider.POLYGON;
    }

    @Override
    public boolean getStatic() {
        return false;
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
    public double getRadius() {
        return 0;
    }

    @Override
    public Point2D.Double[] getPoints(){
        return realizePoints();
    }
}
