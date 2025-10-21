package entities;

import maze.Level;
import ai.Pathfinding;
import utils.Constants;

public class PathfindingEnemy extends Enemy {
    
    public PathfindingEnemy(int x, int y, Level level) {
        super(x, y, level, new Pathfinding());
        this.color = Constants.ENEMY_PATHFINDING_COLOR;
    }
}