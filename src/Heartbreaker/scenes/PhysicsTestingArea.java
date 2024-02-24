package Heartbreaker.scenes;

import Heartbreaker.engine.Camera;
import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.collision.CollisionManager;
import Heartbreaker.engine.vectors.Vector2;
import Heartbreaker.objects.*;

import java.util.Random;

import java.awt.*;

public class PhysicsTestingArea extends Level{

    public PhysicsTestingArea() {}

    @Override
    public boolean initialize() {
        collisionManager.clear();
        origin = new Point(GameFrame.GAME_WIDTH/2,GameFrame.GAME_HEIGHT/2);
        camera = new Camera(0,0,.75);
        Random random = new Random();
        for(int i = 0; i < 2000; i++){
            addObject(new PhysicsBall(random.nextInt(-3000,3001),random.nextInt(-3000,3001),10,1,random.nextInt(-200,201),random.nextInt(-200,201)));
        }
        return true;
    }

    @Override
    public void updateScene(double delta) {
        System.out.println("FRAME\n\n\n");
        updateObjects(delta);
    }

    @Override
    public void draw(Graphics2D g, double delta) {
        drawGrid(g);
        drawObjects(g, delta);
        g.drawOval(-10,-10,20,20);
    }
    public void drawGrid(Graphics2D g){
        double zoom = (camera.getZoom());
        int cellSize = (int) Math.round(50 * zoom);
        BasicStroke temp = (BasicStroke) g.getStroke();
        Color col = g.getColor();
        g.setStroke(new BasicStroke(2));

        int mScreenX = GameFrame.GAME_WIDTH/2;
        int mScreenY = GameFrame.GAME_HEIGHT/2;

        int xOffset = (int) (camera.getxPosition() * camera.getZoom());
        int yOffset = (int) (camera.getyPosition() * camera.getZoom());

        int yAxis = mScreenX - xOffset;
        int xAxis = mScreenY - yOffset;

        int xMod = xOffset % cellSize;
        int yMod = yOffset % cellSize;

        //To the right
        for(int x = mScreenX - xMod; x < GameFrame.GAME_WIDTH; x += cellSize){
            if(x == yAxis){
                g.setColor(Color.GREEN); // Draw the x-axis as green
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawLine(x,0,x,GameFrame.GAME_HEIGHT);
        }
        //to the left
        for(int x = mScreenX - xMod - cellSize; x >= 0; x -= cellSize){
            if(x == yAxis){
                g.setColor(Color.GREEN); // Draw the x-axis as green
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawLine(x,0,x,GameFrame.GAME_HEIGHT);
        }

// Draw the horizontal grid lines
        for(int y = mScreenY - yMod; y < GameFrame.GAME_HEIGHT; y += cellSize){
            if(y == xAxis){
                g.setColor(Color.RED); // Draw the x-axis as green
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawLine(0,y,GameFrame.GAME_WIDTH,y);
        }
        for(int y = mScreenY - yMod - cellSize; y >= 0; y -= cellSize){
            if(y == xAxis){
                g.setColor(Color.RED); // Draw the x-axis as green
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawLine(0,y,GameFrame.GAME_WIDTH,y);
        }

        g.setStroke(temp);
        g.setColor(col);
    }

    @Override
    public void levelBeaten() {

    }
}
