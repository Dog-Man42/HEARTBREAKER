
package Heartbreaker.engine;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import net.arikia.dev.drpc.*;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
//https://github.com/Vatuu/discord-rpc

public class GameWindow extends JFrame {
    public static GameFrame panel;
    public static DiscordRichPresence presence;

    public GameWindow(String title) {

        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            System.out.println("Welcome " + user.username + "#" + user.discriminator + "!");
        }).build();
        DiscordRPC.discordInitialize("1174564288418033755", handlers, true);

        presence = new DiscordRichPresence.Builder("whoops").setDetails("Testing Bullet Hell").build();
        presence.state="yay";
        presence.largeImageKey="cover";
        DiscordRPC.discordUpdatePresence(presence);
        DiscordRPC.discordRunCallbacks();

        panel = new GameFrame();



        this.add(panel) ;
        this.setTitle(title);
        this.setResizable(true);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                DiscordRPC.discordShutdown();
            }
        });
        this.setLocationRelativeTo(null);
        this.pack();

        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

}
