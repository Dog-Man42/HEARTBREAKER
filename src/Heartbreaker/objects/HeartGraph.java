package Heartbreaker.objects;

import Heartbreaker.engine.GameFrame;
import Heartbreaker.engine.GameObject;

import java.awt.geom.*;
import java.awt.*;

public class HeartGraph extends GameObject {
    private Heart heart;
    private double bpm = 60;
    private double frames = 0;
    private int damage = 0;
    private double[] histogram;
    private boolean flatlined = false;

    HeartGraph(Heart parentHeart){
        setScale(1);
        heart = parentHeart;
        Point2D.Double[] temp = new Point2D.Double[200];

        for(int i = 0; i < temp.length; i ++){
            temp[i] = new Point2D.Double(i,0);
        }
        setVertices(temp);
        histogram = new double[temp.length];
        setXPosition(GameFrame.GAME_WIDTH - 205);
        setYPosition(22);

        //Fill out histogram first
        for(int i = temp.length-1; i >= 0; i--){
            double beat_duration = 60 / bpm;
            double totalTime = (frames-i/2.0) / 60;
            double rate = totalTime % beat_duration;
            double time = rate / beat_duration * (2 * Math.PI);
            //transformedVertices[i].setLocation(i, -10 * (Math.abs(Math.sin(time)) * Math.abs((Math.cos(time+.4) - Math.tan(time/2) - .4))) + 3);

            histogram[i] = (-10 * Heart.heartFunction(time)) + 12;
            //transformedVertices[i].setLocation(i/2, -10 * (Math.abs(-Math.sin(time)) * (Math.abs((-Math.cos(time - 1.5) +Math.tan(time/2) - 4.5)) - 5.4)) + 0);
            temp[i].setLocation(i,histogram[i]);
        }
        setTransformedVertices(temp);
    }


    public void update(){
        frames+= GameFrame.delta;
        Point2D.Double[] temp = getTransformedVertices();
        for(int i = temp.length-1; i >= 0; i--){
            double fps = 1.0 / GameFrame.delta;
            double beat_duration = 60.0 / bpm;
            double totalTime = frames;
            double rate = totalTime % beat_duration;
            double time = rate / beat_duration * (2 * Math.PI);
            //time = frames + GameFrame.delta;
            //transformedVertices[i].setLocation(i, -10 * (Math.abs(Math.sin(time)) * Math.abs((Math.cos(time+.4) - Math.tan(time/2) - .4))) + 3);
            if(i == 0){
                if(!(bpm < 10)) {
                    //histogram[0] = -10 * (Math.abs(-Math.sin(time)) * (Math.abs((-Math.cos(time - 1.5) + Math.tan(time / 2) - 4.5)) - 5.4)) + 0;
                    histogram[0] = (-10 * Heart.heartFunction(time)) + 12;
                } else {
                    histogram[0] = 5.4;
                }
            } else {
                histogram[i] = histogram[i-1];
            }
            //transformedVertices[i].setLocation(i/2, -10 * (Math.abs(-Math.sin(time)) * (Math.abs((-Math.cos(time - 1.5) +Math.tan(time/2) - 4.5)) - 5.4)) + 0);
            temp[i].setLocation(i,histogram[i]);
        }

    }
    public void setBPM(double newBPM){
        bpm = newBPM;
        if(newBPM % 1.0 == 0){
            damage++;
        }
    }
    public void draw(Graphics2D g){
        if(flatlined){
            g.setColor(Color.getHSBColor((0f)/255,1,.1f));
        } else {
            g.setColor(Color.getHSBColor((120f-damage)/255,1,.1f));
        }

        g.fillRect((int) getXPosition()-150,(int) getYPosition()-22,355,45);
        g.setStroke(new BasicStroke(1));
        if(flatlined){
            g.setColor(Color.getHSBColor((0f)/255,1,.9f));
        } else {
            g.setColor(Color.getHSBColor((120f-damage)/255,1,.9f));
        }

        Point2D.Double[] line = realizePoints();
        Point2D.Double[] temp = getTransformedVertices();
        int[] lineX = new int[temp.length];
        int[] lineY = new int[temp.length];
        for(int i = 0; i < temp.length; i++){
            lineX[i] = (int) Math.round(line[i].x);
            lineY[i] = (int) Math.round(line[i].y);
        }
        g.drawPolyline(lineX,lineY,temp.length);

        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setFont(new Font("SANS_SERIF",Font.PLAIN,27));
        if(flatlined){
            g.drawString("FLATLINE", (int) getXPosition() - 147, (int) getYPosition() + 12);
        } else {
            g.drawString("BPM:" + bpm, (int) getXPosition() - 147, (int) getYPosition() + 12);
        }
        g.setStroke(new BasicStroke(3));
    }
    public void setFlatlined(boolean flat){
        this.flatlined = flat;
    }



}
