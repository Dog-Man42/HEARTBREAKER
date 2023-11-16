package Heartbreaker.objects;

import Heartbreaker.engine.*;
import Heartbreaker.engine.collision.*;
import Heartbreaker.scenes.MainMenu;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

public class Player extends Entity implements UsesPolar, Collider{

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


    private Point2D mousePosition = new Point2D.Double(0,0);

    public Player(int x, int y){
        super(20,20);
        this.xPosition = x;
        this.yPosition = y;
        radialPosition = y;
        scale = 5;

        vertices = new Point2D.Double[]{ new Point2D.Double(-2,-3), new Point2D.Double(-0,3) , new Point2D.Double(2,-3)};
        transformedVertices = new Point2D.Double[vertices.length];
        transformedVertices = copyVertices(vertices);

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
        rotation  = Math.toDegrees(-Math.atan2(GameFrame.mouseX - getXPosition(),GameFrame.mouseY - getYPosition()));


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

        if(spacePressed || Math.abs(xvel) > 0.01 ||Math.abs(xvel) > 0.01) {
            radialPosition = cartesianToRadius(xPosition - currentScene.origin.x, yPosition - currentScene.origin.y);
            theta = cartesianToTheta(yPosition - currentScene.origin.y, xPosition - currentScene.origin.x);
        }

        double deltaX = xPosition - prevX;
        double deltaY = yPosition - prevY;




        radialVelocity = radialVelocity * .95;
        angularVelocity  = angularVelocity * .95;
        xvel = xvel * .95;
        yvel = yvel * .95;
        if(Math.abs(xvel) <0.01){
            xvel = 0;
        }
        if(Math.abs(yvel) < 0.01){
            yvel = 0;
        }


        if(coolDown <= 0) {

            Point2D.Double[] temp = realizePoints();
            if(spawnBullet) {
                spawnBullet = false;
                GameFrame.soundManager.playClip(SoundManager.shootGeneric);
                currentScene.spawnBullet(temp[1].x, temp[1].y, 0, 0, rotation, 12, 120,true);
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

        if(isHit() || getIFrames() > 0){
            double temp = scale;
            scale += getIFrames() % 4;
            g.setColor(Color.red);
            decrementIFrames();
            setHit(false);
            g.drawPolygon(realizePoly(transformedVertices));
            scale = temp;
        } else {
            g.setColor(Color.blue);
            g.drawPolygon(realizePoly(transformedVertices));
        }
        if(getHP() <= 0){
            GameFrame.setCurrentScene(new MainMenu());
        }

        g.setStroke(new BasicStroke(2));
        g.setFont(new Font("Consolas",Font.PLAIN,40));
        g.drawString("HP: " + getHP(), 0,GameFrame.GAME_HEIGHT - 40);
    }

    public void checkKeys(){

        if(dPressed) {
            if(spacePressed){
                xvel += 2.5 * acceleration;
                if (xvel > 12) {
                    xvel = 12;
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
                xvel -= 2.5 * acceleration;
                if (xvel < -12) {
                    xvel = -12;
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
                yvel -= 2.5 * acceleration;
                if (yvel < -12){
                    yvel = -12;
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
                yvel += 2.5 * acceleration;
                if (yvel > 12) {
                    yvel = 12;
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



    public void damage(int dmg) {
        if(getIFrames() <= 0) {
            super.damage(dmg);
            GameFrame.soundManager.playClip(SoundManager.playerDamage);
        }
    }

    public double getRadialPosition(){
        return radialPosition;
    }
    public double getTheta(){
        return theta;
    }

    @Override
    public void collided(CollisionData colData) {
        if(colData.getCollider().getClass() == Heart.class){
            //currentScene.damageHeart(1);
            radialVelocity = 10;
        } else {
            if (!(colData.getCollider() instanceof Bullet)) {
                xvel = colData.getNormal().x * 2;
                yvel = colData.getNormal().y * 2;
                xPosition -= (colData.getNormal().x * (colData.getDepth()));
                yPosition -= (colData.getNormal().y * (colData.getDepth()));
                //radialVelocity = 0;
                //angularVelocity = 0;
                radialPosition = cartesianToRadius((xPosition - currentScene.origin.x) + 2 * (colData.getNormal().x * colData.getDepth()), (yPosition - currentScene.origin.y) + 2 * (colData.getNormal().y * colData.getDepth()));
                theta = cartesianToTheta((yPosition - currentScene.origin.y) + 2 * (colData.getNormal().x * colData.getDepth()), (xPosition - currentScene.origin.x) + 2 * (colData.getNormal().x * colData.getDepth()));
            }
        }
        if(!(isHit() || getIFrames() > 0)){
            damage(colData.getCollider().getDamage());
        }
    }

    @Override
    public int getCanHit() {
        /*
        if(isHit()){
            return Collider.HITS_NONE;
        }

         */
        return Collider.HITS_ENEMY;
    }

    @Override
    public int getHitBy() {
        /*
        if (isHit()){
            return Collider.HIT_BY_NONE;
        }
         */
        return Collider.HIT_BY_ENEMY;
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
        return 0;
    }

    @Override
    public double getXVelocity() {
        return xvel;
    }

    @Override
    public double getYVelocity() {
        return yvel;
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
