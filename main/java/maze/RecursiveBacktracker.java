package maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class RecursiveBacktracker extends MazeGenerator {
    
    public RecursiveBacktracker(int width, int height) {
        super(width, height);
    }
    
    @Override
    public int[][] generateMaze() {
        // Start from a random cell
        int startX = 1;
        int startY = 1;
        setPath(startX, startY);
        
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startY});
        
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int x = current[0];
            int y = current[1];
            
            List<int[]> neighbors = getUnvisitedNeighbors(x, y);
            
            if (!neighbors.isEmpty()) {
                Collections.shuffle(neighbors, random);
                int[] next = neighbors.get(0);
                
                // Remove wall between current and next
                int wallX = (x + next[0]) / 2;
                int wallY = (y + next[1]) / 2;
                setPath(wallX, wallY);
                setPath(next[0], next[1]);
                
                stack.push(next);
            } else {
                stack.pop();
            }
        }
        
        // Create entrance and exit
        setPath(1, 0); // Entrance
        setPath(width - 2, height - 1); // Exit
        
        return maze;
    }
    
    private List<int[]> getUnvisitedNeighbors(int x, int y) {
        List<int[]> neighbors = new ArrayList<>();
        
        // Check all directions (2 steps away)
        int[][] directions = {{0, -2}, {2, 0}, {0, 2}, {-2, 0}};
        
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            
            if (isValid(newX, newY) && isWall(newX, newY)) {
                neighbors.add(new int[]{newX, newY});
            }
        }
        
        return neighbors;
    }
}