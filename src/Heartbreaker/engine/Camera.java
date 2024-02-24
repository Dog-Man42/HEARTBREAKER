package Heartbreaker.engine;

import java.awt.event.KeyEvent;

public class Camera {
    double xPosition = 0;
    double yPosition = 0;
    double zoom = 1;
    public Camera(double x, double y, double zoom){
        this.xPosition = x + GameFrame.WIDTH/2;
        this.yPosition = y + GameFrame.HEIGHT/2;
        this.zoom = zoom;

    }

    public void update(double delta){
        if(Keyboard.isKeyPressed(KeyEvent.VK_LEFT)){
            xPosition -= 250 * delta;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_UP)){
            yPosition -= 250 * delta;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)){
            xPosition += 250 * delta;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_DOWN)){
            yPosition += 250 * delta;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_MINUS)){
            zoom -= .01;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_EQUALS)){
            zoom += .01;
        }


    }

    public double getxPosition() {
        return xPosition;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

}
