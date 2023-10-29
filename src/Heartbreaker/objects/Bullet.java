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
            hitBy = Collider.HIT_BY_NONE;
        } else {
            hits = Collider.HITS_PLAYER;
            hitBy = Collider.HIT_BY_NONE;
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
                currentScene.addToBulletQueue(this);
            } else {
                dieInFrames--;
            }
        }
    }

    /*
    public void collisionDetection(){
        if(dieNextFrame){
            if(dieInFrames <= 0) {
                if(playerBullet)
                    currentScene.missedCount++;
                currentScene.addToBulletQueue(this);
            } else {
                dieInFrames--;
            }
        } else {
            if(playerBullet) {
                if (calculateDistance(xPosition, yPosition, currentScene.origin.x, currentScene.origin.y) <= 250) {
                    if (Collision.polygonPolygon(currentScene.heart.realizePoints(),realizePoints())) {
                        currentScene.heartCount++;
                        currentScene.addToBulletQueue(this);
                        currentScene.damageHeart(damage);
                    }
                }
                if(currentScene.shield != null) {
                    ShieldCircle[] circles = currentScene.shield.getCircles();
                    for(int i = 0; i < circles.length; i++){
                        if(circles[i].isAlive()){
                            Point2D.Double circleCenter = new Point2D.Double(circles[i].getXPosition() +
                                    currentScene.origin.x,circles[i].getYPosition() + currentScene.origin.y);
                            if(Collision.circlePolygon(circleCenter,20, realizePoints())){
                                //currentScene.addToBulletQueue(this);
                                currentScene.shieldHitCout++;
                                circles[i].damage(damage);
                                currentScene.addToBulletQueue(this);
                            }
                        }
                    }
                }
            } else {
                if (Collision.polygonPolygon(realizePoints(), currentScene.player.realizePoints())) {
                    currentScene.player.damage(1);
                    currentScene.addToBulletQueue(this);
                    dieNextFrame = true;
                    dieInFrames = 10;
                }
            }
        }
    }
     */

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
