package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimsAlgorithm extends MazeGenerator {
    
    public PrimsAlgorithm(int width, int height) {
        super(width, height);
    }
    
    @Override
    public int[][] generateMaze() {
        // Start with a grid full of walls
        List<int[]> frontiers = new ArrayList<>();
        
        // Start from a random cell
        int startX = 1;
        int startY = 1;
        setPath(startX, startY);
        
        // Add frontier cells
        addFrontiers(startX, startY, frontiers);
        
        while (!frontiers.isEmpty()) {
            // Pick a random frontier cell
            int randomIndex = random.nextInt(frontiers.size());
            int[] frontier = frontiers.remove(randomIndex);
            int x = frontier[0];
            int y = frontier[1];
            
            // Get neighbors that are already paths
            List<int[]> neighbors = getPathNeighbors(x, y);
            if (!neighbors.isEmpty()) {
                // Connect to a random neighbor
                int[] neighbor = neighbors.get(random.nextInt(neighbors.size()));
                int wallX = (x + neighbor[0]) / 2;
                int wallY = (y + neighbor[1]) / 2;
                setPath(wallX, wallY);
                setPath(x, y);
                
                // Add new frontiers
                addFrontiers(x, y, frontiers);
            }
        }
        
        // Create entrance and exit
        setPath(1, 0);
        setPath(width - 2, height - 1);
        
        return maze;
    }
    
    private void addFrontiers(int x, int y, List<int[]> frontiers) {
        int[][] directions = {{0, -2}, {2, 0}, {0, 2}, {-2, 0}};
        
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            
            if (isValid(newX, newY) && isWall(newX, newY)) {
                frontiers.add(new int[]{newX, newY});
            }
        }
    }
    
    private List<int[]> getPathNeighbors(int x, int y) {
        List<int[]> neighbors = new ArrayList<>();
        int[][] directions = {{0, -2}, {2, 0}, {0, 2}, {-2, 0}};
        
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            
            if (isValid(newX, newY) && !isWall(newX, newY)) {
                neighbors.add(new int[]{newX, newY});
            }
        }
        
        return neighbors;
    }
}
