package ai;

import entities.Entity;
import entities.Player;
import maze.Level;
import java.util.*;

public class PredictiveBehavior implements AIMovement {
    private int moveCounter = 0;
    private int moveDelay = 12;
    private List<int[]> playerHistory;
    private final int HISTORY_SIZE = 5;
    
    public PredictiveBehavior() {
        playerHistory = new ArrayList<>();
    }
    
    @Override
    public void move(Entity enemy, Player player, Level level) {
        moveCounter++;
        if (moveCounter < moveDelay) return;
        
        moveCounter = 0;
        
        // Record player position
        playerHistory.add(0, new int[]{player.getX(), player.getY()});
        if (playerHistory.size() > HISTORY_SIZE) {
            playerHistory.remove(playerHistory.size() - 1);
        }
        
        // Predict player's next position
        int[] predictedPos = predictPlayerPosition();
        
        // Use A* to move toward predicted position
        List<Node> path = findPath(enemy.getX(), enemy.getY(), 
                                 predictedPos[0], predictedPos[1], level);
        
        if (path != null && path.size() > 1) {
            Node nextStep = path.get(1);
            enemy.setPosition(nextStep.x, nextStep.y);
        }
    }
    
    private int[] predictPlayerPosition() {
        if (playerHistory.size() < 2) {
            return new int[]{playerHistory.get(0)[0], playerHistory.get(0)[1]};
        }
        
        // Calculate movement trend
        int dx = 0, dy = 0;
        for (int i = 0; i < playerHistory.size() - 1; i++) {
            int[] current = playerHistory.get(i);
            int[] previous = playerHistory.get(i + 1);
            dx += current[0] - previous[0];
            dy += current[1] - previous[1];
        }
        
        // Predict next position based on trend
        int[] lastPos = playerHistory.get(0);
        int predictedX = lastPos[0] + dx;
        int predictedY = lastPos[1] + dy;
        
        return new int[]{predictedX, predictedY};
    }
    
    // A* pathfinding implementation
    private List<Node> findPath(int startX, int startY, int targetX, int targetY, Level level) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();
        
        Node startNode = new Node(startX, startY);
        Node targetNode = new Node(targetX, targetY);
        
        openSet.add(startNode);
        
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            closedSet.add(current);
            
            if (current.equals(targetNode)) {
                return reconstructPath(current);
            }
            
            for (Node neighbor : getNeighbors(current, level)) {
                if (closedSet.contains(neighbor)) continue;
                
                int newCost = current.gCost + 1;
                if (newCost < neighbor.gCost || !openSet.contains(neighbor)) {
                    neighbor.gCost = newCost;
                    neighbor.hCost = heuristic(neighbor, targetNode);
                    neighbor.parent = current;
                    
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        
        return null;
    }
    
    private List<Node> getNeighbors(Node node, Level level) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        
        for (int[] dir : directions) {
            int newX = node.x + dir[0];
            int newY = node.y + dir[1];
            
            if (!level.isWall(newX, newY)) {
                neighbors.add(new Node(newX, newY));
            }
        }
        
        return neighbors;
    }
    
    private int heuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
    
    private List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(0, node);
            node = node.parent;
        }
        return path;
    }
    
    private class Node implements Comparable<Node> {
        int x, y;
        int gCost, hCost;
        Node parent;
        
        Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.gCost = Integer.MAX_VALUE;
        }
        
        int fCost() { return gCost + hCost; }
        
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.fCost(), other.fCost());
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return x == node.x && y == node.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}