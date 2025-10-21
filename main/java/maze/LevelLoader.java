package maze;

import java.awt.Point;
import java.util.Random;

public class LevelLoader {
    private static final int BASE_WIDTH = 15;
    private static final int BASE_HEIGHT = 15;
    
    public static Level loadLevel(int levelNumber) {
        Random random = new Random();
        
        // Increase maze size with level
        int width = BASE_WIDTH + levelNumber * 2;
        int height = BASE_HEIGHT + levelNumber * 2;
        
        // Choose maze generation algorithm based on level
        MazeGenerator generator;
        if (levelNumber % 2 == 0) {
            generator = new PrimsAlgorithm(width, height);
        } else {
            generator = new RecursiveBacktracker(width, height);
        }
        
        int[][] maze = generator.generateMaze();
        
        // Find valid start and exit positions
        Point start = findValidPosition(maze, true);
        Point exit = findValidPosition(maze, false);
        
        return new Level(levelNumber, maze, start, exit);
    }
    
    private static Point findValidPosition(int[][] maze, boolean nearTop) {
        Random random = new Random();
        int x, y;
        
        do {
            if (nearTop) {
                x = random.nextInt(maze[0].length - 4) + 2;
                y = random.nextInt(maze.length / 3) + 1;
            } else {
                x = random.nextInt(maze[0].length - 4) + 2;
                y = maze.length - 2 - random.nextInt(maze.length / 3);
            }
        } while (maze[y][x] == 1); // Keep looking if it's a wall
        
        return new Point(x, y);
    }
}