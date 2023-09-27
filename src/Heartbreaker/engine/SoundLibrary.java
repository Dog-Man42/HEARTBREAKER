package Heartbreaker.engine;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SoundLibrary {
    public static Map<String, AudioInputStream> sounds;

    public SoundLibrary(){
        sounds = new HashMap<>();
        loadSound("flatline.wav");
        loadSound("shootGrav.wav");
        loadSound("heartDamage.wav");
        loadSound("playerDamage.wav");
        loadSound("shieldOrbDamage.wav");
        loadSound("shieldOrbDestroy.wav");
        loadSound("shieldOrbShrink.wav");
        loadSound("shootGeneric.wav");

    }
    private void loadSound(String filename){
        /*
        try {
            Clip clip = AudioSystem.getClip();
            InputStream is = getClass().getResourceAsStream("/Heartbreaker/sounds/" + filename);
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));

            clip.open(ais);
            sounds.put(filename, clip);
        } catch (Exception e){
            e.printStackTrace();
        }

         */
        try {
            // Obtain an AudioInputStream from a file or other source
            InputStream is = getClass().getResourceAsStream("/Heartbreaker/sounds/" + filename);
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));

            // Store the AudioInputStream in the sounds map
            sounds.put(filename, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public AudioInputStream getAudioInputStream(String fileName) {
        return sounds.get(fileName);
    }

}
