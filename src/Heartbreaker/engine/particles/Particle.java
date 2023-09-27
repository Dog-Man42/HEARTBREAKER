package Heartbreaker.engine.particles;

import Heartbreaker.engine.GameFrame;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class Particle {

    protected ParticleEmitter parent;

    protected double xPos;
    protected double yPos;
    protected double rotation;

    protected double xVel;
    protected double yVel;
    protected double rotVel;

    protected int age;
    protected int ageLimit;

    public Particle(ParticleEmitter parent,double xPos, double yPos, double rot, double xVel, double yVel, double rotVel, int ageLimit){
        this.parent = parent;

        this.xPos = xPos;
        this.yPos = yPos;
        this.rotation = rot;

        this.xVel = xVel;
        this.yVel = yVel;
        this.rotVel = rotVel;

        this.ageLimit = ageLimit;
        this.age = 0;
    }
    public Particle(){}

    public void update(){
        age++;
        xPos += xVel;
        yPos += yVel;

        if(age > ageLimit && ageLimit > 0){
            parent.addToDeleteQueue(this);
        }

    }

    public void draw(Graphics2D g){
        g.setColor(Color.white);
        g.setStroke(new BasicStroke(5));
        g.drawLine( (int) xPos,(int) yPos,(int) xPos, (int) yPos);
    }
}
