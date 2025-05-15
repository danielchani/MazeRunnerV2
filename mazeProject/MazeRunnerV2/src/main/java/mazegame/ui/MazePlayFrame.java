package main.java.mazegame.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.Point;

import main.java.mazegame.MazeGenerator;
import main.java.algorithm.BFSMazeSolver;
import main.java.algorithm.DFSMazeSolver;
import main.java.algorithm.MazeSolverContext;

public class MazePlayFrame extends JFrame implements MazePlayPanel.MazeWinListener {
    private int level = 1;
    private int rows, cols;
    private MazePlayPanel playPanel;
    private JPanel controlPanel;
    private JComboBox<String> algoSelect;

    public MazePlayFrame(int initialRows, int initialCols) {
        super("Maze Game – Level 1");
        this.rows = initialRows;
        this.cols = initialCols;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 650);
        setLayout(new BorderLayout());
        initControls();
        newLevel();
    }

    private void initControls() {
        controlPanel = new JPanel();
        algoSelect = new JComboBox<>(new String[]{"BFS", "DFS"});
        JButton solveBtn   = new JButton("Show Solution");
        JButton restartBtn = new JButton("Restart Level");

        controlPanel.add(new JLabel("Solver:"));
        controlPanel.add(algoSelect);
        controlPanel.add(solveBtn);
        controlPanel.add(restartBtn);
        add(controlPanel, BorderLayout.SOUTH);

        solveBtn.addActionListener(ignored -> {
            MazeSolverContext ctx = new MazeSolverContext(
                    "DFS".equals(algoSelect.getSelectedItem())
                            ? new DFSMazeSolver()
                            : new BFSMazeSolver()
            );
            List<Point> path = ctx.solve(
                    playPanel.getMaze(),
                    new Point(playPanel.getStartX(), playPanel.getStartY()),
                    new Point(playPanel.getEndX(),   playPanel.getEndY())
            );
            playPanel.setSolutionPath(path);
        });

        restartBtn.addActionListener(ignored -> newLevel());
    }

    private void newLevel() {
        setTitle(String.format("Maze Game – Level %d (%dx%d)", level, rows, cols));

        // יצירת מבוך חדש עם פתחים
        char[][] maze = new MazeGenerator(rows, cols).generate();
        maze[1][0]           = ' '; // entrance
        maze[rows-2][cols-1] = ' '; // exit

        if (playPanel != null) {
            remove(playPanel);
        }
        playPanel = new MazePlayPanel(
                maze,
                1, 0,
                rows-2, cols-1,
                this
        );
        add(playPanel, BorderLayout.CENTER);
        revalidate();
        playPanel.requestFocusInWindow();
    }

    @Override
    public void onWin() {
        int opt = JOptionPane.showConfirmDialog(
                this,
                "Well done! You completed level " + level + ".\nNext level?",
                "Level Complete",
                JOptionPane.YES_NO_OPTION
        );
        if (opt == JOptionPane.YES_OPTION) {
            level++;
            rows += 2;
            cols += 2;
            newLevel();
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MazePlayFrame frame = new MazePlayFrame(21, 41);
            frame.setVisible(true);
        });
    }
}
