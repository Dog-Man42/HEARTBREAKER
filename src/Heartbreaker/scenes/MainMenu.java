package Heartbreaker.scenes;

import Heartbreaker.engine.*;
import Heartbreaker.engine.particles.*;
import Heartbreaker.engine.vectors.Vector3;
import Heartbreaker.engine.vectors.Vector3Math;
import Heartbreaker.main.Heartbreaker;
import Heartbreaker.objects.*;



import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MainMenu extends Level {



    public MainMenu(){
        super(false);
    }

    public boolean initialize() {
        origin = new Point(GameFrame.GAME_WIDTH/2,GameFrame.GAME_HEIGHT/2);
        WIDTH = GameFrame.GAME_WIDTH;
        HEIGHT = GameFrame.GAME_HEIGHT;
        player = new Player(origin.x,300);
        addObject(player);
        heart = new Heart(origin.x,origin.y);
        addObject(heart);
        shield = null;

        bullets.clear();
        bulletDeleteQueue.clear();
        return true;

    }

    public void updateScene(){
        updateObjects();
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
        //heart.draw(g);
        //player.draw(g);
        drawObjects(g);






    }
}
