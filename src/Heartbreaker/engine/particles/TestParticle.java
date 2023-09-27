package Heartbreaker.engine.particles;

import java.util.Random;

public class TestParticle extends Particle{
    public TestParticle(ParticleEmitter parent,double xPos, double yPos, double rot, double xVel, double yVel, double rotVel, int ageLimit){
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
    public void update(){
        age++;
        Random rand = new Random();
        if(rand.nextBoolean()){
            if(rand.nextBoolean()){
                double temp = rand.nextDouble(-1.25,1.25);
                if(Math.abs(temp) > 0.75){
                    xVel *= temp;
                }
            } else {
                double temp = rand.nextDouble(-1.25,1.25);
                if(Math.abs(temp) > 0.75){
                    yVel *= temp;
                }
            }
        }
        if(Math.abs(xVel) < 0.1){
            xVel = .1;
        }
        if(Math.abs(yVel) < 0.1){
            yVel = .1;
        }
        xPos += xVel;
        yPos += yVel;

        if(age > ageLimit && ageLimit > 0){
            parent.addToDeleteQueue(this);
        }


    }

}
