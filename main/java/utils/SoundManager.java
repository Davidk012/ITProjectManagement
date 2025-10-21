package utils;

import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, Clip> soundClips;
    private Clip backgroundMusic;
    private boolean soundEnabled;
    private boolean musicEnabled;
    
    private SoundManager() {
        soundClips = new HashMap<>();
        soundEnabled = true;
        musicEnabled = true;
        loadSounds();
    }
    
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    private void loadSounds() {
        try {
            // These would be your actual sound files
            // For now, we'll create placeholder clips
            loadSound("background", "/sounds/background.wav");
            loadSound("collect", "/sounds/collect.wav");
            loadSound("enemy_collision", "/sounds/enemy_collision.wav");
            loadSound("level_complete", "/sounds/level_complete.wav");
            loadSound("powerup", "/sounds/powerup.wav");
        } catch (Exception e) {
            System.out.println("Error loading sounds: " + e.getMessage());
        }
    }
    
    private void loadSound(String key, String filePath) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                getClass().getResource(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            soundClips.put(key, clip);
        } catch (Exception e) {
            System.out.println("Could not load sound: " + filePath);
        }
    }
    
    public void playSound(String key) {
        if (!soundEnabled) return;
        
        Clip clip = soundClips.get(key);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    public void loopSound(String key) {
        if (!musicEnabled) return;
        
        Clip clip = soundClips.get(key);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stopSound(String key) {
        Clip clip = soundClips.get(key);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    
    public void startBackgroundMusic() {
        if (backgroundMusic == null) {
            backgroundMusic = soundClips.get("background");
        }
        if (backgroundMusic != null && musicEnabled) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }
    
    // Getters and setters
    public void setSoundEnabled(boolean enabled) { soundEnabled = enabled; }
    public void setMusicEnabled(boolean enabled) { 
        musicEnabled = enabled; 
        if (!enabled) {
            stopBackgroundMusic();
        } else {
            startBackgroundMusic();
        }
    }
    public boolean isSoundEnabled() { return soundEnabled; }
    public boolean isMusicEnabled() { return musicEnabled; }
}