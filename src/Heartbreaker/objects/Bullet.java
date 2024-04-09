package Heartbreaker.objects;

import java.awt.*;
import java.awt.geom.Point2D;

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
    protected int colLayer;
    protected int colMask;

    public Bullet(double xpos,double ypos, double shooterXvel, double shooterYvel, double angle,double speed, int limit,boolean player){
        playerBullet = player;
        if(player){
            colLayer = Collider.LAYER_1;
            colMask = Collider.MASK_2;
        } else {
            colLayer = Collider.LAYER_2;
            colMask = Collider.MASK_1;
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
            Vector2 v = VectorMath.normalize(new Vector2(xvel, yvel));
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

    public void update(double delta){
        changeXPos(xvel * delta);
        changeYPos(yvel * delta);
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
        age+= delta;
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

    public void draw(Graphics2D g, double delta){
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
    public int getCollisionLayer() {
        return colLayer;
    }

    @Override
    public int getCollisionMask() {
        return colMask;
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
