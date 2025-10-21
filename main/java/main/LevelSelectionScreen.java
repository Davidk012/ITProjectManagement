package main;

import utils.Constants;
import utils.ScoreManager;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelSelectionScreen {
    private GameManager gameManager;
    private ScoreManager scoreManager;
    private int selectedLevel;
    private boolean visible;
    
    public LevelSelectionScreen(GameManager gameManager, ScoreManager scoreManager) {
        this.gameManager = gameManager;
        this.scoreManager = scoreManager;
        this.selectedLevel = 1;
        this.visible = false;
    }
    
    public void draw(Graphics2D g2d) {
        if (!visible) return;
        
        // Semi-transparent background
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        
        // Title
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString("Select Level", Constants.WINDOW_WIDTH / 2 - 100, 80);
        
        // Level buttons
        for (int i = 1; i <= 10; i++) {
            int x = 100 + (i % 5) * 120;
            int y = 150 + (i / 5) * 120;
            
            // Check if level is unlocked
            boolean unlocked = i <= scoreManager.getMaxUnlockedLevel();
            
            if (unlocked) {
                g2d.setColor(i == selectedLevel ? Color.YELLOW : Color.BLUE);
            } else {
                g2d.setColor(Color.GRAY);
            }
            
            g2d.fillRoundRect(x, y, 80, 80, 20, 20);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("" + i, x + 35, y + 45);
            
            if (!unlocked) {
                g2d.setColor(Color.RED);
                g2d.drawString("🔒", x + 60, y + 25);
            }
        }
        
        // Instructions
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.drawString("Press ENTER to start level", Constants.WINDOW_WIDTH / 2 - 100, 500);
        g2d.drawString("Press ESC to return to menu", Constants.WINDOW_WIDTH / 2 - 100, 530);
    }
    
    public void keyPressed(int keyCode) {
        if (!visible) return;
        
        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (selectedLevel > 5) selectedLevel -= 5;
                break;
            case KeyEvent.VK_DOWN:
                if (selectedLevel <= 5) selectedLevel += 5;
                break;
            case KeyEvent.VK_LEFT:
                if (selectedLevel > 1) selectedLevel--;
                break;
            case KeyEvent.VK_RIGHT:
                if (selectedLevel < 10) selectedLevel++;
                break;
            case KeyEvent.VK_ENTER:
                if (selectedLevel <= scoreManager.getMaxUnlockedLevel()) {
                    gameManager.loadLevel(selectedLevel);
                    hide();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                hide();
                break;
        }
    }
    
    public void show() { visible = true; selectedLevel = 1; }
    public void hide() { visible = false; }
    public boolean isVisible() { return visible; }
}