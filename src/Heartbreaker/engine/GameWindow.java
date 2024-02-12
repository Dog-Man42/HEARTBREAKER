
package Heartbreaker.engine;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import Heartbreaker.engine.scenes.Scene;
import Heartbreaker.scenes.Level;


public class GameWindow extends JFrame{
    public static GameFrame panel;

    private static boolean playing = false;


    public GameWindow(String title, Level mainScene) {
        panel = new GameFrame(mainScene);

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

    public static void addComponent(JComponent component){
        panel.add(component);
        panel.uiComponents.add(component);
    }
    public static void clearComponents(){
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.uiComponents.clear();

    }

}
