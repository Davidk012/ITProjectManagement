package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean running = false;
    private GameManager gameManager;
    
    public GamePanel() {
        setPreferredSize(new Dimension(
            utils.Constants.WINDOW_WIDTH, 
            utils.Constants.WINDOW_HEIGHT
        ));
        setFocusable(true);
        addKeyListener(this);
        
        gameManager = new GameManager();
    }
    
    public void startGame() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
            running = true;
        }
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerFrame = 1000000000.0 / utils.Constants.FPS;
        double delta = 0;
        
        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / nsPerFrame;
            lastTime = currentTime;
            
            while (delta >= 1) {
                update();
                delta--;
            }
            
            repaint();
            
            try {
                Thread.sleep(utils.Constants.FRAME_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void update() {
        gameManager.update();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        gameManager.draw(g2d);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        gameManager.keyPressed(e.getKeyCode());
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        gameManager.keyReleased(e.getKeyCode());
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
}