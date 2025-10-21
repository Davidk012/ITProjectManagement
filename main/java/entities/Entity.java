package entities;

import maze.Level;
import java.awt.Graphics2D;
import java.awt.Color;

public abstract class Entity {
    protected int x, y;
    protected Level level;
    protected Color color;
    
    public Entity(int x, int y, Level level, Color color) {
        this.x = x;
        this.y = y;
        this.level = level;
        this.color = color;
    }
    
    public abstract void update();
    
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect(x * utils.Constants.TILE_SIZE, y * utils.Constants.TILE_SIZE,
                   utils.Constants.TILE_SIZE, utils.Constants.TILE_SIZE);
    }
    
    protected boolean isValidMove(int newX, int newY) {
        return !level.isWall(newX, newY);
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    
    // Setters
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}