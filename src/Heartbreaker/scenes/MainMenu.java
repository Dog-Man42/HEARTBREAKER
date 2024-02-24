package Heartbreaker.scenes;

import Heartbreaker.engine.*;
import Heartbreaker.main.Heartbreaker;
import Heartbreaker.objects.*;
import Heartbreaker.objects.Box;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class MainMenu extends Level {



    public MainMenu(){}

    public boolean initialize() {
        origin = new Point(GameFrame.GAME_WIDTH/2,GameFrame.GAME_HEIGHT/2);
        WIDTH = GameFrame.GAME_WIDTH;
        HEIGHT = GameFrame.GAME_HEIGHT;
        player = new Player(origin.x,300);
        addObject(player);
        heart = new Heart(origin.x,origin.y);
        addObject(heart);
        shield = null;
        camera = new Camera(origin.x,origin.y,1);
        Box box1 = new Box(0,0,10);
        addObject(box1);




        return true;



    }

    public void updateScene(double delta){
        updateObjects(delta);
        if(Keyboard.isKeyPressed(KeyEvent.VK_SLASH)){
            GameFrame.setCurrentScene(new PhysicsTestingArea());
        }
    }

    @Override
    public void levelBeaten() {}

    public void mouseMoved(MouseEvent e) {
        if(player != null)
            player.mouseMoved(e);
    }

    @Override
    public void draw(Graphics2D g, double delta) {

        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //g.drawImage(drawCircle(g),0,0,null);
        //drawCircle(g);
        radialGrid(g);
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
        //heart.draw(g);
        //player.draw(g);
        drawObjects(g, delta);
        g.drawOval((int) camera.getxPosition() - 5, (int) camera.getyPosition() - 5, 10,10);

    }

    public void radialGrid(Graphics2D g){
        g.setColor(new Color(50,50,50));
        g.setStroke(new BasicStroke(2));
        double resolution = 24;
        double diagonal = Math.sqrt(Math.pow(WIDTH,2) + Math.pow(HEIGHT,2));
        double centerx = origin.x + (origin.x - camera.getxPosition()) * camera.getZoom();
        double centery = origin.y + (origin.y - camera.getyPosition()) * camera.getZoom();
        for(int i = 0; i < resolution; i++){
            double theta = (360.0/resolution) * i;
            double endx = centerx + diagonal * Math.cos(Math.toRadians(theta)) * camera.getZoom();
            double endy = centery + diagonal * Math.sin(Math.toRadians(theta)) * camera.getZoom();
            g.drawLine((int) Math.round(centerx), (int) Math.round(centery), (int) endx, (int) endy);

        }
        for(int i = 0; i < resolution; i++) {
            g.setColor(new Color(150 / (i/2 + 1), 150 / (i/2 + 1), 150 / (i/2 + 1)));
            int size = (int) ((diagonal / resolution) * i * camera.getZoom());
            g.drawOval((int) (centerx - (size / 2)), (int) (centery - size / 2), size, size);
        }

    }
}
