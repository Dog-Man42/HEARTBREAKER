package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.SoundManager;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class MinorEye extends BaseObject{

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
        this.xPosition = x;
        this.yPosition = y;
        radius = 4;
        irisRadius = 1.5;
        pupilXRadius = .5;
        pupilYRadius = 1;
        scale = 10;
        rand = new Random(System.nanoTime());
        frame = rand.nextInt(0,120/4);
    }
    @Override
    public void update() {
        frame++;
        setXPosition(getXPosition() + 3*Math.sin(frame/10));
        setYPosition(getYPosition() + (Math.sin((frame/10)*2) + Math.cos((frame/10)/2.0)));
        scale = 4 + (2*Math.sin(frame/15.0)+1.5);
        double theta = -Math.atan2(currentScene.player.getXPosition() - xPosition, currentScene.player.getYPosition() - yPosition);
        Point2D.Double temp = rotatePoint(theta,new Point2D.Double(0,1));

        irisXPos = temp.x * (1.5 * scale);
        irisYPos = temp.y * (1.5 * scale);
        /*
        if(irisXPos + (irisRadius * scale) > radius*scale){
            irisXPos = (radius * scale) - (irisRadius * scale);
        }
        if(irisYPos + (irisRadius * scale) > radius*scale){
            irisYPos = (radius * scale) - (irisRadius * scale);
        }

         */

        if(frame % 120 == 0){
            firing = true;
            GameFrame.soundManager.playClip(SoundManager.shieldOrbDamage);
            currentScene.spawnBullet(getXPosition() + irisXPos,getYPosition() + irisYPos,0,0,Math.toDegrees(theta) + rand.nextFloat(-.5f,.5f),20,60,false);
            scale -= 2;
        }





    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawOval((int) (getXPosition() - (radius * scale)), (int) (getYPosition() - (radius * scale)), (int) (radius*2 * scale), (int) (radius*2 * scale));
        if(firing){
            g.setColor(Color.ORANGE);
            firing = false;
        } else {
            g.setColor(Color.red);
        }
        g.drawOval((int) (getXPosition() + irisXPos - (irisRadius * scale)), (int) (getYPosition() + irisYPos - (irisRadius * scale)), (int) (irisRadius *2 * scale), (int) (irisRadius *2 * scale));
        //g.setColor(Color.BLACK);
        g.drawOval((int) (getXPosition() + irisXPos - (pupilXRadius * scale)), (int) (getYPosition() + irisYPos - (pupilYRadius * scale)), (int) (pupilXRadius *2 * scale), (int) (pupilYRadius *2 * scale));
    }
}
