package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.SoundManager;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;
import Heartbreaker.scenes.Level;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class MinorEye extends Entity {

    private double frame = 0;
    private double radius;
    private double irisXPos = 0;
    private double irisYPos = 0;
    private double irisRadius;
    private double pupilXRadius;
    private double pupilYRadius;
    private boolean firing = false;

    private Level currentLevel;

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

        if(getScene() instanceof Level){
            currentLevel = (Level) getScene();
        }
    }

    @Override
    public void update(double delta) {
        frame++;
        //setXPosition(getXPosition() + 4*Math.sin(frame/10));
        //setYPosition(getYPosition() + 1.5 * (Math.sin((frame/10)*2) + Math.cos((frame/10)/2.0)));
        setScale(6 + (1.5*Math.sin(frame/15.0)+1.5));
        double theta = 0;
        if(currentLevel == null) {
            theta = -Math.atan2(currentLevel.player.getXPosition() - getXPosition(), currentLevel.player.getYPosition() - getYPosition());
        } else {
            theta += .1;
        }
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
            getScene().addObject(new Bullet(getXPosition() + irisXPos,getYPosition() + irisYPos,0,0,Math.toDegrees(theta) + rand.nextFloat(-.5f,.5f),1200,60,false));
            changeScale(-2);
        }






    }

    @Override
    public void draw(Graphics2D g, double delta) {

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
                currentLevel.score += 10 * dmg;
                getScene().removeObject(this);
                GameFrame.soundManager.playClip(SoundManager.deathMinorEye);
            }

            currentLevel.score += 2 * dmg;
            GameFrame.soundManager.playClip(SoundManager.hurtMinorEye);
        }
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
