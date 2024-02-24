package Heartbreaker.scenes;

import Heartbreaker.engine.*;
import Heartbreaker.engine.collision.CollisionManager;
import Heartbreaker.engine.scenes.Scene;
import Heartbreaker.objects.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class Level extends Scene{


    public int WIDTH = GameFrame.GAME_WIDTH;
    public int HEIGHT = GameFrame.GAME_HEIGHT;
    public int DIAGONAL = (int)Math.round(Math.sqrt(WIDTH * WIDTH + HEIGHT * HEIGHT));
    public int score = 0;
    public int missedCount = 0;
    public int shieldHitCout = 0;
    public int heartCount = 0;

    public ArrayList<Box> boxes = new ArrayList<>();

    public static Player player;
    public static Heart heart;
    public static Shield shield;



    public Level(){

        collisionManager = new CollisionManager();
    }

    public void spawnBullet(double x, double y,double xVel, double yVel, double angle, double speed, int age,boolean player){
        addObject(new Bullet(x,y,xVel,yVel,angle,speed,age,player));
    }
    public void spawnGravBullet(double x, double y,double xVel, double yVel, double angle, double speed, int age, boolean player){
        addObject(new GravBullet(x,y,xVel,yVel,angle,speed,age,player));
    }

    public abstract void draw(Graphics2D g, double delta);

    public void mouseMoved(MouseEvent e) {
        if(player != null)
            player.mouseMoved(e);
    }
    public void mousePressed(MouseEvent e) {
        if(player != null)
            player.mousePressed(e);
    }
    public void mouseReleased(MouseEvent e) {
        if(player != null)
            player.mouseReleased(e);
    }

    public abstract void levelBeaten();





}
