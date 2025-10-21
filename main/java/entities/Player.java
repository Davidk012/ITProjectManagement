package entities;

import maze.Level;
import utils.Constants;
import java.awt.event.KeyEvent;
import java.awt.Graphics2D; // Added import
import java.awt.Color; // Added import

public class Player extends Entity {
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private int startX, startY;
    private double speedMultiplier;
    private boolean invincible;
    private long invincibleUntil;
    
    public Player(int x, int y, Level level) {
        super(x, y, level, Constants.PLAYER_COLOR);
        this.startX = x;
        this.startY = y;
        this.speedMultiplier = 1.0;
        this.invincible = false;
    }
    
    @Override
    public void update() {
        if (invincible && System.currentTimeMillis() > invincibleUntil) {
            invincible = false;
        }
        
        int moves = (int) speedMultiplier;
        for (int i = 0; i < moves; i++) {
            int newX = x;
            int newY = y;
            
            if (upPressed) newY--;
            if (downPressed) newY++;
            if (leftPressed) newX--;
            if (rightPressed) newX++;
            
            if (isValidMove(newX, newY)) {
                x = newX;
                y = newY;
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        if (invincible) {
            // Flash effect when invincible
            if ((System.currentTimeMillis() / 200) % 2 == 0) {
                g2d.setColor(Color.WHITE);
            } else {
                g2d.setColor(Constants.PLAYER_COLOR);
            }
        } else {
            g2d.setColor(color);
        }
        
        g2d.fillRect(x * utils.Constants.TILE_SIZE, y * utils.Constants.TILE_SIZE,
                   utils.Constants.TILE_SIZE, utils.Constants.TILE_SIZE);
    }
    
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
        }
    }
    
    public void keyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
        }
    }
    
    public void resetPosition() {
        x = startX;
        y = startY;
        speedMultiplier = 1.0;
        invincible = false;
    }
    
    // Getters and setters for power-ups
    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }
    
    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
        if (invincible) {
            this.invincibleUntil = System.currentTimeMillis() + 3000;
        }
    }
    
    public boolean isInvincible() { return invincible; }
}