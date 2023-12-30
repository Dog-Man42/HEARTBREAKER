
package Heartbreaker.engine;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;


//https://github.com/Vatuu/discord-rpc

public class GameWindow extends JFrame{
    public static GameFrame panel;

    private static boolean playing = false;


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


    public void start(){
        while(playing){

        }
    }
}
