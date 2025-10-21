package maze;

import entities.Player;
import entities.Enemy;
import entities.RandomEnemy;
import entities.PathfindingEnemy;
import entities.PatrolEnemy;
import entities.PredictiveHunter;
import entities.PowerUp;
import entities.SpeedBoostPowerUp;
import entities.InvincibilityPowerUp;
import utils.Constants;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {
    private int[][] maze;
    private Player player;
    private List<Enemy> enemies;
    private List<PowerUp> powerUps;
    private Point exit;
    private int levelNumber;
    
    public Level(int levelNumber, int[][] maze, Point playerStart, Point exit) {
        this.levelNumber = levelNumber;
        this.maze = maze;
        this.exit = exit;
        this.enemies = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        
        // Create player
        this.player = new Player(playerStart.x, playerStart.y, this);
        
        // Add enemies based on level
        initializeEnemies();
        
        // Add power-ups
        initializePowerUps();
    }
    
    private void initializeEnemies() {
        // Add enemies based on level number
        switch (levelNumber) {
            case 1:
                enemies.add(new RandomEnemy(5, 5, this));
                break;
            case 2:
                enemies.add(new RandomEnemy(5, 5, this));
                enemies.add(new PathfindingEnemy(10, 10, this));
                break;
            case 3:
                enemies.add(new RandomEnemy(5, 5, this));
                enemies.add(new PathfindingEnemy(10, 10, this));
                enemies.add(new PatrolEnemy(15, 15, this));
                break;
            case 4:
                enemies.add(new RandomEnemy(5, 5, this));
                enemies.add(new PathfindingEnemy(10, 10, this));
                enemies.add(new PatrolEnemy(15, 15, this));
                enemies.add(new PredictiveHunter(8, 8, this));
                break;
            default:
                // For levels beyond 4, add more enemies
                for (int i = 0; i < levelNumber; i++) {
                    int type = i % 4;
                    switch (type) {
                        case 0: enemies.add(new RandomEnemy(3 + i*2, 3 + i*2, this)); break;
                        case 1: enemies.add(new PathfindingEnemy(5 + i*2, 5 + i*2, this)); break;
                        case 2: enemies.add(new PatrolEnemy(7 + i*2, 7 + i*2, this)); break;
                        case 3: enemies.add(new PredictiveHunter(6 + i*2, 6 + i*2, this)); break;
                    }
                }
        }
    }
    
    private void initializePowerUps() {
        Random random = new Random();
        int powerUpCount = levelNumber + 1; // More power-ups in higher levels
        
        for (int i = 0; i < powerUpCount; i++) {
            Point pos = findValidPositionForPowerUp();
            if (pos != null) {
                if (random.nextBoolean()) {
                    powerUps.add(new SpeedBoostPowerUp(pos.x, pos.y, this));
                } else {
                    powerUps.add(new InvincibilityPowerUp(pos.x, pos.y, this));
                }
            }
        }
    }
    
    private Point findValidPositionForPowerUp() {
        Random random = new Random();
        int attempts = 0;
        while (attempts < 100) {
            int x = random.nextInt(maze[0].length);
            int y = random.nextInt(maze.length);
            
            // Check if position is valid (not wall, not player start, not exit, not enemy)
            if (!isWall(x, y) && 
                !(x == player.getX() && y == player.getY()) &&
                !(x == exit.x && y == exit.y) &&
                !isEnemyAt(x, y)) {
                return new Point(x, y);
            }
            attempts++;
        }
        return null;
    }
    
    private boolean isEnemyAt(int x, int y) {
        for (Enemy enemy : enemies) {
            if (enemy.getX() == x && enemy.getY() == y) {
                return true;
            }
        }
        return false;
    }
    
    public void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.update(player);
        }
    }
    
    public void draw(Graphics2D g2d) {
        // Draw maze
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                if (maze[y][x] == 1) { // Wall
                    g2d.setColor(Constants.WALL_COLOR);
                } else { // Path
                    g2d.setColor(Constants.PATH_COLOR);
                }
                g2d.fillRect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, 
                           Constants.TILE_SIZE, Constants.TILE_SIZE);
            }
        }
        
        // Draw exit
        g2d.setColor(Constants.EXIT_COLOR);
        g2d.fillRect(exit.x * Constants.TILE_SIZE, exit.y * Constants.TILE_SIZE,
                   Constants.TILE_SIZE, Constants.TILE_SIZE);
        
        // Draw power-ups
        for (PowerUp powerUp : powerUps) {
            if (!powerUp.isActive()) { // Only draw if not collected
                powerUp.draw(g2d);
            }
        }
        
        // Draw player
        player.draw(g2d);
        
        // Draw enemies
        for (Enemy enemy : enemies) {
            enemy.draw(g2d);
        }
    }
    
    public boolean isWall(int x, int y) {
        if (x < 0 || x >= maze[0].length || y < 0 || y >= maze.length) {
            return true;
        }
        return maze[y][x] == 1;
    }
    
    public boolean isLevelComplete(Player player) {
        return player.getX() == exit.x && player.getY() == exit.y;
    }
    
    public boolean checkEnemyCollisions(Player player) {
        for (Enemy enemy : enemies) {
            if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
                return true;
            }
        }
        return false;
    }
    
    // New method to check power-up collisions
    public PowerUp checkPowerUpCollisions(Player player) {
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp powerUp = powerUps.get(i);
            if (!powerUp.isActive() && 
                powerUp.getX() == player.getX() && 
                powerUp.getY() == player.getY()) {
                powerUps.remove(i);
                return powerUp;
            }
        }
        return null;
    }
    
    public int[][] getMaze() { return maze; }
    public Player getPlayer() { return player; }
    public int getLevelNumber() { return levelNumber; }
}