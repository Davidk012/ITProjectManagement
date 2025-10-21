package ai;

import entities.Entity;
import entities.Player;
import maze.Level;
import java.util.Random;

public class RandomMovement implements AIMovement {
    private Random random = new Random();
    private int moveCounter = 0;
    private int moveDelay = 10; // Move every 10 frames
    
    @Override
    public void move(Entity enemy, Player player, Level level) {
        moveCounter++;
        if (moveCounter < moveDelay) return;
        
        moveCounter = 0;
        
        int direction = random.nextInt(4);
        int newX = enemy.getX();
        int newY = enemy.getY();
        
        switch (direction) {
            case 0: newY--; break; // Up
            case 1: newY++; break; // Down
            case 2: newX--; break; // Left
            case 3: newX++; break; // Right
        }
        
        if (!level.isWall(newX, newY)) {
            enemy.setPosition(newX, newY);
        }
    }
}