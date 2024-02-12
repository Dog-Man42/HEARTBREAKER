package Heartbreaker.objects;

import Heartbreaker.engine.GameObject;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Entity is the base abstract class for all objects which have health, take damage, and collide with other {@link Heartbreaker.engine.collision.Collider colliders}
 *
 * @author tuckt
 */
public abstract class Entity extends GameObject implements Collider {


    /** Max HP which cannot be exceeded */
    private int maxHp;

    /** Hp of Entity which can be modified */
    private int hp;

    /** Amount of Invincibility Frames the entity gains upon the calling of {@link #damage(int)}*/
    private int maxIFrames;

    /** The amount of Invincibility Frames the entity has at any given time */
    private int iFrames;

    /** Set true by {@link #damage(int)} and {@link #setHit(boolean hit)}.
     * Useful for things that should happen the frame after an Entity takes damage, such as behavior or drawing changes.
     * Should be set to false the frame after being set true.
     */
    private boolean hit = false;

    /**
     * Constructor for Entity
     *
     * @param MaxHp Sets {@link #maxHp}
     * @param maxIFrames Sets {@link #maxIFrames}
     */
    public Entity(int MaxHp, int maxIFrames){
        this.maxHp = MaxHp;
        this.hp = maxHp;
        this.maxIFrames = maxIFrames;
    }

    /**
     * Reduces {@link #hp} by dmg. Negative argument "heals" entity.
     * Sets {@link #iFrames} to {@link #maxIFrames} and sets {@link #hit} to true.
     * @param dmg Amount to decrease or increase hp by.
     */
    public void damage(int dmg){
        if(iFrames <= 0) {
            hp -= dmg;
            iFrames = maxIFrames;
            hit = true;
        }
    }

    /**
     * Reduces {@link #hp} by dmg. Negative argument "heals" entity.
     * <p>
     * If triggerIframes is false, will not set {@link #iFrames} to {@link #maxIFrames} and  {@link #hit} to true.
     * @param dmg Amount to decrease or increase hp by.
     * @param triggerIFrames if false, iFrames will not be set to maxIframes
     */
    public void damage(int dmg,boolean triggerIFrames){
        if(iFrames <= 0) {
            hp -= dmg;
            if (triggerIFrames) {
                iFrames = maxIFrames;
                hit = true;
            }
        }
    }


    /** @return current hp */
    public int getHP(){
        return hp;
    }

    /**
     * @param hp what to set hp to */
    public void setHP(int hp){
        this.hp = hp;
        if(hp > maxHp){
            hp = maxHp;
        }
    }

    /** @return iFrames */
    public int getIFrames(){
        return iFrames;
    }

    /** Sets iFrames*/
    public void setIframes(int iframes){
        this.iFrames = iframes;
    }

    /** Lowers iFrames by 1 */
    public void decrementIFrames(){
        this.iFrames--;
    }

    /** Sets iFrames to maxIframes */
    public void maxOutIframes(){
        this.iFrames = maxIFrames;
    }

    /** Sets hit */
    public void setHit(boolean hit){
        this.hit = hit;
    }

    /** @return if hit is true or false */
    public boolean isHit(){
        return this.hit;
    }

    /**
     * By default, decrements iFrames if greater than 0 and sets hit to false if it is true each frame.
     */
    @Override
    public void update() {
        if(iFrames > 0){
            iFrames--;
        }
        if(hit){
            hit = false;
        }
    }

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
