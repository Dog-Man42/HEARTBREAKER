package Heartbreaker.objects;


import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.GameObject;
import Heartbreaker.engine.SoundManager;

import java.awt.*;
import java.awt.geom.*;


public class Shield extends GameObject {
    private double timer = 0;

    private double cooldown = 60;
    private int circleCount = 7;
    private ShieldCircle[] circles;
    double bpm = 60;
    double damage = 0;
    private boolean flatlined = false;
    private boolean playShoot = false;
    private boolean played = false;

    public Shield(double x, double y){
        setXPosition(x);
        setYPosition(y);
        setScale(1);
        circles = new ShieldCircle[circleCount];
        for(int i = 0; i < circles.length; i++) {
            circles[i] = new ShieldCircle(0, 0);
            circles[i].setDrawnByScene(false);
            getScene().addObject(circles[i]);
        }
    }
    public void update(){
        if(!flatlined) {
            timer += GameFrame.delta;

        }

        double beat_duration = 60.0 / bpm;
        double totalTime = timer;
        double rate = totalTime % beat_duration;
        double time = rate / beat_duration * (2 * Math.PI);
        //BPM
        //double time = rate;
        //scale = ((Math.abs(Math.sin(time)) * Math.abs((Math.cos(time+.4) - Math.tan(time/2) - .4)))/20) + 1;
        setScale((Heart.heartFunction(time)/25) + 1);
        //scale = 1 * -(Math.abs(Math.sin(frames)) * Math.abs(Math.cos(frames) - 1)) + 5;
        //scale *= -1;
        //scale *= -1;
    }
    public void draw(Graphics2D g) {


        int midX = getScene().origin.x;
        int midY = getScene().origin.y;
        double theta = 60 * timer;
        for (int i = 0; i < circleCount; i++) {
            if (circles[i].isAlive()) {
                int distance = 100;

                Point2D.Double point = rotatePoint(Math.toRadians((((360.0 / circleCount) + (Math.sin(Math.toRadians(theta / 1.3))) * 6) * i)
                        + Math.sin(Math.toRadians(theta)) * (80 + (damage / 4))), new Point2D.Double(getScale() * distance, getScale() * distance));

                Point2D.Double prevPoint = rotatePoint(Math.toRadians((((360.0 / circleCount) + (Math.sin(Math.toRadians((theta - 1) / 1.3))) * 6) * i)
                        + Math.sin(Math.toRadians(theta - 1 )) * (80 + (damage / 4))), new Point2D.Double(getScale() * distance, getScale() * distance));
                double deltaX = point.x - prevPoint.x;
                double deltaY = point.y - prevPoint.y;


                //g.drawOval((int) ((midX + point.x) - 13),(int) ((midY + point.y) - 13),26,26);
                circles[i].setXposition(point.x);
                circles[i].setYposition(point.y);
                circles[i].draw(g);

                if (cooldown <= 0 && !flatlined) {
                    playShoot = true;
                    //Some weirdness with the rounding that I don't feel like fixing
                    if (circles[i].getHP() <= 15) {
                        if (Math.round(cooldown) % 5 == 0) {
                            getScene().spawnBullet(((midX + point.x)), ((midY + point.y)), deltaX / 4, deltaY / 4, Math.toDegrees(-Math.atan2(point.x, point.y)), 120, 320, false);
                        }
                    } else {
                        if (Math.round(cooldown) % 10 == 0) {

                            getScene().spawnBullet(((midX + point.x)), ((midY + point.y)), 0, 0, Math.toDegrees(-Math.atan2(point.x, point.y)), 60, 480, false);
                        }
                    }
                }
            }
        }
        if (playShoot && !played){
            GameFrame.soundManager.playClip(SoundManager.shootShield);
            played = true;
        }

        System.out.println(cooldown);
        if(cooldown <= -20 - (int) Math.round(damage/10)){
            playShoot = false;
            cooldown = 275 - (int) Math.round(damage/1.75);
            played = false;
        } else {
            cooldown -= 60 * GameFrame.delta;
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


