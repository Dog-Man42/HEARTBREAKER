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
    public static Box box;



    public ArrayList<Bullet> bullets = new ArrayList<>();
    public ArrayList<Bullet> bulletDeleteQueue = new ArrayList<>();


    public Level(){
        collisionManager = new CollisionManager();
    }


    public abstract boolean initialize();

    public void addToBulletQueue(Bullet bullet){
        bulletDeleteQueue.add(bullet);
    }
    public void deleteBullets(){
        if(bulletDeleteQueue.size() > 0) {
            for (int i = bulletDeleteQueue.size() - 1; i >= 0; i--) {
                bullets.remove(bulletDeleteQueue.get(i));
                collisionManager.removeCollider(bulletDeleteQueue.get(i));
                bulletDeleteQueue.remove(bulletDeleteQueue.get(i));

            }
        }
    }
    public void drawBullets(Graphics2D g){
        if(bullets.size() > 0){
            for(int i = bullets.size() - 1; i >= 0; i--){
                bullets.get(i).draw(g);
            }
        }
    }
    public void spawnBullet(double x, double y,double xVel, double yVel, double angle, double speed, int age,boolean player){
        addObject(new Bullet(x,y,xVel,yVel,angle,speed,age,player));
    }
    public void spawnGravBullet(double x, double y,double xVel, double yVel, double angle, double speed, int age, boolean player){
        addObject(new GravBullet(x,y,xVel,yVel,angle,speed,age,player));
    }
    public void updateBullets(){
        if(bullets.size() > 0){
            for(int i = bullets.size() - 1; i >= 0; i--){
                if(!bulletDeleteQueue.contains(bullets.get(i))) {
                    bullets.get(i).update();
                }

            }
        }
    }

    public abstract void draw(Graphics2D g);


    //input Management
    public void keyPressed(KeyEvent e){
        if(player != null)
            player.keyPressed(e);
    }
    public void keyReleased(KeyEvent e){
        if(player != null)
            player.keyReleased(e);
    }

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


    //Heart Management
    public static Heart getHeart(){
        return heart;
    }
    public void damageHeart(int dmg){
        if(heart != null) {
            heart.damage(dmg);
        }
        if(shield != null){
            shield.damage(dmg);
        }
    }

    public abstract void levelBeaten();





}
