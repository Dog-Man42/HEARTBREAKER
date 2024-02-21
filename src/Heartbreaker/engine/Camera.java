package Heartbreaker.engine;

import java.awt.event.KeyEvent;

public class Camera {
    double xPosition = 0;
    double yPosition = 0;
    double zoom = 1;
    public Camera(double x, double y, double zoom, double rotation){
        this.xPosition = x;
        this.yPosition = y;
        this.zoom = zoom;

    }

    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT -> xPosition -= 250 * GameFrame.delta;
            case KeyEvent.VK_UP -> yPosition -= 250 * GameFrame.delta;
            case KeyEvent.VK_RIGHT-> xPosition += 250 * GameFrame.delta;
            case KeyEvent.VK_DOWN -> yPosition += 250 * GameFrame.delta;
            case KeyEvent.VK_MINUS -> zoom -= .01;
            case KeyEvent.VK_EQUALS -> zoom += .01;
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
