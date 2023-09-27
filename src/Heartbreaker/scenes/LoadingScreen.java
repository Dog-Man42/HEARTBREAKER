package Heartbreaker.scenes;

import Heartbreaker.engine.GameFrame;

import javax.swing.*;
import java.awt.*;

import java.awt.Graphics2D;

public class LoadingScreen extends JComponent {

    int frame = 0;


    public void draw(Graphics2D g) {
        //this.setBackground(Color.black);
        g.setColor(Color.WHITE);

        if (frame > 10) {
            frame = 0;
        }
        String ellipses = "";
        for (int i = 0; i < frame; i++) {
            ellipses = ellipses + ".";
        }



        String loadingText = "LOADING" + ellipses;
        Font font = new Font("Dialog", Font.PLAIN, 50);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth("LOADING..........");
        int textHeight = fontMetrics.getHeight();

        int centerX = (GameFrame.GAME_WIDTH - textWidth) / 2;
        int centerY = (GameFrame.GAME_HEIGHT - textHeight) / 2;
        g.setFont(font);
        g.drawString("LOADING" + ellipses, centerX, centerY);
        frame++;
    }



}
