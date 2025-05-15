package main.java.mazegame;

import java.awt.Point;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
    private final int rows, cols;
    private final char[][] maze;
    private final Random rand = new Random();

    public MazeGenerator(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.maze = new char[rows][cols];
    }

    public char[][] generate() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                maze[i][j] = '#';

        Stack<Point> stack = new Stack<>();
        Point start = new Point(1, 1);
        maze[start.x][start.y] = ' ';
        stack.push(start);

        while (!stack.isEmpty()) {
            Point cur = stack.peek();
            Point next = getRandomNeighbor(cur);
            if (next != null) {
                maze[(cur.x + next.x) / 2][(cur.y + next.y) / 2] = ' ';
                maze[next.x][next.y] = ' ';
                stack.push(next);
            } else {
                stack.pop();
            }
        }
        return copy(maze);
    }

    private Point getRandomNeighbor(Point p) {
        Point[] dirs = { new Point(-2,0), new Point(2,0), new Point(0,-2), new Point(0,2) };
        for (int i = dirs.length - 1; i > 0; i--) {
            int j = rand.nextInt(i+1);
            Point tmp = dirs[i]; dirs[i] = dirs[j]; dirs[j] = tmp;
        }
        for (Point d : dirs) {
            int nx = p.x + d.x, ny = p.y + d.y;
            if (nx>0 && nx<rows && ny>0 && ny<cols && maze[nx][ny]=='#')
                return new Point(nx, ny);
        }
        return null;
    }

    private char[][] copy(char[][] src) {
        char[][] dst = new char[src.length][];
        for (int i = 0; i < src.length; i++) dst[i] = src[i].clone();
        return dst;
    }
}