package Heartbreaker.valentine.ui;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Picks fonts that exist on this machine.
 *
 * @author tuckt
 */
public class Fonts {

    private static final String[] MONO_PREFERRED = {"JetBrains Mono", "Cascadia Mono", "Consolas", "Menlo"};
    private static String monoFamily;

    /**
     * @return the first installed font from a list of clean monospaced fonts, or Java's default monospaced font
     */
    public static Font mono(int style, int size){
        if(monoFamily == null){
            List<String> available = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
            monoFamily = Font.MONOSPACED;
            for(String name : MONO_PREFERRED){
                if(available.contains(name)){
                    monoFamily = name;
                    break;
                }
            }
        }
        return new Font(monoFamily, style, size);
    }
}
