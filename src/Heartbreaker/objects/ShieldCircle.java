package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.SoundManager;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;

import java.awt.geom.*;
import java.awt.*;

public class ShieldCircle extends BaseObject implements Collider {

    private int hp;
    int radius;
    private boolean alive = true;
    private boolean hit = false;
    private int iframes = 0;
    private double xVel = 0;
    private double yVel = 0;


    public ShieldCircle(double xpos, double ypos){
        this.xPosition = xpos;
        this.yPosition = ypos;
        this.hp = 40;
        this.radius = 20;
    }

    public void setXposition(double xpos){
        xVel = xpos - xPosition;
        xPosition = xpos;
    }
    public void setYposition(double ypos){
        yVel = ypos - yPosition;
        yPosition = ypos;
    }
    public boolean isAlive(){
        return alive;
    }
    public void damage(int dmg){
        if(iframes <= 0) {
            hit = true;
            hp -= dmg;
            iframes = 10;

            if (hp <= 20 && radius >= 20) {
                radius = 10;
                currentScene.shield.damage += .5;
                currentScene.score += 100 * dmg;
                GameFrame.soundManager.playClip(SoundManager.shieldOrbShrink);
            } else {
                currentScene.shield.damage += .25;
                currentScene.score += 1 * dmg;
                GameFrame.soundManager.playClip(SoundManager.shieldOrbDamage);
            }
        }
    }
    public int getHp(){
        return hp;
    }


    @Override
    public void draw(Graphics2D g){

        int midX = currentScene.origin.x;
        int midY = currentScene.origin.y;
        if(iframes > 0){
            iframes--;
        }

        if(hit || iframes > 0){
            g.setColor(Color.red);
            hit = false;
            if(hp <= 0){
                alive = false;
                currentScene.score += 10000;
                GameFrame.soundManager.playClip(SoundManager.shieldOrbDestroy);
                currentScene.shield.damage += 5;
            }
        } else {
            if(hp > 20) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.orange);
            }
        }
        g.drawOval((int) ((midX + xPosition) - radius),(int) ((midY + yPosition) - radius),2 * radius,2 * radius);
    }

    @Override
    public void update() {
    }

    @Override
    public void collided(CollisionData colData) {
        damage(colData.getCollider().getDamage());
    }

    @Override
    public Point2D.Double getPosition(){
        return new Point2D.Double(currentScene.origin.x + xPosition, currentScene.origin.y + yPosition);
    }
    @Override
    public int getCanHit() {
        return Collider.HITS_PLAYER;
    }

    @Override
    public int getHitBy() {
        return Collider.HIT_BY_PLAYER;
    }

    @Override
    public int getHitBoxType() {
        return Collider.CIRCLE;
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
        return this.radius;
    }

    @Override
    public Point2D.Double[] getPoints() {
        return new Point2D.Double[0];
    }
}
