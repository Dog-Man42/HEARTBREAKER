package Heartbreaker.engine.scenes;

import Heartbreaker.engine.Camera;
import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.collision.CollisionManager;
import Heartbreaker.engine.input.KeyInput;
import Heartbreaker.engine.input.MouseInput;
import Heartbreaker.engine.vectors.Vector2;
import Heartbreaker.scenes.MainMenu;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class DefaultTestScene extends Scene{


    private int exitCoolDown = 120;
    private boolean dragging = false;
    private Point dragStartMouse = new Point(0,0);
    private Point2D.Double dragStartCam = new Point2D.Double(0,0);

    @Override
    public boolean initialize() {
        collisionManager = new CollisionManager();
        origin = new Point(GameFrame.GAME_WIDTH/2,GameFrame.GAME_HEIGHT/2);
        camera = new Camera(0,0,.75);
        return true;
    }

    @Override
    public void updateScene(double delta) {

        if(KeyInput.isKeyPressed(KeyEvent.VK_ESCAPE) && exitCoolDown <= 0){
            GameFrame.setCurrentScene(new MainMenu());
        }
        if(exitCoolDown > 0){
            exitCoolDown--;
        }

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

        updateObjects(delta);
    }

    @Override
    public void draw(Graphics2D g, double delta) {
        g.setColor(new Color(0,0,50));
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(Color.WHITE);
        drawInfinitePlane(g);
        drawObjects(g,delta);
        camera.update(delta);

    }

    public void drawInfinitePlane(Graphics2D g){
        double zoom = camera.getZoom();
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

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(100,100,100));
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

        g.setStroke(new BasicStroke(2 ));
        g.setColor(Color.GREEN);
        g.drawLine(yAxis,GameFrame.GAME_HEIGHT,yAxis,0);
        g.setColor(Color.RED);
        g.drawLine(GameFrame.GAME_WIDTH,xAxis,0,xAxis);

        g.setStroke(temp);
        g.setColor(col);
    }
}
