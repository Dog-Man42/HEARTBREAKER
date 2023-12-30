package Heartbreaker.main;

import Heartbreaker.engine.*;
import java.io.BufferedReader;
import java.io.FileReader;


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
        String key;
        try (BufferedReader br = new BufferedReader(new FileReader("src/key.txt"))){
            key = br.readLine();
        } catch(Exception e) {
            e.printStackTrace();
            key = null;
            System.out.println("notfound");
        }
        
        
        
        GameWindow window = new GameWindow("HEARTBREAKER", key);
        window.start();

        
        

    }
    public static void setBpm(float bpm){
        track.setBpmTo(bpm);
    }
    public static void startTrack(){
        track.start();
    }
}