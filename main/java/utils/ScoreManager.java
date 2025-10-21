package utils;

import java.awt.*;
import java.io.*;
import java.util.prefs.Preferences;

public class ScoreManager {
    private int score;
    private int lives;
    private int level;
    private int maxUnlockedLevel;
    private int highScore;
    
    private Preferences prefs;
    
    public ScoreManager() {
        this.prefs = Preferences.userNodeForPackage(ScoreManager.class);
        loadHighScore();
        reset();
    }
    
    public void reset() {
        this.score = 0;
        this.lives = 3;
        this.level = 1;
        this.maxUnlockedLevel = 1;
    }
    
    public void addScore(int points) {
        score += points;
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
    }
    
    public void loseLife() {
        lives--;
    }
    
    public void gainLife() {
        lives++;
    }
    
    public void unlockLevel(int level) {
        if (level > maxUnlockedLevel) {
            maxUnlockedLevel = level;
        }
    }
    
    public void nextLevel() {
        level++;
    }
    
    private void loadHighScore() {
        highScore = prefs.getInt("highScore", 0);
    }
    
    private void saveHighScore() {
        prefs.putInt("highScore", highScore);
    }
    
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Draw score
        g2d.drawString("Score: " + score, 20, 30);
        
        // Draw lives
        g2d.drawString("Lives: " + lives, 20, 60);
        
        // Draw level
        g2d.drawString("Level: " + level, 20, 90);
        
        // Draw high score
        g2d.drawString("High Score: " + highScore, Constants.WINDOW_WIDTH - 200, 30);
    }
    
    // Getters
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getLevel() { return level; }
    public int getMaxUnlockedLevel() { return maxUnlockedLevel; }
    public int getHighScore() { return highScore; }
    
    // Setters
    public void setLevel(int level) { this.level = level; }
}