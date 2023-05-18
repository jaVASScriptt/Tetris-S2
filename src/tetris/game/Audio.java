package tetris.game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {
    private final Clip clip;
    private final FloatControl volume;

    private static final int ENCODING = 44100;
    public static final int MENU_LOOP = ENCODING * 8;
    public static final int GAME_LOOP = -1;


    public Audio(String source) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        this.clip = AudioSystem.getClip();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(source));
        clip.open(audioInputStream);

        this.volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        setLoopPoint(MENU_LOOP);
        play();
    }

    public void setLoopPoint(int lp) {
        clip.setLoopPoints(0, lp);
    }

    public void play() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public void setVolume(double newVolume) {
        volume.setValue(20.0f * (float) Math.log10(newVolume / 100.0));
    }
}