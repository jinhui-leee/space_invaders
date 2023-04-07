package org.newdawn.spaceinvaders;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;


public class Music {
    private Clip clip;
    private  long stopTime = 0;
    public boolean isPlaying = false;

    public void playMusic() {
        try {
            File musicFile = new File("src/audio/Game.wav");
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            if (stopTime > 0) {
                clip.setMicrosecondPosition(stopTime);
            }
            clip.start();
            isPlaying = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying(){
        return clip != null && clip.isActive();
    }

    public void stopMusic() {
        if (clip != null && clip.isActive() && isPlaying) {
            stopTime = clip.getMicrosecondPosition();
            clip.stop();
            isPlaying = false;
        }
    }
    public static void shotAudio () {
        try {
            File file = new File("src/audio/Shot.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception e) {
            System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
        }
    }
}