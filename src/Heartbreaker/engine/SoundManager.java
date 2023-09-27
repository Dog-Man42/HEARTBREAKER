package Heartbreaker.engine;


import javax.sound.sampled.*;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class SoundManager {
    private SoundLibrary soundLibrary;
    private ArrayList<Clip> playingClips;

    public static final String flatline = "flatline.wav";
    public static final String heartbreak = "heartbreak.wav";
    public static final String heartDamage = "heartDamage.wav";
    public static final String playerDamage = "playerDamage.wav";
    public static final String shieldOrbDamage = "shieldOrbDamage.wav";
    public static final String shieldOrbShrink = "shieldOrbShrink.wav";
    public static final String shieldOrbDestroy = "shieldOrbDestroy.wav";
    public static final String shootGrav = "shootGrav.wav";
    public static final String shootGeneric = "shootGeneric.wav";
    public static final String shootShield = "shootShield.wav";

    SoundManager(){
        soundLibrary = new SoundLibrary();
        playingClips = new ArrayList<>();

    }


    public void playClip(String file) {

        try {
            InputStream is = new BufferedInputStream(getClass().getResourceAsStream("/Heartbreaker/sounds/" + file));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(is);
            final Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            clip.addLineListener(myLineEvent -> {
                if (myLineEvent.getType() == LineEvent.Type.STOP)
                    clip.close();
            });


            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(.3f));

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}
