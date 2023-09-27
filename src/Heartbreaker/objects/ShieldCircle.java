package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.SoundManager;

import java.awt.geom.*;
import java.awt.*;

public class ShieldCircle extends BaseObject{

    private int hp;
    int radius;
    private boolean alive = true;
    private boolean hit = false;
    private int iframes = 0;

    ShieldCircle(double xpos, double ypos){
        this.xPosition = xpos;
        this.yPosition = ypos;
        this.hp = 40;
        this.radius = 20;
    }

    public void setXposition(double xpos){
        xPosition = xpos;
    }
    public void setYposition(double ypos){
        yPosition = ypos;
    }
    public boolean isAlive(){
        return alive;
    }
    public void damage(int dmg){
        if(iframes <= 0) {
            hit = true;
            hp -= dmg;
            iframes = 10;

            if (hp <= 20 && radius >= 20) {
                radius = 10;
                currentScene.shield.damage += .5;
                currentScene.score += 100 * dmg;
                GameFrame.soundManager.playClip(SoundManager.shieldOrbShrink);
            } else {
                currentScene.shield.damage += .25;
                currentScene.score += 1 * dmg;
                GameFrame.soundManager.playClip(SoundManager.shieldOrbDamage);
            }
        }
    }
    public int getHp(){
        return hp;
    }

    public void draw(Graphics g){

        int midX = currentScene.origin.x;
        int midY = currentScene.origin.y;
        if(iframes > 0){
            iframes--;
        }

        if(hit || iframes > 0){
            g.setColor(Color.red);
            hit = false;
            if(hp <= 0){
                alive = false;
                currentScene.score += 10000;
                GameFrame.soundManager.playClip(SoundManager.shieldOrbDestroy);
                currentScene.shield.damage += 5;
            }
        } else {
            if(hp > 20) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.orange);
            }
        }
        g.drawOval((int) ((midX + xPosition) - radius),(int) ((midY + yPosition) - radius),2 * radius,2 * radius);
    }

}
