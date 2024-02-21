package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.SoundManager;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class MinorEye extends Entity {

    double frame = 0;
    double radius;
    double irisXPos = 0;
    double irisYPos = 0;
    double irisRadius;
    double pupilXRadius;
    double pupilYRadius;
    boolean firing = false;

    Random rand;
    public MinorEye(double x, double y){
        super(15,20);
        setXPosition(x);
        setYPosition(y);
        setScale(10);
        radius = 4;
        irisRadius = 1.5;
        pupilXRadius = .5;
        pupilYRadius = 1;
        rand = new Random(System.nanoTime());
        frame = rand.nextInt(0,120);
    }
    @Override
    public void update() {
        frame++;
        //setXPosition(getXPosition() + 4*Math.sin(frame/10));
        //setYPosition(getYPosition() + 1.5 * (Math.sin((frame/10)*2) + Math.cos((frame/10)/2.0)));
        setScale(6 + (1.5*Math.sin(frame/15.0)+1.5));
        double theta = -Math.atan2(getScene().player.getXPosition() - getXPosition(), getScene().player.getYPosition() - getYPosition());
        Point2D.Double temp = rotatePoint(theta,new Point2D.Double(0,1));

        irisXPos = temp.x * (1.5 * getScale());
        irisYPos = temp.y * (1.5 * getScale());
        /*
        if(irisXPos + (irisRadius * scale) > radius*scale){
            irisXPos = (radius * scale) - (irisRadius * scale);
        }
        if(irisYPos + (irisRadius * scale) > radius*scale){
            irisYPos = (radius * scale) - (irisRadius * scale);
        }

         */

        if(frame % 240 == 0){
            firing = true;
            GameFrame.soundManager.playClip(SoundManager.shootLaser);
            getScene().spawnBullet(getXPosition() + irisXPos,getYPosition() + irisYPos,0,0,Math.toDegrees(theta) + rand.nextFloat(-.5f,.5f),1200,60,false);
            changeScale(-2);
        }






    }

    @Override
    public void draw(Graphics2D g) {

        if(isHit() || getIFrames() > 0){
            double temp = getScale();
            changeScale(getIFrames() % 4);
            g.setColor(Color.red);
            setIframes(getIFrames() - 1);
            setHit(false);
            g.drawOval((int) (getXPosition() - (radius * getScale())), (int) (getYPosition() - (radius * getScale())), (int) (radius*2 * getScale()), (int) (radius*2 * getScale()));
            if(firing){
                g.setColor(Color.YELLOW);
                firing = false;
            } else {
                g.setColor(Color.CYAN);
            }
            g.drawOval((int) (getXPosition() + irisXPos - (irisRadius * getScale())), (int) (getYPosition() + irisYPos - (irisRadius * getScale())), (int) (irisRadius *2 * getScale()), (int) (irisRadius *2 * getScale()));
            //g.setColor(Color.BLACK);
            g.drawOval((int) (getXPosition() + irisXPos - (pupilXRadius * getScale())), (int) (getYPosition() + irisYPos - (pupilYRadius * getScale())), (int) (pupilXRadius *2 * getScale()), (int) (pupilYRadius *2 * getScale()));
            setScale(temp);
        } else {
            g.setColor(Color.WHITE);
            g.drawOval((int) (getXPosition() - (radius * getScale())), (int) (getYPosition() - (radius * getScale())), (int) (radius*2 * getScale()), (int) (radius*2 * getScale()));
            if(firing){
                g.setColor(Color.ORANGE);
                firing = false;
            } else {
                g.setColor(Color.red);
            }
            g.drawOval((int) (getXPosition() + irisXPos - (irisRadius * getScale())), (int) (getYPosition() + irisYPos - (irisRadius * getScale())), (int) (irisRadius *2 * getScale()), (int) (irisRadius *2 * getScale()));
            //g.setColor(Color.BLACK);
            g.drawOval((int) (getXPosition() + irisXPos - (pupilXRadius * getScale())), (int) (getYPosition() + irisYPos - (pupilYRadius * getScale())), (int) (pupilXRadius *2 * getScale()), (int) (pupilYRadius *2 * getScale()));
        }


    }

    @Override
    public void collided(CollisionData colData) {
        int dmg = colData.getCollider().getDamage();
        if(!isHit() && getIFrames() <= 0) {
            damage(dmg);
            //setXPosition(getXPosition() - colData.getNormal().x * colData.getDepth());
            //setYPosition(getYPosition() - colData.getNormal().x * colData.getDepth());
            if(getHP() <= 0){
                getScene().score += 10 * dmg;
                getScene().removeObject(this);
                GameFrame.soundManager.playClip(SoundManager.deathMinorEye);
            }

            getScene().score += 2 * dmg;
            GameFrame.soundManager.playClip(SoundManager.hurtMinorEye);
        }
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
        return 1;
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
        return radius * getScale();
    }

    @Override
    public Point2D.Double[] getPoints() {
        return new Point2D.Double[0];
    }
}
