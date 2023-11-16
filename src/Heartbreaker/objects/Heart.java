package Heartbreaker.objects;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.SoundManager;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;
import Heartbreaker.main.Heartbreaker;
import Heartbreaker.scenes.Level1;
import Heartbreaker.scenes.MainMenu;

public class Heart extends Entity implements Collider {

    private double frames = 0;
    double damage = 0;
    double bpm = 60;
    Random random = new Random();
    HeartGraph graph = new HeartGraph(this);
    private boolean flatlined = false;
    private BrokenHeart left;
    private BrokenHeart right;
    private boolean dead = false;

    public Heart(int x, int y){
        super(200,10);
        Heartbreaker.startTrack();
        this.xPosition = x;
        this.yPosition = y;

        scale = -5;

        vertices = new Point2D.Double[]{
                new Point2D.Double(0,-13),
                new Point2D.Double(14,0),
                new Point2D.Double(16,8),
                new Point2D.Double(14,13),
                new Point2D.Double(10,15),
                new Point2D.Double(5,12),
                new Point2D.Double(0,8),
                new Point2D.Double(-5,12),
                new Point2D.Double(-10,15),
                new Point2D.Double(-14,13),
                new Point2D.Double(-16,8),
                new Point2D.Double(-14,0),
                new Point2D.Double(0,-13)};
        transformedVertices = new Point2D.Double[vertices.length];
        transformedVertices = copyVertices(vertices);


    }


    public void move() {}
    public void update(){
        //frames += .15;
        frames++;

        double beat_duration = 60 / bpm;
        double totalTime = frames / 60;
        double rate = totalTime % beat_duration;
        double time = rate / beat_duration * (2 * Math.PI);
        xPosition = currentScene.origin.x + random.nextInt(-1 - (int)(damage*Math.pow(damage,.2)/20),1 + (int)(damage*Math.pow(damage,.2)/20));
        yPosition = currentScene.origin.y + random.nextInt(-1 - (int)damage/20,1 + (int)damage/20);
        //BPM
        //double time = rate;
        //scale = (Math.abs(Math.sin(time)) * Math.abs((Math.cos(time+.4) - Math.tan(time/2) - .4))) + 3;
        scale = (Math.abs(-Math.sin(time)) * (Math.abs((-Math.cos(time - 1.5) +Math.tan(time/2) - 4.5)) - 5.4)) + 5;
        scale *= -1;
        //scale = 1 * -(Math.abs(Math.sin(frames)) * Math.abs(Math.cos(frames) - 1)) + 5;
        graph.update();
        if(left != null && right != null){
            left.update();
            right.update();
        }

    }
    public void damage(int dmg) {

        if (getIFrames() <= 0) {
            if (!flatlined) {
                GameFrame.soundManager.playClip(SoundManager.heartDamage);
                maxIframes();
                currentScene.score += 100 * dmg;
                damage += .5 * dmg;
                bpm += .5 * dmg;
                graph.setBPM(bpm);
                if(currentScene.shield != null) {
                    currentScene.shield.bpm = bpm;
                }
                Heartbreaker.setBpm((float) bpm);
                if (bpm >= 180) {
                    setIframes(60);
                    //GameFrame.setCurrentScene(new MainMenu());
                    bpm = 0;
                    graph.setBPM(bpm);
                    graph.setFlatlined(true);
                    currentScene.shield.setFlatlined(true);
                    Heartbreaker.setBpm(10);
                    flatlined = true;
                    GameFrame.soundManager.playClip(SoundManager.flatline);
                }
        } else {
            if (!dead) {
                currentScene.score += 1000;
                currentScene.levelBeaten();
                GameFrame.soundManager.playClip(SoundManager.heartbreak);
                dead = true;
                left = new BrokenHeart(1, xPosition, yPosition, scale);
                right = new BrokenHeart(-1, xPosition, yPosition, scale);
            }
            }
        }
    }
    

    public void draw(Graphics2D g){
        if(getIFrames() <= 0) {
            g.setColor(Color.getHSBColor(0f, .7f, .7f));
        } else {
            g.setColor(Color.getHSBColor(0f, .2f, .9f));
            scale *= 1.25;
            decrementIFrames();
        }
        if(!dead) {
            g.drawPolygon(realizePoly(transformedVertices));
        } else {
            left.draw(g);
            right.draw(g);
        }
        graph.draw(g);
    }

    public double getBpm(){
        return bpm;
    }


    @Override
    public void collided(CollisionData colData) {
        if(currentScene instanceof MainMenu){
            GameFrame.setCurrentScene(new Level1());
        } else {
            damage(colData.getCollider().getDamage());
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
        return Collider.POLYGON;
    }

    @Override
    public boolean getStatic() {
        return true;
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
        return 0;
    }

    @Override
    public Point2D.Double[] getPoints() {
        return realizePoints();
    }
}
