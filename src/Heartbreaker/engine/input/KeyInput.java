
//https://stackoverflow.com/questions/18037576/how-do-i-check-if-the-user-is-pressing-a-key

package Heartbreaker.engine.input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
public class KeyInput {
    private static final Map<Integer, Boolean> pressedKeys = new HashMap<>();

    //Allows for changing keybinds for actions
    private static final Map<String, Integer> registeredActionBinds = new HashMap<>();

    static {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(event -> {
            synchronized (KeyInput.class) {
                if (event.getID() == KeyEvent.KEY_PRESSED) pressedKeys.put(event.getKeyCode(), true);
                else if (event.getID() == KeyEvent.KEY_RELEASED) pressedKeys.put(event.getKeyCode(), false);
                return false;
            }
        });
    }

    public static boolean isKeyPressed(int keyCode) { // Any key code from the KeyEvent class
        return pressedKeys.getOrDefault(keyCode, false);
    }

    public static void registerKeyBind(String action, int keyCode){
        registeredActionBinds.put(action,keyCode);
    }

    public static boolean isBindPressed(String action){
        return isKeyPressed(registeredActionBinds.get(action));
    }
}
