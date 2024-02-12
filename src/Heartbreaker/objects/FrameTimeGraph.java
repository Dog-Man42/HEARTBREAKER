package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.GameObject;

import java.awt.geom.*;
import java.awt.*;

public class FrameTimeGraph extends GameObject {
    private double[] histogram;
    private double maxTime = 0;

    public FrameTimeGraph(){
        setScale(2);
        Point2D.Double[] temp = new Point2D.Double[240];
        for(int i = 0; i < temp.length; i ++){
            temp[i] = new Point2D.Double(i,0);
        }
        histogram = new double[temp.length];
        setVertices(temp);
        setXPosition(0);
        setYPosition(GameFrame.GAME_HEIGHT - 22);
    }

    public void addFrame(double frametime){
        if (frametime > maxTime){
            maxTime = frametime;
        }
        Point2D.Double[] temp = getTransformedVertices();
        for (int i = temp.length - 1; i >= 0; i--) {
            if (i == 0) {
                histogram[0] = frametime * -5;
            } else {
                histogram[i] = histogram[i - 1];
            }
            temp[i].setLocation(i, histogram[i]);
        }
    }

    @Override
    public void update() {}

    public void draw(Graphics2D g ){
        Point2D.Double[] line = realizePoints();
        Point2D.Double[] temp = getVerticies();

        for(int i = 0; i < temp.length; i++){
            g.setStroke(new BasicStroke(1));
            g.setFont(new Font(Font.MONOSPACED,Font.ROMAN_BASELINE,20));
            g.drawString("Max Frametime Milliseconds " + maxTime,0,Math.round(getYPosition() - 10));

            g.drawLine((int) Math.round(line[i].x),(int) Math.round(getYPosition()),(int) Math.round(line[i].x),(int) Math.round(line[i].y));
        }
        for(int i = 1; i < 31; i++){
            g.drawLine((int) line[temp.length-1].x - 20, (int) Math.round(getYPosition()) -10 * i,(int) line[temp.length-1].x + 10,(int) Math.round(getYPosition()) - 10 * i);
            g.setFont(new Font(Font.MONOSPACED,Font.ROMAN_BASELINE,15));
            g.drawString(i + "ms",(int) line[temp.length-1].x + 10,(int) Math.round(getYPosition()) -10 * i);
        }



    }
}
