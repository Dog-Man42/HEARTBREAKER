package Heartbreaker.objects;

import Heartbreaker.engine.GameObject;

import java.awt.*;
import java.util.Random;


public class Tentacle extends GameObject {
    private double frames = 0;
    private double[] angles;
    private double[] angularVels;

    private TentacleSegment[] segments;

    private final double angularAccel = .3;
    private final double maxAngleVel = 100;
    private double limit = 60;

    private int segmentLength;

    private Random random = new Random();


    public Tentacle(int length, int segmentRadius, double x, double y, double scale) {
        setXPosition(x);
        setYPosition(y);
        setScale(scale);
        this.segmentLength = segmentRadius * 3;

        segments = new TentacleSegment[length];
        for(int i = 0; i < segments.length; i++){
            segments[i] = new TentacleSegment(segmentRadius,1,0,30 * i);
            segments[i].setParent(this,false);
        }


        angles = new double[segments.length];
        angularVels = new double[segments.length];

    }

    public void update(double delta) {
        setRotation(getRotation()+10);
        boolean slow;

        for(int i = 0; i < segments.length; i++){

            double tempAngle = angles[i];
            angles[i] += angularVels[i];

            if(Math.abs(angularVels[i]) > maxAngleVel ){
                angularVels[i] = -angularVels[i] / 4.0;
            }

            if(Math.abs(angles[i]) > limit-20){
                if(Math.abs(angles[i]) > limit){
                    angles[i] = Math.signum(angles[i]) * limit;
                    angularVels[i] = -angularVels[i] / 4.0;
                }
                if(Math.signum(angularVels[i]) == Math.signum(angles[i])) {
                    angularVels[i] *= .5;
                }
            } else {
                slow = false;
            }
            angularVels[i] *=.9;

            if(i > 0) {
                double relativeRotation = Math.toRadians(tempAngle - angles[i]);

                angularVels[i] += (random.nextDouble(-10, 10)/(25.0/i) + angularVels[i-1]/2) * angularAccel;
                angularVels[i-1] += (angularVels[i] - angularVels[i-1])/i * angularAccel;

                segments[i].setPosition(rotatePointAroundPoint(relativeRotation,segments[i].getLocalPos(),segments[i-1].getLocalPos()));


                // Normalize the line segment to maintain its length
                double dx = segments[i].getXPositionWorld() -segments[i-1].getXPositionWorld();
                double dy = segments[i].getYPositionWorld() -segments[i-1].getYPositionWorld();
                double length = Math.sqrt(dx * dx + dy * dy);

                if (length > 0) {
                    double scaleFactor = segmentLength / length;
                    dx *= scaleFactor;
                    dy *= scaleFactor;

                    segments[i].setXPosition(segments[i-1].getXPositionWorld() + dx);
                    segments[i].setYPosition(segments[i-1].getYPositionWorld() + dy);
                }
            } else {
                angularVels[i] += (random.nextDouble(-1, 1) * angularAccel);
                segments[i].setPosition(rotatePoint(Math.toRadians(angles[i]),segments[i].getLocalPos()));
            }

            //System.out.println(this);
        }
    }


    public void draw(Graphics2D g, double delta) {

        for(int i = 0; i < segments.length; i++) {
            if(i > 0){
                g.setColor(Color.GREEN);
                g.drawLine((int) segments[i-1].getXPositionWorld(), (int) segments[i-1].getYPositionWorld(), (int) segments[i].getXPositionWorld(),(int) segments[i].getYPositionWorld());
            }
            segments[i].draw(g, delta );
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Angles[");
        for(double d : angles){
            sb.append(d + ",");
        }
        sb.append("] Angular Velocities[");
        for(double d : angularVels){
            sb.append(d + ",");
        }
        sb.append("]");
        return sb.toString();
    }


}


