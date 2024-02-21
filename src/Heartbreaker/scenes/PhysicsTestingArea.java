package Heartbreaker.scenes;

import Heartbreaker.engine.Camera;
import Heartbreaker.engine.GameFrame;
import Heartbreaker.objects.Heart;
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
        return true;
    }

    @Override
    public void updateScene() {
        updateObjects();
        System.out.println(getObjects().size());
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
        g.setStroke(new BasicStroke(1));

        int xOrigin = (int) -camera.getxPosition();
        int yOrigin = (int) -camera.getyPosition();

        int xOffset = (int) ((camera.getxPosition() * camera.getZoom()) % cellSize);
        int yOffset = (int) ((camera.getyPosition() * camera.getZoom()) % cellSize);

        for(int x = -xOffset; x < WIDTH; x += cellSize){
            if(x == xOrigin){
                g.setColor(Color.GREEN); // Draw the x-axis as green
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawLine(x,0,x,HEIGHT);
        }

// Draw the horizontal grid lines
        for(int y = -yOffset; y < HEIGHT; y += cellSize){
            if(y == yOrigin){
                g.setColor(Color.RED); // Draw the y-axis as red
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
