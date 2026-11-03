package Heartbreaker.valentine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VCore {


    private static Map<String, Boolean> flags = Collections.synchronizedMap(new HashMap<>());
    private static Map<String, Integer> values = Collections.synchronizedMap(new HashMap<>());

    private static Map<String, VWindow> runningWindows = Collections.synchronizedMap(new HashMap<>());


    public static void setFlag(String key, Boolean value){
        flags.put(key, value);
    }

    public static boolean getFlag(String key) throws NullPointerException {
        if (!flags.containsKey(key)) {
            throw new NullPointerException("Key:" + key + " has not been initialized!");
        }
        return flags.get(key);
    }

}
