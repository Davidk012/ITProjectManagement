package entities;

import maze.Level;
import ai.PatrolBehavior;
import utils.Constants;

public class PatrolEnemy extends Enemy {
    
    public PatrolEnemy(int x, int y, Level level) {
        super(x, y, level, new PatrolBehavior());
        this.color = Constants.ENEMY_PATROL_COLOR;
    }
}