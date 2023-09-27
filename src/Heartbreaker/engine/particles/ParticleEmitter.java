package Heartbreaker.engine.particles;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.awt.*;
import java.util.Random;

public class ParticleEmitter {

    public double xPos;
    public double yPos;
    private double rotation;

    private double xVel;
    private double yVel;
    private double rotVel;

    private int age;
    private int ageLimit;

    //How many frames in between spawns, above. 0 is always emitting.
    private double frequency;
    private int cooldown = 0;

    private Class<? extends Particle> particleType;
    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<Particle> deleteQueue = new ArrayList<>();

    public ParticleEmitter(Class<? extends Particle> type, double xPos, double yPos, double frequency){
        this.xPos = xPos;
        this.yPos = yPos;
        particleType = type;
        this.frequency = frequency;
    }
    public ParticleEmitter(Class<? extends Particle> type, double xPos, double yPos, double frequency, int ageLimit){
        particleType = type;
        this.ageLimit = ageLimit;
        this.frequency = frequency;
    }

    public void update(){
        Random rand = new Random();
        if(cooldown < 1){
            try{
                Constructor<? extends Particle> c = particleType.getDeclaredConstructor(ParticleEmitter.class, double.class, double.class, double.class, double.class, double.class, double.class, int.class);
                //System.out.println(c);
                particles.add(c.newInstance(this,xPos,yPos,0,rand.nextDouble(-1,1),rand.nextDouble(-1,1),0,10000));
                cooldown = (int) Math.round(60 / frequency);
            } catch(InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }
        } else {
            cooldown--;
        }
        for(int i = particles.size() - 1; i >= 0; i--){
            particles.get(i).update();
        }
        killParticles();
    }

    public void addToDeleteQueue(Particle particle){
        deleteQueue.add(particle);
    }
    public void killParticles(){
        for(int i = deleteQueue.size() - 1; i >= 0; i--){
            particles.remove(i);
            deleteQueue.remove(i);
        }
    }
    public void draw(Graphics2D g){
        for(int i = 0; i < particles.size(); i++){
            particles.get(i).draw(g);
        }
    }

    public void setPos(double x, double y){
        xPos = x;
        yPos = y;
    }

}
