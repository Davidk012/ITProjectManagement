package entities;

import maze.Level; // Added import
import utils.SoundManager; // Added import
import java.awt.*; // Added import

public class InvincibilityPowerUp extends PowerUp {
    private static final Color COLOR = Color.YELLOW;
    private static final int DURATION = 3000; // 3 seconds
    
    public InvincibilityPowerUp(int x, int y, Level level) {
        super(x, y, level, COLOR, DURATION);
    }
    
    @Override
    public void applyEffect(Player player) {
        player.setInvincible(true);
        SoundManager.getInstance().playSound("powerup");
    }
    
    @Override
    public void removeEffect(Player player) {
        player.setInvincible(false);
    }
    
    @Override
    public void update() {
        // Power-up doesn't move
    }
}