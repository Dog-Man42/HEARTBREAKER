package Heartbreaker.engine;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

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
            xPosition -= (1/zoom) * 250 * delta;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_UP)){
            yPosition -= (1/zoom) * 250 * delta;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)){
            xPosition += (1/zoom) * 250 * delta;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_DOWN)){
            yPosition += (1/zoom) * 250 * delta;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_MINUS)){
            if(zoom > .04) {
                zoom -= .01;
            }
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_EQUALS)){
            zoom += .01;
        }

        double zoomAmount = MouseInput.getScrollAmount() / 25.0;
        if(zoomAmount > 0){
            if (zoom > 0.04){
                zoom -= zoomAmount * zoom;
            }
        } else {
            zoom -= zoomAmount * zoom;
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

    public Point2D.Double getPosition(){
        return new Point2D.Double(xPosition,yPosition);
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public void setPosition(Point2D.Double position){
        this.xPosition = position.x;
        this.yPosition = position.y;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

}
