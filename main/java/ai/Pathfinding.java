package ai;

import entities.Entity;
import entities.Player;
import maze.Level;
import java.util.*;

public class Pathfinding implements AIMovement {
    private int moveCounter = 0;
    private int moveDelay = 15;
    
    @Override
    public void move(Entity enemy, Player player, Level level) {
        moveCounter++;
        if (moveCounter < moveDelay) return;
        
        moveCounter = 0;
        
        List<Node> path = findPath(enemy.getX(), enemy.getY(), 
                                 player.getX(), player.getY(), level);
        
        if (path != null && path.size() > 1) {
            Node nextStep = path.get(1);
            enemy.setPosition(nextStep.x, nextStep.y);
        }
    }
    
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
        
        return null; // No path found
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