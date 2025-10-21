package entities;

import maze.Level;
import ai.RandomMovement;
import utils.Constants;

public class RandomEnemy extends Enemy {
    
    public RandomEnemy(int x, int y, Level level) {
        super(x, y, level, new RandomMovement());
        this.color = Constants.ENEMY_RANDOM_COLOR;
    }
}