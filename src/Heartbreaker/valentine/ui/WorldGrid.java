package Heartbreaker.valentine.ui;

import Heartbreaker.valentine.GameFrame;
import Heartbreaker.valentine.scenes.Scene;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Draws an infinite grid that is fixed in world space. Line spacing snaps between 1, 2 and 5 times a power of ten
 * so the grid stays readable at any zoom, and every line sits on an exact multiple of that spacing.
 *
 * @author tuckt
 */
public class WorldGrid {

    /** World pixels per grid unit. 1 means the grid counts world pixels. */
    public double unitScale = 1;

    /** If true, positive y is drawn upwards and labels count up the screen */
    public boolean yUp = false;

    /** Draw numbers along the axes */
    public boolean labels = true;

    /** Approximate screen distance between major lines in pixels */
    public double targetSpacing = 80;

    public Color majorColor = new Color(255, 255, 255, 32);
    public Color minorColor = new Color(255, 255, 255, 12);
    public Color xAxisColor = new Color(255, 90, 90, 150);
    public Color yAxisColor = new Color(90, 230, 120, 150);
    public Color labelColor = new Color(140, 145, 160);
    public Font labelFont = Fonts.mono(Font.PLAIN, 12);

    public WorldGrid(){}

    public WorldGrid(double unitScale, boolean yUp){
        this.unitScale = unitScale;
        this.yUp = yUp;
    }

    /** Grid units to screen pixels */
    public Point2D.Double unitsToScreen(Scene scene, double x, double y){
        return scene.worldToScreen(x * unitScale, (yUp ? -y : y) * unitScale);
    }

    /** Screen pixels to grid units */
    public Point2D.Double screenToUnits(Scene scene, double x, double y){
        Point2D.Double w = scene.screenToWorld(x, y);
        return new Point2D.Double(w.x / unitScale, (yUp ? -w.y : w.y) / unitScale);
    }

    /** @return the spacing of major lines in grid units at the scene's current zoom */
    public double majorStep(Scene scene){
        double pxPerUnit = unitScale * (scene.getCamera() == null ? 1 : scene.getCamera().getZoom());
        return niceStep(targetSpacing / pxPerUnit);
    }

    public void draw(Graphics2D g, Scene scene){
        int w = GameFrame.GAME_WIDTH;
        int h = GameFrame.GAME_HEIGHT;
        Stroke oldStroke = g.getStroke();
        Color oldColor = g.getColor();

        double step = majorStep(scene);
        double minor = step / 5;
        double pxPerUnit = unitScale * (scene.getCamera() == null ? 1 : scene.getCamera().getZoom());

        //Visible range in grid units, sorted so min <= max whichever way y points
        Point2D.Double a = screenToUnits(scene, 0, 0);
        Point2D.Double b = screenToUnits(scene, w, h);
        double minX = Math.min(a.x, b.x), maxX = Math.max(a.x, b.x);
        double minY = Math.min(a.y, b.y), maxY = Math.max(a.y, b.y);

        g.setStroke(new BasicStroke(1));
        if(minor * pxPerUnit >= 10){
            g.setColor(minorColor);
            drawLines(g, scene, minor, minX, maxX, minY, maxY, w, h);
        }
        g.setColor(majorColor);
        drawLines(g, scene, step, minX, maxX, minY, maxY, w, h);

        Point2D.Double zero = unitsToScreen(scene, 0, 0);
        g.setStroke(new BasicStroke(1.5f));
        g.setColor(xAxisColor);
        g.draw(new Line2D.Double(0, zero.y, w, zero.y));
        g.setColor(yAxisColor);
        g.draw(new Line2D.Double(zero.x, 0, zero.x, h));

        if(labels){
            //Clamped to the screen edge so they stay visible when the axis scrolls away
            double labelY = Math.max(16, Math.min(h - 6, zero.y + 16));
            double labelX = Math.max(6, Math.min(w - 50, zero.x + 6));
            String format = "%." + Math.max(0, (int) -Math.floor(Math.log10(step))) + "f";
            g.setFont(labelFont);
            g.setColor(labelColor);
            for(long i = (long) Math.ceil(minX / step); i * step <= maxX; i++){
                if(i == 0) continue;
                double sx = unitsToScreen(scene, i * step, 0).x;
                g.drawString(String.format(format, i * step), (float) sx + 3, (float) labelY);
            }
            for(long i = (long) Math.ceil(minY / step); i * step <= maxY; i++){
                if(i == 0) continue;
                double sy = unitsToScreen(scene, 0, i * step).y;
                g.drawString(String.format(format, i * step), (float) labelX, (float) sy - 3);
            }
        }

        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    private void drawLines(Graphics2D g, Scene scene, double step, double minX, double maxX, double minY, double maxY, int w, int h){
        for(long i = (long) Math.ceil(minX / step); i * step <= maxX; i++){
            double sx = unitsToScreen(scene, i * step, 0).x;
            g.draw(new Line2D.Double(sx, 0, sx, h));
        }
        for(long i = (long) Math.ceil(minY / step); i * step <= maxY; i++){
            double sy = unitsToScreen(scene, 0, i * step).y;
            g.draw(new Line2D.Double(0, sy, w, sy));
        }
    }

    /** Rounds up to 1, 2 or 5 times a power of ten */
    public static double niceStep(double raw){
        double exp = Math.pow(10, Math.floor(Math.log10(raw)));
        double f = raw / exp;
        if(f <= 1) return exp;
        if(f <= 2) return 2 * exp;
        if(f <= 5) return 5 * exp;
        return 10 * exp;
    }
}
