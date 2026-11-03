//https://stackoverflow.com/questions/18037576/how-do-i-check-if-the-user-is-pressing-a-key

package Heartbreaker.valentine.input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeyInput {
    private static final Map<Integer, Boolean> pressedKeys = new HashMap<>();

    //Allows for changing keybinds for actions
    private static final Map<String, Integer> registeredActionBinds = new HashMap<>();

    //Keys that went down since the last frame, and the set exposed for the current frame
    private static final Set<Integer> pendingPresses = new HashSet<>();
    private static Set<Integer> framePresses = new HashSet<>();

    //Same as above, but also counting the OS's key repeat while a key is held
    private static final Set<Integer> pendingRepeats = new HashSet<>();
    private static Set<Integer> frameRepeats = new HashSet<>();

    static {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(event -> {
            synchronized (KeyInput.class) {
                if (event.getID() == KeyEvent.KEY_PRESSED) {
                    if (!pressedKeys.getOrDefault(event.getKeyCode(), false)) {
                        pendingPresses.add(event.getKeyCode());
                    }
                    pendingRepeats.add(event.getKeyCode());
                    pressedKeys.put(event.getKeyCode(), true);
                }
                else if (event.getID() == KeyEvent.KEY_RELEASED) pressedKeys.put(event.getKeyCode(), false);
                return false;
            }
        });
    }

    /**
     * Called by the engine once per frame, before the scene updates.
     * Moves presses that happened since the last frame into the current frame.
     */
    public static synchronized void update() {
        framePresses = new HashSet<>(pendingPresses);
        pendingPresses.clear();
        frameRepeats = new HashSet<>(pendingRepeats);
        pendingRepeats.clear();
    }

    public static boolean isKeyPressed(int keyCode) { // Any key code from the KeyEvent class
        return pressedKeys.getOrDefault(keyCode, false);
    }

    /**
     * True only on the frame the key went down, no matter how long it is held.
     * @param keyCode Any key code from the KeyEvent class
     */
    public static synchronized boolean isKeyJustPressed(int keyCode) {
        return framePresses.contains(keyCode);
    }

    /**
     * True on the frame the key went down, then again at the OS key repeat rate while held. Good for stepping through things.
     * @param keyCode Any key code from the KeyEvent class
     */
    public static synchronized boolean isKeyRepeated(int keyCode) {
        return frameRepeats.contains(keyCode);
    }

    public static void registerKeyBind(String action, int keyCode){
        registeredActionBinds.put(action,keyCode);
    }

    public static boolean isBindPressed(String action){
        return isKeyPressed(registeredActionBinds.get(action));
    }
}
