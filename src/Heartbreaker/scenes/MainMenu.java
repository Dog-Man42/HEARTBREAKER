package Heartbreaker.scenes;

import Heartbreaker.engine.*;
import Heartbreaker.engine.particles.*;
import Heartbreaker.engine.vectors.Vector3;
import Heartbreaker.engine.vectors.Vector3Math;
import Heartbreaker.main.Heartbreaker;
import Heartbreaker.objects.*;
import Heartbreaker.scenes.attributes.UsesBullets;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MainMenu extends Level implements UsesBullets {

    private ParticleEmitter emitter;
    private Tentacle tentacle;
    private Tentacle tentacle2;
    private Tentacle tentacle3;
    private Tentacle tentacle4;


    public boolean initialize() {
        origin = new Point(GameFrame.GAME_WIDTH/2,GameFrame.GAME_HEIGHT/2);
        WIDTH = GameFrame.GAME_WIDTH;
        HEIGHT = GameFrame.GAME_HEIGHT;
        player = new Player(origin.x,300);
        heart = new Heart(origin.x,origin.y);
        shield = null;
        emitter = new ParticleEmitter(TestParticle.class,500,500,1000);
        tentacle = new Tentacle(10,10,origin.x,origin.y,15);
        tentacle2 = new Tentacle(5,5,origin.x,origin.y,15);
        tentacle3 = new Tentacle(5,5,origin.x,origin.y,15);
        tentacle4 = new Tentacle(5,5,origin.x,origin.y,15);
        tentacle2.setRotation(90);
        tentacle3.setRotation(180);
        tentacle4.setRotation(270);

        System.out.println(Runtime.getRuntime().availableProcessors());
        bullets.clear();
        bulletDeleteQueue.clear();
        objects.add(new MinorEye(250,500));
        return true;

    }

    public void updateScene(){
        tentacle.update();
        tentacle2.update();
        tentacle3.update();
        tentacle4.update();
        player.update();
        heart.update();
        updateBullets();
        deleteBullets();
        updateObjects();
    }

    public void damageHeart(int dmg){
        GameFrame.setCurrentScene(new Level1());
    }

    @Override
    public void levelBeaten() {}

    public void mouseMoved(MouseEvent e) {
        if(player != null)
            player.mouseMoved(e);
    }

    @Override
    public void draw(Graphics2D g) {

        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //g.drawImage(drawCircle(g),0,0,null);
        //drawCircle(g);
        g.setColor(Color.RED);
        //g.drawString("Frame Rate: " + GameFrame.FPS,0,20);
        //g.drawString("Frame Time MS: " + String.format("%.2f", GameFrame.avgFrameTime),0,45);
        g.setFont(new Font(Font.DIALOG,Font.ROMAN_BASELINE,50));
        g.drawString("HEARTBREAKER",10, 50);

        g.setFont(new Font("Dialog",Font.PLAIN,15));
        g.setColor(Color.gray);
        g.drawString("A and D to rotate around center, W and S to move in and out.", 10, 75);
        g.drawString("Using the space bar, you can precisely move up Left, Right, Up, and Down", 10, 100);
        g.drawString("Left click for Standard Bullet, right click for Gravity Bullet", 10, 120);
        g.setFont(new Font("Dialog",Font.ITALIC,15));
        g.drawString("Avoid bullets and YOU MAY FIRE WHEN READY.", 10, 155);
        g.setFont(new Font("Dialog",Font.PLAIN,40));
        g.drawString("High Score: " + GameFrame.highScore, 10, 195);
        g.setColor(Color.WHITE);
        Font font = new Font(Font.SERIF,Font.PLAIN,20);

        FontMetrics fontMetrics = g.getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth(Heartbreaker.version);
        int textHeight = fontMetrics.getHeight();
        g.setFont(font);
        g.drawString(Heartbreaker.version,GameFrame.GAME_WIDTH-textWidth-10,GameFrame.GAME_HEIGHT-textHeight);


        g.setStroke(new BasicStroke(3));
        heart.draw(g);
        player.draw(g);
        tentacle.draw(g);
        drawObjects(g);
/*
        tentacle2.draw(g);
        tentacle3.draw(g);
        tentacle4.draw(g);

 */

        drawBullets(g);




    }
    public BufferedImage drawCircle(Graphics2D g){
        BufferedImage image = GameFrame.bufferedImage;
        int r = 200;
        double diagonal = Math.sqrt(Math.pow(WIDTH,2) + Math.pow(HEIGHT,2));
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int dx = x- origin.x;
                int dy = y - origin.y;
                double distance = Math.sqrt(dx*dx + dy*dy);

                if(distance < r){
                    double dz = Math.sqrt(r * r - dx * dx - dy * dy);
                    Vector3 vec = new Vector3(dx,dy,dz);

                    Vector3 normal = Vector3Math.normalize(vec);
                    double dp = Math.abs(player.getRadialPosition());
                    Vector3 light = new Vector3(player.getXPosition(), player.getYPosition(), r*r / dp);
                    Vector3 direction = Vector3Math.normalize(Vector3.difference(light,new Vector3(x,y,dz)));
                    double shade =  Vector3Math.dot(normal,direction);
                    if(shade >= 0.01) {
                        // Calculate light falloff
                        double lightDistance = Vector3Math.length(Vector3.difference(light, new Vector3(x, y, dz)));
                        double falloff = Math.max(1.0 - lightDistance / 5000, 0);
                        //shade *= falloff;
                        //normal shading
                        int red = (int) Math.round(255 * (normal.x * 0.5 + 0.5) * shade);
                        int green = (int) Math.round(255 * (normal.y * 0.5 + 0.5) * shade);
                        int blue = (int) Math.round(255 * (normal.z * 0.5 + 0.5) * shade);
                        //gray = (int) Math.round(255 * (0.5 * 0.5 + 0.5) * shade);
                        red = Math.min(Math.max(red, 0), 255);
                        green = Math.min(Math.max(green, 0), 255);
                        blue = Math.min(Math.max(blue, 0), 255);

                        int rgb = (255 << 24) | (red << 16) | (green << 8) | blue;
                        image.setRGB(x, y, rgb);
                    } else {
                        int rgb = (255 << 24) | (0) | 0;
                        image.setRGB(x, y, rgb);
                    }
                } else {
                    image.setRGB(x,y,0);
                }
            }
        }
        return image;
    }
}
