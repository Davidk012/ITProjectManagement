package entities;

import maze.Level;
import ai.PredictiveBehavior; // Added import
import utils.Constants;

public class PredictiveHunter extends Enemy {
    
    public PredictiveHunter(int x, int y, Level level) {
        super(x, y, level, new PredictiveBehavior());
        this.color = Constants.ENEMY_ADVANCED_COLOR;
    }
}