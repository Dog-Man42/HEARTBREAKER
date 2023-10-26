package Heartbreaker.objects;

import Heartbreaker.engine.collision.Collision;
import Heartbreaker.engine.UsesPolar;

import java.awt.*;
import java.awt.geom.Point2D;

public class GravBullet extends Bullet implements UsesPolar {

    private double xvel;
    private double yvel;
    private int age = 0;
    private int ageLimit;
    private Polygon polygon;
    private boolean playerBullet;
    private boolean dieNextFrame = false;
    private int dieInFrames = -1;

    private double theta;
    private double angularVelocity;

    private double radialPosition;
    private double radialVelocity;



    public GravBullet(double xpos,double ypos, double shooterXvel, double shooterYvel, double angle,double speed, int limit,boolean player){
        damage = 2;
        playerBullet = player;
        scale = 10;
        rotation = angle;
        ageLimit = limit;


        xPosition = xpos;
        yPosition = ypos;
        double xval = xpos - currentScene.origin.x;
        double yval = ypos - currentScene.origin.y;

        radialPosition = cartesianToRadius(xval,yval);
        theta = cartesianToTheta(yval,xval);

        double tempRot = Math.toRadians(rotation + theta);

        xvel = speed * Math.cos(Math.toRadians(rotation));
        yvel = speed * Math.sin(Math.toRadians(rotation));

        radialVelocity = 3 * xvel * Math.cos(Math.toRadians(theta)) + 3 * yvel * Math.sin(Math.toRadians(theta));
        angularVelocity = (-xvel * Math.sin(Math.toRadians(theta)) + yvel * Math.cos(Math.toRadians(theta)));


        vertices = new Point2D.Double[]{
                new Point2D.Double(-1,-4),
                new Point2D.Double(-1,4),
                new Point2D.Double(1,4),
                new Point2D.Double(1,-4)
        };
        transformedVertices = new Point2D.Double[vertices.length];
        transformedVertices = rotatePoints(Math.toRadians(angle),vertices);
        polygon = realizePoly(transformedVertices);
    }

    public void update(){
        radialVelocity -= 5/radialPosition;

        theta += angularVelocity;
        radialPosition += radialVelocity;
        //theta += 1;
        Point2D.Double position = rotatePoint(Math.toRadians(theta),new Point2D.Double(0,radialPosition));

        xPosition = position.x + currentScene.origin.x;
        yPosition = position.y + currentScene.origin.y;


        if(radialPosition < 20  || radialPosition > 1000 ){
            currentScene.missedCount++;
            currentScene.addToBulletQueue(this);
        }


        collisionDetection();

        if(ageLimit > 0){
            if(age >= ageLimit-20){
                if(scale <= 1.25) {
                    currentScene.missedCount++;
                    currentScene.addToBulletQueue(this);
                } else{
                    scale -= 1.25;
                }
            }
        }
        age++;




    }

    public void collisionDetection(){
        if(dieNextFrame){
            if(dieInFrames <= 0) {
                currentScene.missedCount++;
                currentScene.addToBulletQueue(this);
            } else {
                dieInFrames--;
            }
        } else {
            if (playerBullet) {
                if (calculateDistance(xPosition, yPosition, currentScene.origin.x, currentScene.origin.y) <= 250) {
                    if (Collision.circlePolygon(new Point2D.Double(xPosition,yPosition),scale/2, currentScene.heart.realizePoints())) {
                        currentScene.heartCount++;
                        currentScene.addToBulletQueue(this);
                        currentScene.damageHeart(damage);
                    }
                }
                if(currentScene.shield != null) {
                    ShieldCircle[] circles = currentScene.shield.getCircles();
                    for(int i = 0; i < circles.length; i++){
                        if(circles[i].isAlive()){
                            Point2D.Double circleCenter = new Point2D.Double(circles[i].getXPosition() +
                                    currentScene.origin.x,circles[i].getYPosition() + currentScene.origin.y);
                            if(Collision.circleCircle(circleCenter,20, new Point2D.Double(xPosition,yPosition), scale/2)){
                                //currentScene.addToBulletQueue(this);
                                currentScene.shieldHitCout++;
                                circles[i].damage(damage);
                                age = ageLimit-60;
                                playerBullet = false;
                                radialVelocity *= -1;
                                angularVelocity *= -1;
                            }
                        }
                    }
                }
            } else {
                if (Collision.circlePolygon(new Point2D.Double(xPosition,yPosition),scale/2, currentScene.player.realizePoints())) {
                    currentScene.player.damage(2);
                    currentScene.addToBulletQueue(this);
                    dieNextFrame = true;
                    dieInFrames = 10;
                }
            }
        }
    }

    public void draw(Graphics2D g){
        g.setColor(Color.cyan);
        g.drawOval((int) xPosition - (int) (scale /2),(int) yPosition - (int) (scale/2),(int) scale,(int) scale);
    }

}
