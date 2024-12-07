package dinojump;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager {
    private Clip clip;

    // Load audio file
    public void load(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Play the audio
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Mulai dari awal
            clip.start();
        }
    }

    // Stop the audio
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
