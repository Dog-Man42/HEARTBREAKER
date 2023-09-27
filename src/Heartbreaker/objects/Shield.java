package Heartbreaker.objects;


import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.SoundManager;

import java.awt.*;
import java.awt.geom.*;


public class Shield extends BaseObject {
    private double frames = 0;
    private double theta = 0;
    private int cooldown = 60;
    private int circleCount = 7;
    private ShieldCircle[] circles;
    double bpm = 60;
    double damage = 0;
    private boolean flatlined = false;
    private boolean playShoot = false;
    private boolean played = false;

    public Shield(double x, double y){
        this.xPosition = x;
        this.yPosition = y;
        scale = 1;
        circles = new ShieldCircle[circleCount];
        for(int i = 0; i < circles.length; i++) {
            circles[i] = new ShieldCircle(0, 0);
        }
    }
    public void update(){
        if(!flatlined) {
            frames++;
        }

        double beat_duration = 60 / bpm;
        double totalTime = frames / 60;
        double rate = totalTime % beat_duration;
        double time = rate / beat_duration * (2 * Math.PI);
        //BPM
        //double time = rate;
        //scale = ((Math.abs(Math.sin(time)) * Math.abs((Math.cos(time+.4) - Math.tan(time/2) - .4)))/20) + 1;
        scale = ((Math.abs(-Math.sin(time)) * (Math.abs((-Math.cos(time - 1.5) +Math.tan(time/2) - 4.5)) - 5.4))/25) + 1;
        //scale = 1 * -(Math.abs(Math.sin(frames)) * Math.abs(Math.cos(frames) - 1)) + 5;
        //scale *= -1;
        //scale *= -1;
    }
    public void draw(Graphics2D g) {


        int midX = currentScene.origin.x;
        int midY = currentScene.origin.y;


        for (int i = 0; i < circleCount; i++) {
            if (circles[i].isAlive()) {
                int distance = 100;

                Point2D.Double point = rotatePoint(Math.toRadians((((360.0 / circleCount) + (Math.sin(Math.toRadians(frames / 1.3))) * 6) * i)
                        + Math.sin(Math.toRadians(frames)) * (80 + (damage / 4))), new Point2D.Double(scale * distance, scale * distance));

                Point2D.Double prevPoint = rotatePoint(Math.toRadians((((360.0 / circleCount) + (Math.sin(Math.toRadians((frames - 1) / 1.3))) * 6) * i)
                        + Math.sin(Math.toRadians(frames - 1 )) * (80 + (damage / 4))), new Point2D.Double(scale * distance, scale * distance));
                double deltaX = point.x - prevPoint.x;
                double deltaY = point.y - prevPoint.y;


                //g.drawOval((int) ((midX + point.x) - 13),(int) ((midY + point.y) - 13),26,26);
                circles[i].setXposition(point.x);
                circles[i].setYposition(point.y);
                circles[i].draw(g);

                if (cooldown <= 0 && !flatlined) {
                    playShoot = true;

                    if (circles[i].getHp() <= 15) {
                        if (cooldown % 5 == 0) {
                            currentScene.spawnBullet(((midX + point.x)), ((midY + point.y)), deltaX / 4, deltaY / 4, Math.toDegrees(-Math.atan2(point.x, point.y)), 2, 320, false);
                        }
                    } else {
                        if (cooldown % 10 == 0) {

                            currentScene.spawnBullet(((midX + point.x)), ((midY + point.y)), 0, 0, Math.toDegrees(-Math.atan2(point.x, point.y)), 1, 480, false);
                        }
                    }
                }
            }
        }
        if (playShoot && !played){
            GameFrame.soundManager.playClip(SoundManager.shootShield);
            played = true;
        }

        if(cooldown <= -20 - (int) Math.round(damage/10)){
            playShoot = false;
            cooldown = 275 - (int) Math.round(damage/1.75);
            played = false;
        } else {
            cooldown--;
        }
        //g.drawOval(midX + 20,midY + 20,25,25);

    }
    public void damage(int dmg){
        if(!flatlined) {
            damage += dmg;
        }

    }

    public ShieldCircle[] getCircles(){
        return circles;
    }
    public void setFlatlined(boolean flat){
        this.flatlined = flat;
    }






}


