package ai;

import entities.Entity;
import entities.Player;
import maze.Level;
import java.util.ArrayList;
import java.util.List;

public class PatrolBehavior implements AIMovement {
    private List<int[]> patrolPoints;
    private int currentTargetIndex = 0;
    private int moveCounter = 0;
    private int moveDelay = 12;
    
    public PatrolBehavior() {
        this.patrolPoints = new ArrayList<>();
    }
    
    @Override
    public void move(Entity enemy, Player player, Level level) {
        moveCounter++;
        if (moveCounter < moveDelay) return;
        
        moveCounter = 0;
        
        // Initialize patrol points if empty
        if (patrolPoints.isEmpty()) {
            initializePatrolPoints(enemy.getX(), enemy.getY(), level);
        }
        
        // Move towards current patrol point
        int[] target = patrolPoints.get(currentTargetIndex);
        
        if (enemy.getX() == target[0] && enemy.getY() == target[1]) {
            // Reached target, move to next point
            currentTargetIndex = (currentTargetIndex + 1) % patrolPoints.size();
            target = patrolPoints.get(currentTargetIndex);
        }
        
        // Simple movement towards target
        int dx = Integer.compare(target[0], enemy.getX());
        int dy = Integer.compare(target[1], enemy.getY());
        
        int newX = enemy.getX();
        int newY = enemy.getY();
        
        // Try horizontal movement first, then vertical
        if (dx != 0 && !level.isWall(newX + dx, newY)) {
            newX += dx;
        } else if (dy != 0 && !level.isWall(newX, newY + dy)) {
            newY += dy;
        }
        
        if (!level.isWall(newX, newY)) {
            enemy.setPosition(newX, newY);
        }
    }
    
    private void initializePatrolPoints(int startX, int startY, Level level) {
        // Create a simple square patrol route
        patrolPoints.add(new int[]{startX, startY});
        patrolPoints.add(new int[]{startX + 3, startY});
        patrolPoints.add(new int[]{startX + 3, startY + 3});
        patrolPoints.add(new int[]{startX, startY + 3});
    }
}