package Heartbreaker.engine.audio;

import java.io.BufferedInputStream;
import java.io.InputStream;

public enum Sounds {
    FLAT_LINE("flatline.wav"),
    HEARTBREAK("heartbreak.wav"),
    HEART_DAMAGE("hearDamage.wav"),
    PLAYER_DAMAGE("playerDamage.wav"),
    SHIELD_ORB_DAMAGE("shieldOrbDamage.wav"),
    SHIELD_ORB_DESTROY("shieldOrbDestroy.wav"),
    SHIELD_ORB_SHRINK("shieldOrbShrink.wav"),
    SHOOT_GENERIC("shootGeneric.wav"),
    SHOOT_SHEILD("shootShield.wav"),

    ;

    private final String filename;

    Sounds(String filename){
        this.filename = filename;

    }

    public BufferedInputStream getSound() {
        return new BufferedInputStream(getClass().getResourceAsStream("/Heartbreaker/sounds/" + filename));
    }

}
