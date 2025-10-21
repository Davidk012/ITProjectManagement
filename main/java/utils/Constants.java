package utils;

import java.awt.Color;

public class Constants {
    // Window settings
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final String GAME_TITLE = "2D Maze Game";
    
    // Game settings
    public static final int TILE_SIZE = 40;
    public static final int FPS = 60;
    public static final long FRAME_TIME = 1000 / FPS;
    
    // Colors
    public static final Color WALL_COLOR = Color.DARK_GRAY;
    public static final Color PATH_COLOR = Color.WHITE;
    public static final Color PLAYER_COLOR = Color.BLUE;
    public static final Color EXIT_COLOR = Color.GREEN;
    public static final Color ENEMY_RANDOM_COLOR = Color.RED;
    public static final Color ENEMY_PATHFINDING_COLOR = Color.ORANGE;
    public static final Color ENEMY_PATROL_COLOR = Color.MAGENTA;
    public static final Color ENEMY_ADVANCED_COLOR = Color.PINK;
    
    // Game states
    public static final int STATE_MENU = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_GAME_OVER = 2;
    public static final int STATE_LEVEL_COMPLETE = 3;
}