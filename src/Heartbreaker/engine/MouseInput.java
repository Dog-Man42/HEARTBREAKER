package Heartbreaker.engine;

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
        scrollAmount += e.getPreciseWheelRotation();
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
        return movedTime == System.currentTimeMillis();
    }

    public static double getScrollAmount(){
        return scrollAmount;
    }
    public static List<Map.Entry<Integer, Boolean>> getPressedButtons(){
        return pressedButtons.entrySet().stream().toList();
    }


}
