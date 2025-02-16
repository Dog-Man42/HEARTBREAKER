package Heartbreaker.engine;

import Heartbreaker.engine.input.KeyInput;
import Heartbreaker.engine.input.MouseInput;
import Heartbreaker.engine.scenes.DefaultScene;
import Heartbreaker.engine.scenes.Scene;
import Heartbreaker.main.Heartbreaker;
import Heartbreaker.scenes.*;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GameFrame extends JPanel implements Runnable{

    public static int GAME_WIDTH = 1920;
    public static int GAME_HEIGHT = 1080;
    static Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);


    public static double delta = 0;
    public static double frameTime = 0;
    public static int FPS = 0;
    public static double targetFPS = 60;

    private static double ns;

    public static boolean LOADING = false;



    Thread gameThread;
    public static BufferedImage bufferedImage;
    public static SoundManager soundManager;
    Graphics2D graphics2D;



    FrameTimeGraph ftGraph;



    static Scene currentScene;
    public static int highScore = 0;
    public static final LoadingScreen loadingScreen = new LoadingScreen();

    public static ArrayList<Scene> renderLayers = new ArrayList<>();

    public static ArrayList<JComponent> uiComponents = new ArrayList<>();






    GameFrame(Scene mainScene){
        this.setFocusable(true);
        MouseInput mouseInput = new MouseInput();
        this.addMouseListener(mouseInput);
        this.addMouseMotionListener(mouseInput);
        this.addMouseWheelListener(mouseInput);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                GAME_WIDTH = e.getComponent().getWidth();
                GAME_HEIGHT = e.getComponent().getHeight();
                SCREEN_SIZE.setSize(GAME_WIDTH,GAME_HEIGHT);
                currentScene.windowResized();
                System.gc();

            }
        });
        //this.addMouseListener(new MouseListener());
        this.setPreferredSize(SCREEN_SIZE);


        soundManager = new SoundManager();


        ftGraph = new FrameTimeGraph();

        loadingScreen.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        gameThread = new Thread(this);
        gameThread.start();
        DefaultScene defaultScene = new DefaultScene();
        defaultScene.mainScene = mainScene;
        currentScene = mainScene;
        currentScene.initialize();



    }



    public void run(){
        //Game Loop
        long lastTime = System.nanoTime();

        ns = 1000000000 / targetFPS;
        long timer = System.currentTimeMillis();
        int frames = 0;
        double frameTimeSum = 0;
        long frameStart = System.nanoTime();
        while(true){
            long now = System.nanoTime();
            if(now - lastTime > ns) {

                if(!LOADING) {
                    update();
                    if(KeyInput.isKeyPressed(KeyEvent.VK_M)){
                        Heartbreaker.stopTrack();
                    }
                }
                frames++;

                repaint();

                try {

                    if( currentScene.getObjects() != null){
                        System.out.println(Runtime.getRuntime().totalMemory());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

                lastTime += ns;
                long frameEnd = System.nanoTime();
                frameTime = (frameEnd - frameStart) / 1000000.0;
                delta = frameTime / 1000;
                //System.out.printf("Frametime MS: %.5f Delta: %.10f%n", frameTime, delta);

                frameTimeSum += frameTime;
                if(ftGraph != null){
                    ftGraph.addFrame(frameTime);
                }
                frameStart = System.nanoTime();


            }
            if(System.currentTimeMillis() - timer >= 1000) {
                timer += 1000;
                FPS = frames;
                //avgFrameTime = frameTimeSum / frames;
                frames = 0;
                frameTimeSum = 0;
            }
        }
    }

    public static void setFPS(double fps){
        ns = 1000000000 / fps;
    }
    public void update(){
        currentScene.updateScene(delta);
    }

    @Override
    public void paintComponent(Graphics g2){
        //super.paintComponent(g2);
        bufferedImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        //bufferedImage = createVolatileImage(getWidth(),getHeight());
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
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(3));
        currentScene.draw(g, delta);
        if(Heartbreaker.DEBUG_MODE){
            ftGraph.draw(g, delta);
        }
    }

    public static Scene getCurrentScene(){
        return currentScene;
    }
    public static void setCurrentScene(Scene scene) {


        // Set the preferred size of the loading screen
        loadingScreen.setPreferredSize(SCREEN_SIZE);

        GameWindow.clearComponents();

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
                currentScene = scene;
                try {
                    currentScene.initialize();
                } catch(Exception e) {
                    e.printStackTrace();
                }

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

    public static long getBytesFromList(ArrayList list) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(list);
        out.close();
        return baos.toByteArray().length;
    }



}
