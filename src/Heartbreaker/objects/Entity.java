package Heartbreaker.objects;

import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;

import java.awt.*;
import java.awt.geom.Point2D;

public class Entity extends BaseObject implements Collider {

    private int maxHp;
    private int hp;
    private int maxIFrames;
    private int iFrames;
    private boolean hit = false;

    public Entity(int MaxHp, int maxIFrames){
        this.maxHp = MaxHp;
        this.hp = maxHp;
        this.maxIFrames = maxIFrames;
    }
    public void damage(int dmg){
        hp -= dmg;
        iFrames = maxIFrames;
        hit = true;
    }
    public void damage(int dmg,boolean triggerIFrames){
        hp -= dmg;
        if(triggerIFrames){
            iFrames = maxIFrames;
            hit = true;
        }
    }

    public int getHP(){
        return hp;
    }

    public void setHP(int hp){
        this.hp = hp;
    }
    public int getIFrames(){
        return iFrames;
    }
    public void setIframes(int iframes){
        this.iFrames = iframes;
    }
    public void decrementIFrames(){
        this.iFrames--;
    }
    public void maxIframes(){
        this.iFrames = maxIFrames;
    }
    public void setHit(boolean hit){
        this.hit = hit;
    }
    public boolean isHit(){
        return this.hit;
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D g) {}
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
