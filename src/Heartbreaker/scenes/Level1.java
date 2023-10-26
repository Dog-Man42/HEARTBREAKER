package Heartbreaker.scenes;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import Heartbreaker.engine.*;
import Heartbreaker.objects.*;
import Heartbreaker.scenes.attributes.UsesBullets;

public class Level1 extends Level implements UsesBullets {

    private long startTime = System.currentTimeMillis();
    private boolean beaten = false;
    private long beatTime;




    public boolean initialize(){

        origin = new Point(GameFrame.GAME_WIDTH/2,GameFrame.GAME_HEIGHT/2);

        spawnPlayer();
        spawnHeart();
        spawnShield();
        boxes = new ArrayList<>();

        bullets.clear();
        bulletDeleteQueue.clear();
        Random rand = new Random();
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        objects.add(new MinorEye(rand.nextInt(0,GameFrame.GAME_WIDTH),rand.nextInt(0,GameFrame.GAME_HEIGHT)));
        return true;
    }



    public void updateScene(){
        if(beaten){
            if(System.currentTimeMillis() - beatTime >= 10000){
                GameFrame.setCurrentScene(new MainMenu());
            }
        }
        long updateNano = System.currentTimeMillis();
        player.update();
        heart.update();
        if(shield != null)
            shield.update();

        if(boxes.size() > 0){
            for(Box box : boxes){
                box.update();
            }
        }
        updateObjects();
        updateBullets();
        deleteBullets();
    }

    public void spawnPlayer(){
        player = new Player(0,300);
    }
    public void spawnBox(double x, double y){
        boxes.add(new Heartbreaker.objects.Box(x,y,5));
    }
    public void spawnHeart(){heart = new Heart(origin.x,origin.y);}
    public void spawnShield(){shield = new Shield(origin.x,origin.y);}


    @Override
    public void draw(Graphics2D g){
        long drawNano = System.currentTimeMillis();
        g.setStroke(new BasicStroke(1));
        radialGrid(g);

        g.setStroke(new BasicStroke(3));
        if(shield != null)
            shield.draw(g);
        if(boxes.size() > 0){
            for(Box box : boxes){
                box.draw(g);
            }
        }
        drawBullets(g);
        player.draw(g);
        heart.draw(g);
        drawObjects(g);
        g.setColor(Color.RED);
        //g.drawLine(0,GAME_HEIGHT/2,GAME_WIDTH,GAME_HEIGHT/2);
        //g.drawLine(GAME_WIDTH/2,0,GAME_WIDTH/2,GAME_HEIGHT);

        g.setColor(Color.white);
        g.setFont(new Font("Consolas",Font.PLAIN,20));
        g.drawString("Frame Rate: " + GameFrame.FPS,0,20);
        g.drawString("Frame Time MS: " + String.format("%.2f", GameFrame.avgFrameTime),0,45);
        g.setFont(new Font("Consolas",Font.PLAIN,30));
        g.drawString("SCORE: " + score,0,80);
        double mod = (heartCount *.6) + (shieldHitCout*.2);
        double ds = (System.currentTimeMillis() - startTime) / 2000;
        g.drawString("Accuracy Mod: " + mod/(ds + missedCount*.25 + 1),0,100);
    }

    public void radialGrid(Graphics g){
        g.setColor(new Color(50,50,50));
        double resolution = 24;
        double diagonal = Math.sqrt(Math.pow(WIDTH,2) + Math.pow(HEIGHT,2));
        for(int i = 0; i < resolution; i++){
            double theta = (360.0/resolution) * i;


            Point2D.Double point = rotatePoint(Math.toRadians(theta), new Point2D.Double(0,diagonal));
            g.drawLine(origin.x,origin.y,(int) (origin.x + point.x),(int) (origin.y + point.y));

        }
        for(int i = 0; i < resolution; i++) {
            g.setColor(new Color(150 / (i/2 + 1), 150 / (i/2 + 1), 150 / (i/2 + 1)));
            int size = (int) (diagonal / resolution) * i;
            g.drawOval(origin.x - (size / 2), origin.y - size / 2, size, size);
        }

    }
    public Point2D.Double rotatePoint(double theta, Point2D.Double inputPoint){
        double x = inputPoint.x * Math.cos(theta) - inputPoint.y * Math.sin(theta);
        double y = inputPoint.x * Math.sin(theta) + inputPoint.y * Math.cos(theta);
        return new Point2D.Double(x,y);
    }
    public void levelBeaten(){
        long time = System.currentTimeMillis() - startTime;
        System.out.println(time);
        System.out.println((1_100_000_000 / time) * 10);
        score += (1_000_000_000 / time) * 10;

        double mod = (heartCount *.6) + (shieldHitCout*.2);
        double ds = (System.currentTimeMillis() - startTime) / 2000;
        score += 10000 * (mod/(ds + missedCount*.25 + 1));
        if(player.hp > 0){
            score -= ((5 / player.hp) - 1) * 500;
            if(score > GameFrame.highScore){
                GameFrame.highScore = score;
            }
        } else {
            score = 0;
        }
        beaten = true;
        beatTime = System.currentTimeMillis();
    }




}
