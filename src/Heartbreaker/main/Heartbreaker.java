package Heartbreaker.main;

import Heartbreaker.engine.*;



public class Heartbreaker {
    static MidiPlayer track;
    public static final String version = "Version: 0.0.5a";
    public static boolean DEBUG_MODE = false;
    public static void main(String[] args) throws Exception {

        for(int i = 0; i < args.length; i++){
            if(args[i].equals("-debug")){
                DEBUG_MODE = true;
            }
        }
        track = new MidiPlayer();

        track.init();
        track.setBpmTo(60f);
        GameWindow window = new GameWindow("HEARTBREAKER");
    }
    public static void setBpm(float bpm){
        track.setBpmTo(bpm);
    }
    public static void startTrack(){
        track.start();
    }
}