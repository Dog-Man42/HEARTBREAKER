package Heartbreaker.objects;

import java.awt.*;
import java.awt.geom.Point2D;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.GameObject;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;
import Heartbreaker.engine.vectors.*;

public class Bullet extends GameObject implements Collider{

    private double xvel;
    private double yvel;
    private double speed;
    private double age = 0;
    private int ageLimit;
    private boolean playerBullet;
    private boolean dieNextFrame;
    private int dieInFrames= -1;
    protected int damage = 1;
    protected int hits;
    protected int hitBy;

    public Bullet(double xpos,double ypos, double shooterXvel, double shooterYvel, double angle,double speed, int limit,boolean player){
        playerBullet = player;
        if(player){
            hits = Collider.HITS_ENEMY;
            hitBy = Collider.HIT_BY_ENEMY;
        } else {
            hits = Collider.HITS_PLAYER;
            hitBy = Collider.HIT_BY_PLAYER;
        }
        setXPosition(xpos);
        setYPosition(ypos);

        setScale(2);

        ageLimit = limit;

        xvel = Math.sin(Math.toRadians(angle)) * -speed;
        yvel = Math.cos(Math.toRadians(angle)) * speed;

        if(shooterXvel != 0 && shooterYvel != 0) {
            xvel += shooterXvel;
            yvel += shooterYvel;
            Vector v = VectorMath.normalize(new Vector(xvel, yvel));
            setRotation(Math.toDegrees(-Math.atan2(v.x, v.y)));
        } else {
            setRotation(angle);
        }
        setVertices(new Point2D.Double[]{
                new Point2D.Double(-1,-4),
                new Point2D.Double(-1,4),
                new Point2D.Double(1,4),
                new Point2D.Double(1,-4)
        });

    }

    public Bullet() {
    }

    public void update(){
        changeXPos(xvel * GameFrame.delta);
        changeYPos(yvel * GameFrame.delta);
        int negative = -(getScene().DIAGONAL - getScene().WIDTH);

        if(getXPosition() < negative || getScene().DIAGONAL < getXPosition() || getYPosition() < negative || getScene().DIAGONAL < getYPosition() ){
            getScene().missedCount++;
            getScene().removeObject(this);
        }

        if(ageLimit > 0){
            if(age >= ageLimit-20){
                if(getScale() <= 0.1) {
                    if(playerBullet)
                        getScene().missedCount++;
                    getScene().removeObject(this);
                } else{
                    changeScale(-.1);
                }
            }
        }
        age+= GameFrame.delta;
        if(dieNextFrame) {
            if (dieInFrames <= 0) {
                if (playerBullet)
                    getScene().missedCount++;
                getScene().removeObject(this);
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
        g.drawPolygon(realizePoly());
    }

    public void setXvel(double xvel){
        this.xvel = xvel;
    }

    public void setYvel(double yvel){
        this.yvel = yvel;
    }


    @Override
    public void collided(CollisionData colData) {
        if(!(colData.getCollider() instanceof Bullet)){
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
