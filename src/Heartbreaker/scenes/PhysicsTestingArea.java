package Heartbreaker.scenes;

import Heartbreaker.engine.Camera;
import Heartbreaker.engine.GameFrame;
import Heartbreaker.objects.Box;
import Heartbreaker.objects.Heart;
import Heartbreaker.objects.MinorEye;
import Heartbreaker.objects.Player;

import java.awt.*;

public class PhysicsTestingArea extends Level{
    public PhysicsTestingArea() {}

    @Override
    public boolean initialize() {
        origin = new Point(GameFrame.GAME_WIDTH/2,GameFrame.GAME_HEIGHT/2);
        camera = new Camera(0,0,1);
        addObject(new Player(0,0));
        addObject(new Heart(0,0));
        addObject(new Box(0,0,5));
        addObject(new MinorEye(0,0));
        return true;
    }

    @Override
    public void updateScene() {
        updateObjects();
    }

    @Override
    public void draw(Graphics2D g) {
        drawGrid(g);
        drawObjects(g);
    }
    public void drawGrid(Graphics2D g){
        int cellSize = (int) Math.round(50 * camera.getZoom());
        BasicStroke temp = (BasicStroke) g.getStroke();
        Color col = g.getColor();
        g.setStroke(new BasicStroke(2));

        int mScreenX = WIDTH/2;
        int mScreenY = HEIGHT/2;

        int xOffset = (int) (camera.getxPosition() * camera.getZoom());
        int yOffset = (int) (camera.getyPosition() * camera.getZoom());

        int xAxis = WIDTH/2 - xOffset;
        int yAxis = HEIGHT/2 - yOffset;

        int xMod = xOffset % cellSize;
        int yMod = yOffset % cellSize;

        //To the right
        for(int x = mScreenX - xMod; x < WIDTH; x += cellSize){
            if(x == xAxis){
                g.setColor(Color.GREEN); // Draw the x-axis as green
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawLine(x,0,x,HEIGHT);
        }
        //to the left
        for(int x = mScreenX - xMod - cellSize; x >= 0; x -= cellSize){
            if(x == xAxis){
                g.setColor(Color.GREEN); // Draw the x-axis as green
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawLine(x,0,x,HEIGHT);
        }

// Draw the horizontal grid lines
        for(int y = mScreenY - yMod; y < HEIGHT; y += cellSize){
            if(y == yAxis){
                g.setColor(Color.RED); // Draw the x-axis as green
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawLine(0,y,WIDTH,y);
        }
        for(int y = mScreenY - yMod - cellSize; y >= 0; y -= cellSize){
            if(y == yAxis){
                g.setColor(Color.RED); // Draw the x-axis as green
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawLine(0,y,WIDTH,y);
        }

        g.setStroke(temp);
        g.setColor(col);
    }

    @Override
    public void levelBeaten() {

    }
}
