package Heartbreaker.valentine;

import Heartbreaker.valentine.input.KeyInput;
import Heartbreaker.valentine.input.MouseInput;
import Heartbreaker.valentine.scenes.DefaultScene;
import Heartbreaker.valentine.scenes.Scene;
import Heartbreaker.main.Heartbreaker;
import Heartbreaker.scenes.*;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.AffineTransform;
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
                syncGameSize();
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

                KeyInput.update();
                if(!LOADING) {
                    update();
                    if(KeyInput.isKeyPressed(KeyEvent.VK_M)){
                        Heartbreaker.stopTrack();
                    }
                }


                repaint();

                frames++;
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

                //If we fell far behind (breakpoint, window drag, slow frame) don't run a burst of catch up frames
                if(System.nanoTime() - lastTime > ns * 5){
                    lastTime = System.nanoTime();
                }
            }

            if(System.currentTimeMillis() - timer >= 1000) {
                timer += 1000;
                FPS = frames;
                //avgFrameTime = frameTimeSum / frames;
                frames = 0;
                frameTimeSum = 0;
            }

            waitUntil(lastTime + (long) ns);
        }
    }

    /** Running estimate of how long Thread.sleep(1) can take, in nanoseconds */
    private double sleepEstimate = 2_000_000;

    /**
     * Waits until System.nanoTime() reaches the deadline without pegging a CPU core.
     * Sleeps in 1ms naps while there's comfortably enough time left, then spins for the last bit,
     * since sleep alone can overshoot by a millisecond or more.
     * @param deadline time in System.nanoTime() units
     */
    private void waitUntil(long deadline){
        long remaining = deadline - System.nanoTime();
        //Only nap if even a slow nap would still wake up before the deadline
        while(remaining > sleepEstimate){
            long start = System.nanoTime();
            try{
                Thread.sleep(1);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
            long took = System.nanoTime() - start;
            //Jump up to a slow nap immediately (a late frame is worse than some spinning), drift back down slowly
            sleepEstimate = Math.max(took, sleepEstimate * 0.99 + took * 0.01);
            remaining = deadline - System.nanoTime();
        }
        while(System.nanoTime() < deadline){
            Thread.onSpinWait();
        }
    }

    public static void setFPS(double fps){
        ns = 1000000000 / fps;
    }
    public void update(){
        currentScene.updateScene(delta);
    }

    /**
     * Copies the panel's real size into GAME_WIDTH / GAME_HEIGHT and tells the scene.
     */
    private void syncGameSize(){
        GAME_WIDTH = getWidth();
        GAME_HEIGHT = getHeight();
        SCREEN_SIZE.setSize(GAME_WIDTH,GAME_HEIGHT);
        if(currentScene != null){
            currentScene.windowResized();
        }
    }

    @Override
    public void paintComponent(Graphics g2){
        //super.paintComponent(g2);
        //Windows can shrink the window to fit the screen before any resize event fires, so catch that here
        if(getWidth() > 0 && getHeight() > 0 && (getWidth() != GAME_WIDTH || getHeight() != GAME_HEIGHT)){
            syncGameSize();
        }
        //Render at the display's real resolution so windows scaling (125%, 150%...) doesn't stretch the frame and blur text
        AffineTransform screenTransform = ((Graphics2D) g2).getTransform();
        double scaleX = screenTransform.getScaleX();
        double scaleY = screenTransform.getScaleY();
        int pixelWidth = Math.max(1, (int) Math.ceil(getWidth() * scaleX));
        int pixelHeight = Math.max(1, (int) Math.ceil(getHeight() * scaleY));
        bufferedImage = new BufferedImage(pixelWidth,pixelHeight,BufferedImage.TYPE_3BYTE_BGR);
        //bufferedImage = createVolatileImage(getWidth(),getHeight());
        graphics2D = bufferedImage.createGraphics();
        graphics2D.scale(scaleX, scaleY);

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        if(!LOADING) {

            draw(graphics2D);
        } else {
            loadingScreen.draw(graphics2D);
        }
        graphics2D.dispose();
        g2.drawImage(bufferedImage,0,0,getWidth(),getHeight(),this);
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

        VWindow.clearComponents();

        // Add the loading screen to the GameFrame and ensure it's properly displayed
        VWindow.panel.add(loadingScreen);
        VWindow.panel.revalidate();
        VWindow.panel.repaint();

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
        Component[] components = VWindow.panel.getComponents();
        for (Component component : components) {
            if (component instanceof LoadingScreen) {
                VWindow.panel.remove(component);
                VWindow.panel.revalidate();
                VWindow.panel.repaint();
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
