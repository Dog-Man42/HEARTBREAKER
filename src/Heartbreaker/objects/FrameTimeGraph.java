package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import java.awt.geom.*;
import java.awt.*;

public class FrameTimeGraph extends GameObject {
    private double[] histogram;
    private double maxTime = 0;

    public FrameTimeGraph(){
        scale = 2;
        vertices = new Point2D.Double[240];
        for(int i = 0; i < vertices.length; i ++){
            vertices[i] = new Point2D.Double(i,0);
        }
        transformedVertices = copyVertices(vertices);
        histogram = new double[vertices.length];
        xPosition = 0;
        yPosition = GameFrame.GAME_HEIGHT - 22;
    }

    public void addFrame(double frametime){
        if (frametime > maxTime){
            maxTime = frametime;
        }
        for (int i = vertices.length - 1; i >= 0; i--) {
            if (i == 0) {
                histogram[0] = frametime * -25;
            } else {
                histogram[i] = histogram[i - 1];
            }
            transformedVertices[i].setLocation(i, histogram[i]);
        }
    }

    @Override
    public void update() {}

    public void draw(Graphics2D g ){
        Point2D.Double[] line = realizePoints();


        for(int i = 0; i < vertices.length; i++){
            g.setStroke(new BasicStroke(1));
            g.setFont(new Font(Font.MONOSPACED,Font.ROMAN_BASELINE,20));
            g.drawString("Max Frametime Milliseconds " + maxTime,0,Math.round(yPosition - 10));

            g.drawLine((int) Math.round(line[i].x),(int) Math.round(yPosition),(int) Math.round(line[i].x),(int) Math.round(line[i].y));
        }
        for(int i = 1; i < 11; i++){
            g.drawLine((int) line[vertices.length-1].x - 20, (int) Math.round(yPosition) -50 * i,(int) line[vertices.length-1].x + 10,(int) Math.round(yPosition) - 50 * i);
            g.setFont(new Font(Font.MONOSPACED,Font.ROMAN_BASELINE,15));
            g.drawString(i + "ms",(int) line[vertices.length-1].x + 10,(int) Math.round(yPosition) -50 * i);
        }



    }
}
