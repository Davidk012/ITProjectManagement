package entities;

import maze.Level; // Added import
import utils.SoundManager; // Added import
import java.awt.*; // Added import

public class SpeedBoostPowerUp extends PowerUp {
    private static final Color COLOR = Color.CYAN;
    private static final int DURATION = 5000; // 5 seconds
    
    public SpeedBoostPowerUp(int x, int y, Level level) {
        super(x, y, level, COLOR, DURATION);
    }
    
    @Override
    public void applyEffect(Player player) {
        player.setSpeedMultiplier(2.0); // Double speed
        SoundManager.getInstance().playSound("powerup");
    }
    
    @Override
    public void removeEffect(Player player) {
        player.setSpeedMultiplier(1.0); // Normal speed
    }
    
    @Override
    public void update() {
        // Power-up doesn't move
    }
}