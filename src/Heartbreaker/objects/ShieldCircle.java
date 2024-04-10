package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.SoundManager;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;
import Heartbreaker.scenes.Level;

import java.awt.geom.*;
import java.awt.*;

public class ShieldCircle extends Entity implements Collider {

    int radius;
    private boolean alive = true;
    private double xVel = 0;
    private double yVel = 0;

    private Level currentLevel = null;


    public ShieldCircle(double xpos, double ypos){
        super(40,20);
        setXposition(xpos);
        setYposition(ypos);
        this.radius = 20;

        if(getScene() instanceof Level){
            currentLevel = (Level) getScene();
        }
    }

    public void setXposition(double xpos){
        xVel = xpos - getXPosition();
        setXPosition(xpos);
    }
    public void setYposition(double ypos){
        yVel = ypos - getYPosition();
        setYPosition(ypos);
    }
    public boolean isAlive(){
        return alive;
    }
    public void damage(int dmg){
        if(getIFrames() <= 0) {
            super.damage(dmg);

            if (getHP() <= 20 && radius >= 20) {
                radius = 10;
                currentLevel.shield.damage += .5 * dmg;
                currentLevel.score += 100 * dmg;
                GameFrame.soundManager.playClip(SoundManager.shieldOrbShrink);

            } else {
                currentLevel.shield.damage += .25 * dmg;
                currentLevel.score += 1 * dmg;
                GameFrame.soundManager.playClip(SoundManager.shieldOrbDamage);
            }
        }
    }

    @Override
    public void draw(Graphics2D g, double delta){

        int midX = getScene().origin.x;
        int midY = getScene().origin.y;
        if(getIFrames() > 0){
            decrementIFrames();
        }

        if(isHit() || getIFrames() > 0){
            g.setColor(Color.red);
            setHit(false);
            if(getHP() <= 0){
                alive = false;
                currentLevel.score += 10000;
                GameFrame.soundManager.playClip(SoundManager.shieldOrbDestroy);
                currentLevel.shield.damage += 5;
            }
        } else {
            if(getHP() > 20) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.orange);
            }
        }
        g.drawOval((int) ((midX + getXPosition()) - radius),(int) ((midY + getYPosition()) - radius),2 * radius,2 * radius);
    }

    @Override
    public void update(double delta) {
    }

    @Override
    public void collided(CollisionData colData) {
        damage(colData.getCollider().getDamage());
    }

    @Override
    public Point2D.Double getPosition(){
        return new Point2D.Double(getScene().origin.x + getXPosition(), getScene().origin.y + getYPosition());
    }
    @Override
    public int getCollisionLayer() {
        return Collider.LAYER_2;
    }

    @Override
    public int getCollisionMask() {
        return Collider.MASK_1;
    }

    @Override
    public int getHitBoxType() {
        return Collider.CIRCLE;
    }

    @Override
    public boolean getStatic() {
        return false;
    }

    @Override
    public int getDamage() {
        return 1;
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
