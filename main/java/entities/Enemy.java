package entities;

import maze.Level;
import ai.AIMovement;

public abstract class Enemy extends Entity {
    protected AIMovement movementBehavior;
    
    public Enemy(int x, int y, Level level, AIMovement movementBehavior) {
        super(x, y, level, null); // Color will be set by subclasses
        this.movementBehavior = movementBehavior;
    }
    
    @Override
    public void update() {
        // Movement behavior will be implemented by subclasses
    }
    
    public void update(Player player) {
        movementBehavior.move(this, player, level);
    }
}