package Heartbreaker.engine;

import Heartbreaker.engine.scenes.Scene;
import Heartbreaker.main.Heartbreaker;
import Heartbreaker.objects.FrameTimeGraph;
import Heartbreaker.scenes.*;
import net.arikia.dev.drpc.DiscordRPC;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameFrame extends JPanel implements Runnable{

    public static int GAME_WIDTH = 1000;
    public static int GAME_HEIGHT = 800;
    static Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
    public static double delta = 0;

    public static int FPS = 0;

    public static int targetFPS = 60;

    public static boolean LOADING = false;

    public static Long frameStart = 0L;
    public static Long lastFrame = 0L;
    public static double avgFrameTime = 0;

    public static int mouseX = 0;
    public static int mouseY = 0;

    Thread gameThread;
    public static BufferedImage bufferedImage;
    public static SoundManager soundManager;
    Graphics2D graphics2D;


    //Attempt to fix frame time that may or may not work
    public static boolean mouseUpdated = false;
    FrameTimeGraph ftGraph;

    //Game-wide variables
    static Level currentScene = new MainMenu();
    public static int highScore = 0;
    public static final LoadingScreen loadingScreen = new LoadingScreen();

    public static ArrayList<Scene> renderLayers = new ArrayList<>();

    //I think i have to reinvent the button :( because the swing ones are acting weird
    public static ArrayList<JComponent> uiComponents = new ArrayList<>();






    GameFrame(){
        this.setFocusable(true);
        this.addKeyListener(new KeyListener());
        this.addMouseMotionListener(new MotionDetector());
        this.addMouseListener(new MotionDetector() {
        });
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                GAME_WIDTH = e.getComponent().getWidth();
                GAME_HEIGHT = e.getComponent().getHeight();
                SCREEN_SIZE.setSize(GAME_WIDTH,GAME_HEIGHT);

                currentScene.windowResized();

            }
        });
        //this.addMouseListener(new MouseListener());
        this.setPreferredSize(SCREEN_SIZE);


        soundManager = new SoundManager();


        ftGraph = new FrameTimeGraph();

        loadingScreen.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        gameThread = new Thread(this);
        gameThread.start();
        currentScene.initialize();


    }

    public void run(){
        //Game Loop
        long lastTime = System.nanoTime();
        double amountOfTicks = targetFPS;
        double ns;
        long timer = System.currentTimeMillis();
        int frames = 0;
        double frameTimeSum = 0;

        while(true){
            long now = System.nanoTime();
            ns = 1000000000 / 60.0;
            if(now - lastTime > ns) {
                long frameStart = System.nanoTime();
                mouseUpdated = true;
                if(!LOADING) {

                    update();
                }
                frames++;
                repaint();

                lastTime += ns;
                long frameEnd = System.nanoTime();
                double frameTime = (frameEnd - frameStart) / 1000000.0;
                delta = frameTime;
                avgFrameTime = frameTime;
                frameTimeSum += frameTime;
                if(ftGraph != null){
                    ftGraph.addFrame(frameTime);
                }
            }
            if(System.currentTimeMillis() - timer >= 1000) {
                DiscordRPC.discordUpdatePresence(GameWindow.presence);
                timer += 1000;
                FPS = frames;
                //avgFrameTime = frameTimeSum / frames;
                frames = 0;
                frameTimeSum = 0;
            }
        }
    }

    public void update(){
        currentScene.updateScene();
    }

    @Override
    public void paintComponent(Graphics g2){
        //super.paintComponent(g2);
        bufferedImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        graphics2D = bufferedImage.createGraphics();

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if(!LOADING) {

            draw(graphics2D);
        } else {
            loadingScreen.draw(graphics2D);
        }
        g2.drawImage(bufferedImage,0,0,this);
    }

    public void draw(Graphics2D g){
        currentScene.draw(g);
        if(Heartbreaker.DEBUG_MODE){
            ftGraph.draw(g);
        }
    }

    public static Level getCurrentScene(){
        return currentScene;
    }
    public static void setCurrentScene(Level level) {

        // Set the preferred size of the loading screen
        loadingScreen.setPreferredSize(SCREEN_SIZE);

        // Add the loading screen to the GameFrame and ensure it's properly displayed
        GameWindow.panel.add(loadingScreen);
        GameWindow.panel.revalidate();
        GameWindow.panel.repaint();

        // Start the scene initialization in the background
        SwingWorker<Void, String> sceneInitializer = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                LOADING = true;

                // Display the loading screen
                loadingScreen.setVisible(true);

                // Initialize the current scene
                currentScene = level;
                currentScene.initialize();
                LOADING = false;

                // Hide the loading screen
                loadingScreen.setVisible(false);

                return null;
            }

            @Override
            protected void done() {
                // Remove the loading screen from the GameFrame
                removeLoadingScreen();
            }
        };

        // Start the scene initialization in the background
        sceneInitializer.execute();
    }

    private static void removeLoadingScreen() {
        Component[] components = GameWindow.panel.getComponents();
        for (Component component : components) {
            if (component instanceof LoadingScreen) {
                GameWindow.panel.remove(component);
                GameWindow.panel.revalidate();
                GameWindow.panel.repaint();
                break;
            }
        }
    }


    public static class KeyListener extends KeyAdapter{

        public void keyPressed(KeyEvent e){
            currentScene.keyPressed(e);
            switch (e.getKeyCode()){
                case KeyEvent.VK_UP -> targetFPS++;
            }
        }
        public void keyReleased(KeyEvent e){
            currentScene.keyReleased(e);
        }
    }

    public static class MotionDetector extends MouseAdapter{
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            if(!mouseUpdated) {
                //currentScene.mouseMoved(e);
                //mouseUpdated = true;
            }

        }

        public void mouseDragged(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            //currentScene.mouseMoved(e);
        }

        public void mousePressed(MouseEvent e) {
            currentScene.mousePressed(e);
        }
        public void mouseReleased(MouseEvent e) {
            currentScene.mouseReleased(e);
        }


    }


}
