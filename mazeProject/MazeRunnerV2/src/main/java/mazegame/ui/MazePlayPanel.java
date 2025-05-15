package main.java.mazegame.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class MazePlayPanel extends JPanel {
    public interface MazeWinListener { void onWin(); }

    private final char[][] maze;
    private final int startX, startY, endX, endY;
    private int playerX, playerY;
    private List<Point> solutionPath;
    private final MazeWinListener listener;

    public MazePlayPanel(char[][] maze,
                         int startX, int startY,
                         int endX, int endY,
                         MazeWinListener listener) {
        this.maze = maze;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.playerX = startX;
        this.playerY = startY;
        this.listener = listener;

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                int nx = playerX, ny = playerY;
                switch (ke.getKeyCode()) {
                    case KeyEvent.VK_UP:    nx--; break;
                    case KeyEvent.VK_DOWN:  nx++; break;
                    case KeyEvent.VK_LEFT:  ny--; break;
                    case KeyEvent.VK_RIGHT: ny++; break;
                    default: return;
                }
                if (nx >= 0 && nx < maze.length
                        && ny >= 0 && ny < maze[0].length
                        && maze[nx][ny] != '#') {
                    playerX = nx;
                    playerY = ny;
                    repaint();
                    if (playerX == endX && playerY == endY) {
                        listener.onWin();
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int rows = maze.length, cols = maze[0].length;
        int cellW = getWidth() / cols, cellH = getHeight() / rows;

        // ציור המבוך
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g.setColor(maze[i][j] == '#' ? Color.BLACK : Color.WHITE);
                g.fillRect(j*cellW, i*cellH, cellW, cellH);
            }
        }

        // ציור נתיב הפתרון (אם קיים)
        if (solutionPath != null) {
            g.setColor(Color.RED);
            for (Point p : solutionPath) {
                g.fillOval(
                        p.y * cellW + cellW/4,
                        p.x * cellH + cellH/4,
                        cellW/2, cellH/2
                );
            }
        }

        // ציור השחקן
        g.setColor(Color.BLUE);
        g.fillOval(
                playerY * cellW + cellW/4,
                playerX * cellH + cellH/4,
                cellW/2, cellH/2
        );
    }

    // getters לצורך השימוש ב-Frame
    public char[][] getMaze() { return maze; }
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    public int getEndX() { return endX; }
    public int getEndY() { return endY; }

    // setter לנתיב הפתרון
    public void setSolutionPath(List<Point> path) {
        this.solutionPath = path;
        repaint();
    }
}
