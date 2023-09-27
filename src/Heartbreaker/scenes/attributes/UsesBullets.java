package Heartbreaker.scenes.attributes;

import Heartbreaker.objects.Bullet;
import Heartbreaker.objects.GravBullet;

import java.awt.*;
import java.util.ArrayList;

/**
 * Allows for a Level or Scene to use Bullets.
 * Bullets.clear() and bulletDeleteQueue.clear() is recommended to be included in the initialize() method.
 * add updateBullets() and deleteBullets() to updateScene().
 * add drawBullets() to draw().
 */

public interface UsesBullets {
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Bullet> bulletDeleteQueue = new ArrayList<>();

    default void addToBulletQueue(Bullet bullet){
        bulletDeleteQueue.add(bullet);
    }
    default void deleteBullets(){
        if(bulletDeleteQueue.size() > 0) {
            for (int i = bulletDeleteQueue.size() - 1; i >= 0; i--) {
                bullets.remove(bulletDeleteQueue.get(i));
                bulletDeleteQueue.remove(bulletDeleteQueue.get(i));

            }
        }
    }
    default void drawBullets(Graphics2D g){
        if(bullets.size() > 0){
            for(int i = bullets.size() - 1; i >= 0; i--){
                bullets.get(i).draw(g);
            }
        }
    }
    default void spawnBullet(double x, double y,double xVel, double yVel, double angle, double speed, int age,boolean player){
        bullets.add(new Bullet(x,y,xVel,yVel,angle,speed,age,player));
    }
    default void spawnGravBullet(double x, double y,double xVel, double yVel, double angle, double speed, int age, boolean player){
        bullets.add(new GravBullet(x,y,xVel,yVel,angle,speed,age,player));
    }
    default void updateBullets(){
        if(bullets.size() > 0){
            for(int i = bullets.size() - 1; i >= 0; i--){
                if(!bulletDeleteQueue.contains(bullets.get(i))) {
                    bullets.get(i).update();
                }

            }
        }
    }





}
