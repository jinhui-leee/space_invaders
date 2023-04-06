package org.newdawn.spaceinvaders;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Music {
    public static void mainAudioOn(){

        try {
            File file = new File("src/Game.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.loop(Clip.LOOP_CONTINUOUSLY);
//            clip.loop(3);
            clip.start();
        } catch (Exception e) {
            System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
        }
    }
    public static void shotAudio(){
        try {
            File file = new File("src/shot.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
//            clip.loop(Clip.LOOP_CONTINUOUSLY);
//            clip.loop(1);
            clip.start();
        } catch (Exception e) {
            System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
        }
    }
}