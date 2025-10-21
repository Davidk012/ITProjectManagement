package main;

import entities.*;
import maze.Level;
import maze.LevelLoader;
import utils.ScoreManager;
import utils.SoundManager;
import utils.Constants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private Level currentLevel;
    private Player player;
    private ScoreManager scoreManager;
    private SoundManager soundManager;
    private LevelSelectionScreen levelSelectionScreen;
    private List<PowerUp> activePowerUps;
    private int gameState = Constants.STATE_MENU;
    private boolean paused = false;
    
    public GameManager() {
        scoreManager = new ScoreManager();
        soundManager = SoundManager.getInstance();
        levelSelectionScreen = new LevelSelectionScreen(this, scoreManager);
        activePowerUps = new ArrayList<>();
        
        soundManager.startBackgroundMusic();
    }
    
    public void update() {
        if (paused || gameState != Constants.STATE_PLAYING) return;
        
        player.update();
        currentLevel.updateEnemies();
        updatePowerUps();
        checkCollisions();
        checkLevelCompletion();
    }
    
    private void updatePowerUps() {
        for (int i = activePowerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = activePowerUps.get(i);
            powerUp.update(player);
            if (powerUp.isExpired()) {
                activePowerUps.remove(i);
            }
        }
    }
    
    public void draw(Graphics2D g2d) {
        if (levelSelectionScreen.isVisible()) {
            levelSelectionScreen.draw(g2d);
            return;
        }
        
        if (currentLevel != null) {
            currentLevel.draw(g2d);
        }
        
        // Draw UI
        scoreManager.draw(g2d);
        
        // Draw active power-up indicators
        drawPowerUpIndicators(g2d);
        
        if (gameState == Constants.STATE_GAME_OVER) {
            drawGameOver(g2d);
        } else if (gameState == Constants.STATE_LEVEL_COMPLETE) {
            drawLevelComplete(g2d);
        } else if (paused) {
            drawPauseScreen(g2d);
        }
    }
    
    private void drawPowerUpIndicators(Graphics2D g2d) {
        int yPos = 120;
        for (PowerUp powerUp : activePowerUps) {
            if (powerUp.isActive()) {
                long timeLeft = powerUp.getDuration() - (System.currentTimeMillis() - powerUp.getActivationTime()); // Fixed: use getters
                String text = "";
                
                if (powerUp instanceof SpeedBoostPowerUp) {
                    text = "SPEED BOOST: " + (timeLeft / 1000) + "s";
                    g2d.setColor(Color.CYAN);
                } else if (powerUp instanceof InvincibilityPowerUp) {
                    text = "INVINCIBLE: " + (timeLeft / 1000) + "s";
                    g2d.setColor(Color.YELLOW);
                }
                
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                g2d.drawString(text, 20, yPos);
                yPos += 20;
            }
        }
    }
    
    private void checkCollisions() {
        // Check player-enemy collisions
        if (!player.isInvincible() && currentLevel.checkEnemyCollisions(player)) {
            soundManager.playSound("enemy_collision");
            handlePlayerDeath();
        }
        
        // Check power-up collisions - Fixed method call
        PowerUp collectedPowerUp = currentLevel.checkPowerUpCollisions(player);
        if (collectedPowerUp != null) {
            collectedPowerUp.activate(player);
            activePowerUps.add(collectedPowerUp);
            scoreManager.addScore(200);
        }
    }
    
    private void checkLevelCompletion() {
        if (currentLevel.isLevelComplete(player)) {
            soundManager.playSound("level_complete");
            scoreManager.addScore(1000);
            scoreManager.unlockLevel(currentLevel.getLevelNumber() + 1);
            gameState = Constants.STATE_LEVEL_COMPLETE;
            
            new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        loadLevel(currentLevel.getLevelNumber() + 1);
                        gameState = Constants.STATE_PLAYING;
                    }
                },
                2000
            );
        }
    }
    
    private void handlePlayerDeath() {
        scoreManager.loseLife();
        if (scoreManager.getLives() > 0) {
            player.resetPosition();
            // Reset power-ups
            for (PowerUp powerUp : activePowerUps) {
                powerUp.removeEffect(player);
            }
            activePowerUps.clear();
        } else {
            gameState = Constants.STATE_GAME_OVER;
        }
    }
    
    public void loadLevel(int levelNumber) {
        currentLevel = LevelLoader.loadLevel(levelNumber);
        if (currentLevel != null) {
            player = currentLevel.getPlayer();
            activePowerUps.clear();
            scoreManager.setLevel(levelNumber);
        }
    }
    
    private void drawGameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        g2d.drawString("GAME OVER", Constants.WINDOW_WIDTH / 2 - 140, Constants.WINDOW_HEIGHT / 2 - 50);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        g2d.drawString("Final Score: " + scoreManager.getScore(), 
                      Constants.WINDOW_WIDTH / 2 - 100, Constants.WINDOW_HEIGHT / 2 + 20);
        g2d.drawString("Press R to restart or M for menu", 
                      Constants.WINDOW_WIDTH / 2 - 180, Constants.WINDOW_HEIGHT / 2 + 60);
    }
    
    private void drawLevelComplete(Graphics2D g2d) {
        g2d.setColor(new Color(0, 100, 0, 180));
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString("LEVEL COMPLETE!", Constants.WINDOW_WIDTH / 2 - 150, Constants.WINDOW_HEIGHT / 2);
    }
    
    private void drawPauseScreen(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        g2d.drawString("PAUSED", Constants.WINDOW_WIDTH / 2 - 100, Constants.WINDOW_HEIGHT / 2);
    }
    
    public void keyPressed(int keyCode) {
        if (levelSelectionScreen.isVisible()) {
            levelSelectionScreen.keyPressed(keyCode);
            return;
        }
        
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE:
                if (gameState == Constants.STATE_PLAYING) {
                    paused = !paused;
                }
                break;
            case KeyEvent.VK_M:
                if (gameState == Constants.STATE_GAME_OVER || paused) {
                    levelSelectionScreen.show();
                    paused = false;
                    gameState = Constants.STATE_PLAYING;
                }
                break;
            case KeyEvent.VK_R:
                if (gameState == Constants.STATE_GAME_OVER) {
                    resetGame();
                }
                break;
            case KeyEvent.VK_L:
                levelSelectionScreen.show();
                break;
        }
        
        if (gameState == Constants.STATE_PLAYING && !paused) {
            player.keyPressed(keyCode);
        }
    }
    
    public void keyReleased(int keyCode) {
        if (gameState == Constants.STATE_PLAYING && !paused) {
            player.keyReleased(keyCode);
        }
    }
    
    private void resetGame() {
        scoreManager.reset();
        loadLevel(1);
        gameState = Constants.STATE_PLAYING;
    }
}