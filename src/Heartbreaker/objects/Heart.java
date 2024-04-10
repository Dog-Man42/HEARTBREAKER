package Heartbreaker.objects;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.SoundManager;
import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;
import Heartbreaker.main.Heartbreaker;
import Heartbreaker.scenes.Level;
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

    private Level currentLevel = null;

    public Heart(int x, int y){

        super(200,10);

        Heartbreaker.startTrack();
        setXPosition(x);
        setXPosition(y);

        setScale(-5);

        if(getScene() instanceof Level){
            currentLevel = (Level) getScene();
        }

        setVertices(new Point2D.Double[]{
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
                new Point2D.Double(0,-13)});


    }



    public void update(double delta){
        //frames += .15;
        frames+= delta;
        double beat_duration = 60.0 / bpm;
        double totalTime = frames;
        double rate = totalTime % beat_duration;
        double time = rate / beat_duration * (2 * Math.PI);
        setXPosition(getScene().origin.x + random.nextInt(-1 - (int)(damage*Math.pow(damage,.2)/20),1 + (int)(damage*Math.pow(damage,.2)/20)));
        setYPosition(getScene().origin.y + random.nextInt(-1 - (int)damage/20,1 + (int)damage/20));

        setScale( heartFunction(time) + 4);
        //setScale((Math.abs(-Math.sin(time)) * (Math.abs((-Math.cos(time - 1.5) +Math.tan(time/2) - 4.5)) - 5.4)) + 5);
        setScale(-getScale());
        //scale = 1 * -(Math.abs(Math.sin(frames)) * Math.abs(Math.cos(frames) - 1)) + 5;
        graph.update(delta);
        if(left != null && right != null){
            left.update(delta);
            right.update(delta);
        }
    }

    public static double heartFunction(double time){
        double sft = time + Math.PI - 1;
        double a = -1 * Math.pow( Math.sin(.5 * sft + 2.9), 10);
        double b = .5 * Math.pow( Math.sin(.5 * sft + 5), 20);
        double c = 4 * Math.pow( Math.sin(.5 * sft - 0.01145), 40);
        double d = .5 * Math.pow( Math.sin(.5 * sft + 0.7), 50);

        return a + b + c + d ;
    }



    //Rewrite WILL be needed.
    public void damage(int dmg) {

        if (getIFrames() <= 0) {
            if (!flatlined) {
                GameFrame.soundManager.playClip(SoundManager.heartDamage);
                maxOutIframes();
                damage += .5 * dmg;
                bpm += .5 * dmg;
                graph.setBPM(bpm);

                if(currentLevel != null) {
                    currentLevel.shield.damage += .5 * dmg;
                    currentLevel.score += 100 * dmg;
                    if (currentLevel.shield != null) {
                        currentLevel.shield.bpm = bpm;
                    }
                }
                Heartbreaker.setBpm((float) bpm);
                if (bpm >= 180) {
                    setIframes(60);
                    //GameFrame.setCurrentScene(new MainMenu());
                    bpm = 0;
                    graph.setBPM(bpm);
                    graph.setFlatlined(true);
                    if(currentLevel != null) {
                        currentLevel.shield.setFlatlined(true);
                    }
                    Heartbreaker.setBpm(10);
                    flatlined = true;
                    GameFrame.soundManager.playClip(SoundManager.flatline);
                }
        } else {
            if (!dead) {
                if(currentLevel != null) {
                    currentLevel.score += 1000;
                    currentLevel.levelBeaten();
                }
                GameFrame.soundManager.playClip(SoundManager.heartbreak);
                dead = true;
                left = new BrokenHeart(1, getXPosition(), getYPosition(), getScale());
                right = new BrokenHeart(-1, getXPosition(), getYPosition(), getScale());
            }
            }
        }
    }
    

    public void draw(Graphics2D g, double delta){
        if(getIFrames() <= 0) {
            g.setColor(Color.getHSBColor(0f, .7f, .7f));
        } else {
            g.setColor(Color.getHSBColor(0f, .2f, .9f));
            setScale(getScale() * 1.25 );
            decrementIFrames();
        }
        if(!dead) {
            g.drawPolygon(realizePolyCameraSpace(getScene().getCamera()));
        } else {
            left.draw(g,delta);
            right.draw(g,delta);
        }
        graph.draw(g,delta);
    }

    public double getBpm(){
        return bpm;
    }


    @Override
    public void collided(CollisionData colData) {
        if(getScene() instanceof MainMenu){
            GameFrame.setCurrentScene(new Level1());
        } else {
            damage(colData.getCollider().getDamage());
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
        return Collider.POLYGON;
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
