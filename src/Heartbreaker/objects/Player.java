package Heartbreaker.objects;

import Heartbreaker.engine.*;
import Heartbreaker.scenes.MainMenu;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

public class Player extends BaseObject implements UsesPolar {

    private double xvel = 0;
    private double yvel = 0;
    private boolean spawnBullet;
    private boolean spawnGravBullet;
    private int coolDown = 0;

    private Point2D prevMouseX;

    private int MouseX;
    private int MouseY;

    private double theta;
    private double angularVelocity;
    private boolean angularChanged = false;

    private double radialPosition;
    private double radialVelocity;
    private boolean radialChanged = false;

    private final double maxAngularVel = 400;
    private final double maxRadial = 6;
    private final double acceleration = .1;

    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;
    private boolean spacePressed = false;
    private boolean shiftPressed = false;
    private boolean leftClicked = false;
    private boolean rightClicked = false;

    private boolean hit = false;
    public int hp;
    private int iframes = 0;

    private Point2D mousePosition = new Point2D.Double(0,0);

    public Player(int x, int y){
        this.xPosition = x;
        this.yPosition = y;
        radialPosition = y;
        scale = 5;

        vertices = new Point2D.Double[]{ new Point2D.Double(-2,-3), new Point2D.Double(-0,3) , new Point2D.Double(2,-3)};
        transformedVertices = new Point2D.Double[vertices.length];
        transformedVertices = copyVertices(vertices);
        this.hp = 5;

    }
    //input
    public void keyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_A -> aPressed = true;
            case KeyEvent.VK_D -> dPressed = true;
            case KeyEvent.VK_W -> wPressed = true;
            case KeyEvent.VK_S -> sPressed = true;
            case KeyEvent.VK_SPACE -> spacePressed = true;
        }

    }
    public void keyReleased(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_A -> aPressed = false;
            case KeyEvent.VK_D -> dPressed = false;
            case KeyEvent.VK_W -> wPressed = false;
            case KeyEvent.VK_S -> sPressed = false;
            case KeyEvent.VK_SPACE -> spacePressed = false;
        }

    }

    public void mouseMoved(MouseEvent e){
        Point2D prevMousePosition = mousePosition;
        mousePosition = e.getPoint();
    }
    public void mousePressed(MouseEvent e){

        switch(e.getButton()) {
            case MouseEvent.BUTTON1 -> leftClicked = true;
            case MouseEvent.BUTTON3 -> rightClicked = true;
        }
    }
    public void mouseReleased(MouseEvent e){
        switch(e.getButton()) {
            case MouseEvent.BUTTON1 -> leftClicked = false;
            case MouseEvent.BUTTON3 -> rightClicked = false;
        }
    }

    public void update() {

        checkKeys();

        //This causes subtle frame time spikes that cause somewhat noticeable jitter.
        //However, it could be due to the way of the GameFrame sending mouseevent to the level and then level to player.
        //There is a chance this happens multiple times a frame. Would benefit from an attempt at limiting this
        //TODO try to fix frame time stutter


        double targetRotation = Math.toDegrees(-Math.atan2(GameFrame.mouseX - xPosition, GameFrame.mouseY  - yPosition));

        rotation = (rotation + targetRotation);
        rotation  = Math.toDegrees(-Math.atan2(GameFrame.mouseX - xPosition,GameFrame.mouseY - yPosition));


        double prevX = xPosition;
        double prevY = yPosition;

        theta += angularVelocity / radialPosition;
        radialPosition += radialVelocity;

        if(radialPosition < -2){
            theta += 180;
            radialPosition = -radialPosition;


        }
        if(Math.abs(theta) > 360){
            theta = theta - (360 * Math.signum(theta));
        }

        if(radialPosition >= 600){
            radialPosition = 600;
        }
        Point2D.Double position = rotatePoint(Math.toRadians(theta),new Point2D.Double(0,radialPosition));
        xPosition = position.x + currentScene.origin.x;
        yPosition = position.y + currentScene.origin.y;

        xPosition += xvel;
        yPosition += yvel;

        if(spacePressed) {
            radialPosition = cartesianToRadius(xPosition - currentScene.origin.x, yPosition - currentScene.origin.y);
            theta = cartesianToTheta(yPosition - currentScene.origin.y, xPosition - currentScene.origin.x);
        }

        double deltaX = xPosition - prevX;
        double deltaY = yPosition - prevY;




        radialVelocity = radialVelocity * .95;
        angularVelocity  = angularVelocity * .95;
        xvel = xvel * .95;
        yvel = yvel * .95;

        if(Collision.polygonPolygon(currentScene.heart.realizePoints(),realizePoints())){
            damage(1);
            currentScene.damageHeart(1);
            radialVelocity = 10;

        }

        if(coolDown <= 0) {

            Point2D.Double[] temp = realizePoints();
            if(spawnBullet) {
                spawnBullet = false;
                GameFrame.soundManager.playClip(SoundManager.shootGeneric);
                currentScene.spawnBullet(temp[1].x, temp[1].y, 0, 0, rotation, 10, 60,true);
                coolDown = 8;
            } else if(spawnGravBullet){
                spawnGravBullet = false;
                GameFrame.soundManager.playClip(SoundManager.shootGrav);
                currentScene.spawnGravBullet(temp[1].x, temp[1].y, 0,0, rotation, 1, 240,true);
                coolDown = 16;
            }
        } else {
            coolDown --;

        }


        //transformedVertices = rotatePoints(Math.toRadians(rotation),vertices);

    }

    public void draw(Graphics2D g){

        //if(Collision.polygonPolygon(currentScene.heart.realizePoints(),realizePoints())){
        if(hit || iframes > 0){
            double temp = scale;
            scale += iframes % 4;
            g.setColor(Color.red);
            iframes--;
            hit = false;
            g.drawPolygon(realizePoly(transformedVertices));
            scale = temp;
        } else {
            g.setColor(Color.blue);
            g.drawPolygon(realizePoly(transformedVertices));
        }
        if(hp <= 0){
            GameFrame.setCurrentScene(new MainMenu());
        }

        g.setStroke(new BasicStroke(2));
        g.setFont(new Font("Consolas",Font.PLAIN,40));
        g.drawString("HP: " + hp, 0,GameFrame.GAME_HEIGHT - 40);
    }

    public void checkKeys(){

        if(dPressed) {
            if(spacePressed){
                xvel += 1.5 * acceleration;
                if (xvel > 4) {
                    xvel = 4;
                }

            } else {
                angularVelocity -= maxAngularVel * acceleration;
                if (angularVelocity < -maxAngularVel) {
                    angularVelocity = -maxAngularVel;
                }
            }
        }
        if(aPressed) {
            if (spacePressed) {
                xvel -= 1.5 * acceleration;
                if (xvel < -4) {
                    xvel = -4;
                }

            } else {
                angularVelocity += maxAngularVel * acceleration;
                if (angularVelocity > maxAngularVel) {
                    angularVelocity = maxAngularVel;
                }
            }
        }
        if(wPressed) {
            if(spacePressed){
                yvel -= 1.5 * acceleration;
                if (yvel < -4) {
                    yvel = -4;
                }

            } else {
                radialVelocity -= maxRadial * acceleration;
                if (radialVelocity < -maxRadial) {
                    radialVelocity = -maxRadial;
                }
            }
        }
        if(sPressed) {
            if(spacePressed){
                yvel += 1.5 * acceleration;
                if (yvel > 4) {
                    yvel = 4;
                }

            } else {
                radialVelocity += maxRadial * acceleration;
                if (radialVelocity > maxRadial) {
                    radialVelocity = maxRadial;
                }
            }
        }
        if(leftClicked) {
            if(coolDown <= 0)
                spawnBullet = true;
        }
        if(rightClicked) {
            if(coolDown <= 0)
                spawnGravBullet = true;
        }
    }


    public boolean isHit() {
        return hit;
    }

    public void damage(int dmg) {
        if(iframes <= 0) {
            GameFrame.soundManager.playClip(SoundManager.playerDamage);
            this.hit = true;
            this.hp -= dmg;
            iframes = 15;
        }
    }

    public double getRadialPosition(){
        return radialPosition;
    }
    public double getTheta(){
        return theta;
    }

}
