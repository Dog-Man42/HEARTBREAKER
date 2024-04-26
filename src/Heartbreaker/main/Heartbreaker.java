package Heartbreaker.main;

import Heartbreaker.engine.*;
import Heartbreaker.scenes.MainMenu;


public class Heartbreaker {
    static MidiPlayer track;
    public static final String version = "Version: 0.0.5a";
    public static boolean DEBUG_MODE = false;

    private static double targetFPS = 60.0;
    
    public static void main(String[] args) throws Exception {

        for(int i = 0; i < args.length; i++){
            String[] strs = args[i].split("-");
            for(int j = 0; j < strs.length; j++){
                if(strs[j].equals("debug")){
                    DEBUG_MODE = true;
                }
                if(strs[j].contains("FPS:")){
                    String[] tempStrings = strs[j].split(":");
                    try{
                        double temp = Double.parseDouble(tempStrings[1]);
                        if(temp >= 1){
                            targetFPS = temp;
                        }
                    } catch (Exception e){
                        System.out.println("invalid argument." + strs[j]);
                    }
                }
            }
        }
        track = new MidiPlayer();
        track.init();
        track.setBpmTo(60f);
        GameWindow window = new GameWindow("HEARTBREAKER", new MainMenu());
        window.DEBUG = DEBUG_MODE;
        window.setFPS(targetFPS);
        
        

    }
    public static void setBpm(float bpm){
        track.setBpmTo(bpm);
    }
    public static void startTrack(){

        track.start();
    }
    public static void stopTrack(){
        track.stop();
    }
}