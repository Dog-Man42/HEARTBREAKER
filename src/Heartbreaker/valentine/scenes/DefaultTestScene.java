package Heartbreaker.valentine.scenes;

import Heartbreaker.valentine.Camera;
import Heartbreaker.valentine.GameFrame;
import Heartbreaker.valentine.collision.CollisionManager;
import Heartbreaker.valentine.input.KeyInput;
import Heartbreaker.valentine.input.MouseInput;
import Heartbreaker.valentine.ui.WorldGrid;
import Heartbreaker.valentine.vectors.Vector2;
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

    /** Background grid, change its settings to show other units */
    protected WorldGrid grid = new WorldGrid();

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

    /**
     * Draws a grid fixed in world space, see {@link WorldGrid}.
     */
    public void drawInfinitePlane(Graphics2D g){
        grid.draw(g, this);
    }
}
