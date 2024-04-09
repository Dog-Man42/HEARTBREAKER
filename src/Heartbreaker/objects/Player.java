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
    private double coolDown = 0;


    private double theta;
    private double angularVelocity;
    private boolean angularChanged = false;

    private double radialPosition;
    private double radialVelocity;
    private boolean radialChanged = false;

    private final double maxAngularVel = 24000;
    private final double maxRadial = 360;
    private final double acceleration = .1;



    private Point2D mousePosition = new Point2D.Double(0,0);

    public Player(int x, int y){
        super(20,20);
        setXPosition(x);
        setYPosition(y);
        setScale(5);
        radialPosition = y;


        setVertices(new Point2D.Double[]{ new Point2D.Double(-2,-3), new Point2D.Double(-0,3) , new Point2D.Double(2,-3)});

    }

    public void update(double delta) {


        checkKeys(delta);
        //System.out.println(Arrays.toString(getTransformedVertices()));

        //This causes subtle frame time spikes that cause somewhat noticeable jitter.
        //However, it could be due to the way of the GameFrame sending mouseevent to the level and then level to player.
        //There is a chance this happens multiple times a frame. Would benefit from an attempt at limiting this
        //TODO try to fix frame time stutter


        double targetRotation = Math.toDegrees(-Math.atan2(MouseInput.getPosition().x - getXPosition(), MouseInput.getPosition().y  - getYPosition()));

        setRotation(getRotation() + targetRotation);
        setRotation(Math.toDegrees(-Math.atan2(MouseInput.getPosition().x - getXPosition(),MouseInput.getPosition().y - getYPosition())));


        double prevX = getXPosition();
        double prevY = getYPosition();

        theta += (angularVelocity * delta) / radialPosition;
        radialPosition += radialVelocity * delta;

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
        setXPosition(position.x + getScene().origin.x);
        setYPosition(position.y + getScene().origin.y);

        changeXPos(xvel * delta);
        changeYPos(yvel * delta);

        if(Keyboard.isKeyPressed(KeyEvent.VK_SPACE) || Math.abs(xvel) > 0.01 ||Math.abs(xvel) > 0.01) {
            radialPosition = cartesianToRadius(getXPosition() - getScene().origin.x, getYPosition() - getScene().origin.y);
            theta = cartesianToTheta(getYPosition() - getScene().origin.y, getXPosition() - getScene().origin.x);
        }

        double decel = Math.pow(1.0/(60 * .96), delta);
        radialVelocity *= decel;
        angularVelocity *= decel;
        xvel *= decel;
        yvel *= decel;
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
                getScene().addObject(new Bullet(temp[1].x, temp[1].y, 0, 0, getRotation(), 720, 120,true));
                coolDown = 8;
            } else if(spawnGravBullet){
                spawnGravBullet = false;
                GameFrame.soundManager.playClip(SoundManager.shootGrav);
                getScene().addObject(new GravBullet(temp[1].x, temp[1].y, 0,0, getRotation(), 60, 240,true));
                coolDown = 16;
            }
        } else {
            coolDown -= 60 * delta;

        }


        //transformedVertices = rotatePoints(Math.toRadians(rotation),vertices);

    }

    public void draw(Graphics2D g, double delta){

        if(isHit() || getIFrames() > 0){
            double temp = getScale();
            changeScale(getIFrames() % 4);
            g.setColor(Color.red);
            decrementIFrames();
            setHit(false);
            g.drawPolygon(realizePoly());
            setScale(temp);
        } else {
            g.setColor(Color.blue);
            g.drawPolygon(realizePolyCameraSpace(getScene().getCamera()));
        }
        if(getHP() <= 0){
            GameFrame.setCurrentScene(new MainMenu());
        }

        g.setStroke(new BasicStroke(2));
        g.setFont(new Font("Consolas",Font.PLAIN,40));
        g.drawString("HP: " + getHP(), 0,GameFrame.GAME_HEIGHT - 40);
    }

    public void checkKeys(double delta){

        boolean spacePressed = Keyboard.isKeyPressed(KeyEvent.VK_SPACE);

        if(Keyboard.isKeyPressed(KeyEvent.VK_D)) {
            if(spacePressed){
                xvel += 150 * 60 * acceleration * delta;
                if (xvel > 720) {
                    xvel = 720;
                }

            } else {
                angularVelocity -= maxAngularVel * (60 * acceleration) * delta;
                if (angularVelocity < -maxAngularVel) {
                    angularVelocity = -maxAngularVel;
                }
            }
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_A)) {
            if (spacePressed) {
                xvel -= 150 * 60 * acceleration * delta;
                if (xvel < -720) {
                    xvel = -720;
                }

            } else {

                angularVelocity += maxAngularVel * (60 * acceleration) * delta;
                if (angularVelocity > maxAngularVel) {
                    angularVelocity = maxAngularVel;
                }
            }
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_W)) {
            if(spacePressed){
                yvel -= 150 * 60 * acceleration * delta;
                if (yvel < -720){
                    yvel = -720;
                }

            } else {

                radialVelocity -= 60 * maxRadial * acceleration * delta;

                if (radialVelocity < -maxRadial) {
                    radialVelocity = -maxRadial;
                }
            }
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_S)) {
            if(spacePressed){
                yvel += 150 * 60 * acceleration * delta;
                if (yvel > 720) {
                    yvel = 720;
                }

            } else {
                radialVelocity += 60 * maxRadial * acceleration * delta;
                if (radialVelocity > maxRadial) {
                    radialVelocity = maxRadial;
                }
            }
        }
        if(MouseInput.isButtonPressed(MouseEvent.BUTTON1)) {
            if(coolDown <= 0)
                spawnBullet = true;
        }
        if(MouseInput.isButtonPressed(MouseEvent.BUTTON3)) {
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
            radialVelocity = 600;
        } else {
            if (!(colData.getCollider() instanceof Bullet)) {
                xvel = colData.getNormal().x * 600;
                yvel = colData.getNormal().y * 600;
                changeXPos(-(colData.getNormal().x * (colData.getDepth())));
                changeYPos(-(colData.getNormal().y * (colData.getDepth())));
                //radialVelocity = 0;
                //angularVelocity = 0;
                radialPosition = cartesianToRadius((getXPosition() - getScene().origin.x) + 2 * (colData.getNormal().x * colData.getDepth()), (getYPosition() - getScene().origin.y) + 2 * (colData.getNormal().y * colData.getDepth()));
                theta = cartesianToTheta((getYPosition() - getScene().origin.y) + 2 * (colData.getNormal().x * colData.getDepth()), (getXPosition() - getScene().origin.x) + 2 * (colData.getNormal().x * colData.getDepth()));
            }
        }
        if(!(isHit() || getIFrames() > 0)){
            damage(colData.getCollider().getDamage());
        }
    }

    @Override
    public int getCollisionLayer() {
        /*
        if(isHit()){
            return Collider.HITS_NONE;
        }

         */
        return Collider.LAYER_1;
    }

    @Override
    public int getCollisionMask() {
        /*
        if (isHit()){
            return Collider.HIT_BY_NONE;
        }
         */
        return Collider.MASK_2;
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
