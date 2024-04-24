package Heartbreaker.engine.input;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MouseInput extends MouseAdapter {
    private static final Map<Integer, Boolean> pressedButtons = new HashMap<>();

    private static Point mousePosition = new Point(0,0);
    private static double scrollAmount = 0;
    private static long movedTime = 0;
    private static long scrolledTime = 0;


    public void mouseMoved(MouseEvent e){
        movedTime = System.currentTimeMillis();
        mousePosition = e.getPoint();
    }
    public void mouseDragged(MouseEvent e){
        mouseMoved(e);
    }

    public void mousePressed(MouseEvent e){
        pressedButtons.put(e.getButton(),true);
    }
    public void mouseReleased(MouseEvent e){
        pressedButtons.put(e.getButton(),false);
    }

    public void mouseClicked(MouseEvent e){

    }

    public void mouseWheelMoved(MouseWheelEvent e){
        scrollAmount = e.getPreciseWheelRotation();
        scrolledTime = System.currentTimeMillis();

    }

    public static Point getPosition(){
        return mousePosition;
    }

    public static boolean isButtonPressed(int key){
        if(pressedButtons.containsKey(key)){
            return pressedButtons.get(key);
        } else {
            return false;
        }
    }

    public static boolean isMouseMoved(){
        return System.currentTimeMillis() - movedTime <= 10;
    }

    public static double getScrollAmount(){
        if(System.currentTimeMillis() - scrolledTime <= 20) {
            return scrollAmount;
        } else {
            scrollAmount = 0;
            return 0.0;
        }
    }
    public static List<Map.Entry<Integer, Boolean>> getPressedButtons(){
        return pressedButtons.entrySet().stream().toList();
    }


}
