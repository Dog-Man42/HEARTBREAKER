
package Heartbreaker.engine;

import java.awt.*;
import javax.swing.*;

public class GameWindow extends JFrame {
    public static GameFrame panel;
    public GameWindow(String title) {
        panel = new GameFrame();
        this.add(panel) ;
        this.setTitle(title);
        this.setResizable(true);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();

        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

}
