package maze;

import java.util.Random;

public abstract class MazeGenerator {
    protected int width, height;
    protected Random random;
    protected int[][] maze;
    
    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.random = new Random();
        this.maze = new int[height][width];
        initializeMaze();
    }
    
    protected void initializeMaze() {
        // Initialize all as walls
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                maze[y][x] = 1; // 1 represents wall
            }
        }
    }
    
    public abstract int[][] generateMaze();
    
    protected boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    protected boolean isWall(int x, int y) {
        return isValid(x, y) && maze[y][x] == 1;
    }
    
    protected void setPath(int x, int y) {
        if (isValid(x, y)) {
            maze[y][x] = 0; // 0 represents path
        }
    }
}