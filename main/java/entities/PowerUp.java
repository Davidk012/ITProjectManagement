package entities;

import maze.Level; // Added import
import java.awt.*; // Added import

public abstract class PowerUp extends Entity {
    protected int duration;
    protected boolean active;
    protected long activationTime;
    
    public PowerUp(int x, int y, Level level, Color color, int duration) {
        super(x, y, level, color);
        this.duration = duration;
        this.active = false;
    }
    
    public abstract void applyEffect(Player player);
    public abstract void removeEffect(Player player);
    
    public void activate(Player player) {
        active = true;
        activationTime = System.currentTimeMillis();
        applyEffect(player);
    }
    
    public void update(Player player) {
        if (active && System.currentTimeMillis() - activationTime > duration) {
            removeEffect(player);
            active = false;
        }
    }
    
    public boolean isActive() { return active; }
    public boolean isExpired() { return active && System.currentTimeMillis() - activationTime > duration; }
    
    // Add getters for protected fields
    public int getDuration() { return duration; }
    public long getActivationTime() { return activationTime; }
}