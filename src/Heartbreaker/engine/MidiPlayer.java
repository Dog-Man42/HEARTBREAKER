package Heartbreaker.engine;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.midi.*;


public class MidiPlayer {
    private float BPM = 60;
    private Sequencer sequencer;
    public void init() throws Exception {
        sequencer = MidiSystem.getSequencer();
        sequencer.addMetaEventListener(new listener());
        sequencer.open();

        InputStream is = new BufferedInputStream(getClass().getResourceAsStream("/Heartbreaker/midi/Heartbreaker.mid"));
        sequencer.setSequence(is);
        System.out.println(sequencer.getSequence().getResolution());
    }
    public void setBpmTo(float bpm){
        sequencer.setTempoInBPM(bpm);
        BPM = bpm;
    }
    public void start(){
        sequencer.stop();
        sequencer.setTickPosition(-150);
        sequencer.start();
    }
    public class listener implements MetaEventListener {

        @Override
        public void meta(MetaMessage meta) {
            if(meta.getType() == 0x2F){
                sequencer.setTickPosition(6114);
                sequencer.start();
                sequencer.setTempoInBPM(BPM);
            }

        }
    }
}
