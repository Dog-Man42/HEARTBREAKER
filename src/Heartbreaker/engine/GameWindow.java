
package Heartbreaker.engine;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import net.arikia.dev.drpc.*;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
//https://github.com/Vatuu/discord-rpc

public class GameWindow extends JFrame{
    public static GameFrame panel;

    public static String appID;
    private static boolean id_present;
    public static DiscordRichPresence presence;
    private static boolean playing = false;


    public GameWindow(String title, String ID) {
        if(ID != null){
            DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
                System.out.println("Welcome " + user.username + "#" + user.discriminator + "!");
            }).build();
            DiscordRPC.discordInitialize(ID, handlers, true);

            presence = new DiscordRichPresence.Builder("Main Menu").setDetails("Testing Bullet Hell").build();
            presence.largeImageKey="cover1";
            DiscordRPC.discordUpdatePresence(presence);
        }




        panel = new GameFrame();
        if(hasID()) {
            DiscordRPC.discordRunCallbacks();
        }

        this.add(panel) ;
        this.setTitle(title);
        this.setResizable(true);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if(hasID()) {
            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    DiscordRPC.discordShutdown();
                }
            });
        }
        this.setLocationRelativeTo(null);
        this.pack();

        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public static boolean hasID(){
        return id_present;
    }

    public void start(){
        while(playing){

        }
    }
}
