package Heartbreaker.scenes;

import Heartbreaker.engine.Camera;
import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.MouseInput;
import Heartbreaker.engine.scenes.Scene;
import Heartbreaker.engine.vectors.Vector2;
import Heartbreaker.objects.*;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Random;

import java.awt.*;

public class PhysicsTestingArea extends Scene {

    private boolean dragging = false;
    private Point dragStartMouse = new Point(0,0);
    private Point2D.Double dragStartCam = new Point2D.Double(0,0);
    private int frame = 0;

    public PhysicsTestingArea() {}

    @Override
    public boolean initialize() {
        collisionManager.clear();
        origin = new Point(GameFrame.GAME_WIDTH/2,GameFrame.GAME_HEIGHT/2);
        camera = new Camera(0,0,.75);
        Random random = new Random();
        for(int i = 0; i < 2500; i++){
            int mass = random.nextInt(4,6);
            addObject(new PhysicsBall(random.nextInt(-5000,5001),random.nextInt(-5000,5001),2 * mass,mass,random.nextInt(-100,101),random.nextInt(-100,101)));
        }
        return true;
    }

    @Override
    public void updateScene(double delta) {
        if(dragging){
            if(MouseInput.isButtonPressed(MouseEvent.BUTTON1)){
                Vector2 off = Vector2.difference(new Vector2(dragStartMouse), new Vector2(MouseInput.getPosition()));
                off = Vector2.sum(new Vector2(dragStartCam), Vector2.divide(off,camera.getZoom()));
                camera.setPosition(off.toPoint());
            } else {
                dragging = false;
            }
        } else if(MouseInput.isMouseMoved() && MouseInput.isButtonPressed(MouseEvent.BUTTON1)){
            dragging = true;
            dragStartCam = camera.getPosition();
            dragStartMouse = MouseInput.getPosition();
        }
        int substeps = 4;
        for(int i = 0; i < substeps; i++){
            updateObjects(delta/substeps);
        }

    }

    @Override
    public void draw(Graphics2D g, double delta) {
        drawGrid(g);
        drawObjects(g, delta);
    }
    public void drawGrid(Graphics2D g){
        double zoom = (camera.getZoom());
        double s = (zoom * 200) % 50 + 50;

        int cellSize = (int) Math.round(s);
        BasicStroke temp = (BasicStroke) g.getStroke();
        Color col = g.getColor();
        g.setStroke(new BasicStroke(1));

        int mScreenX = GameFrame.GAME_WIDTH/2;
        int mScreenY = GameFrame.GAME_HEIGHT/2;

        int xOffset = (int) (camera.getxPosition() * camera.getZoom());
        int yOffset = (int) (camera.getyPosition() * camera.getZoom());

        int yAxis = mScreenX - xOffset;
        int xAxis = mScreenY - yOffset;

        int xMod = xOffset % cellSize;
        int yMod = yOffset % cellSize;

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.DARK_GRAY);
        //To the right
        for(int x = mScreenX - xMod; x < GameFrame.GAME_WIDTH; x += cellSize){
            g.drawLine(x,0,x,GameFrame.GAME_HEIGHT);
        }
        //to the left
        for(int x = mScreenX - xMod - cellSize; x >= 0; x -= cellSize){
            g.drawLine(x,0,x,GameFrame.GAME_HEIGHT);
        }

// Draw the horizontal grid lines
        for(int y = mScreenY - yMod; y < GameFrame.GAME_HEIGHT; y += cellSize){
            g.drawLine(0,y,GameFrame.GAME_WIDTH,y);
        }
        for(int y = mScreenY - yMod - cellSize; y >= 0; y -= cellSize){
            g.drawLine(0,y,GameFrame.GAME_WIDTH,y);
        }

        g.setStroke(new BasicStroke(4));
        g.setColor(Color.GREEN);
        g.drawLine(yAxis,GameFrame.GAME_HEIGHT,yAxis,0);
        g.setColor(Color.RED);
        g.drawLine(GameFrame.GAME_WIDTH,xAxis,0,xAxis);

        g.setStroke(temp);
        g.setColor(col);
    }

}
