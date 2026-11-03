package Heartbreaker.valentine.ui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Lines of text drawn on a rounded translucent backing, for debug readouts and HUDs.
 * Build it up with {@link #add}, then {@link #draw} it every frame.
 *
 * @author tuckt
 */
public class TextPanel {

    public enum Anchor { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, TOP_CENTER }

    public record Line(String text, Color color, boolean bold) {}

    public static Font font = Fonts.mono(Font.PLAIN, 14);
    public static Font boldFont = Fonts.mono(Font.BOLD, 14);
    public static Color background = new Color(0, 0, 0, 175);
    public static Color defaultColor = new Color(225, 228, 235);
    public static int padding = 10;

    private final List<Line> lines = new ArrayList<>();

    public TextPanel add(String text){
        return add(text, defaultColor, false);
    }

    public TextPanel add(String text, Color color){
        return add(text, color, false);
    }

    public TextPanel add(String text, Color color, boolean bold){
        lines.add(new Line(text, color, bold));
        return this;
    }

    public boolean isEmpty(){
        return lines.isEmpty();
    }

    /**
     * Draws the panel.
     * @param x x position of the anchor corner in screen pixels
     * @param y y position of the anchor corner in screen pixels
     * @param anchor which corner of the panel (x, y) refers to
     */
    public void draw(Graphics2D g, int x, int y, Anchor anchor){
        if(lines.isEmpty()){
            return;
        }
        FontMetrics fm = g.getFontMetrics(font);
        FontMetrics bold = g.getFontMetrics(boldFont);
        int lineHeight = fm.getHeight();
        int width = 0;
        for(Line line : lines){
            width = Math.max(width, (line.bold() ? bold : fm).stringWidth(line.text()));
        }
        int panelW = width + padding * 2;
        int panelH = lines.size() * lineHeight + padding * 2;
        switch(anchor){
            case TOP_RIGHT -> x -= panelW;
            case BOTTOM_LEFT -> y -= panelH;
            case BOTTOM_RIGHT -> { x -= panelW; y -= panelH; }
            case TOP_CENTER -> x -= panelW / 2;
            default -> {}
        }

        Color oldColor = g.getColor();
        Font oldFont = g.getFont();
        g.setColor(background);
        g.fill(new RoundRectangle2D.Double(x, y, panelW, panelH, 12, 12));

        int ty = y + padding + fm.getAscent();
        for(Line line : lines){
            g.setFont(line.bold() ? boldFont : font);
            g.setColor(line.color());
            g.drawString(line.text(), x + padding, ty);
            ty += lineHeight;
        }
        g.setColor(oldColor);
        g.setFont(oldFont);
    }

    /**
     * Small single line label with a backing, for tagging points in the world.
     */
    public static void label(Graphics2D g, String text, double x, double y, Color color, Font labelFont){
        Font oldFont = g.getFont();
        g.setFont(labelFont);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(background);
        g.fill(new RoundRectangle2D.Double(x - 4, y - fm.getAscent() - 2, fm.stringWidth(text) + 8, fm.getHeight() + 2, 6, 6));
        g.setColor(color);
        g.drawString(text, (float) x, (float) y);
        g.setFont(oldFont);
    }
}
