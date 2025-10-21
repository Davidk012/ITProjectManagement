package ai;

import entities.Entity;
import entities.Player;
import maze.Level;

public interface AIMovement {
    void move(Entity enemy, Player player, Level level);
}