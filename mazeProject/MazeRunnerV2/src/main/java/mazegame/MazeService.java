package main.java.mazegame;

import java.awt.Point;
import java.util.List;
import main.java.algorithm.*;

public class MazeService {
    private final IDao<MazeDataModel> dao;
    private final MazeGenerator generator;

    public MazeService(IDao<MazeDataModel> dao, int rows, int cols) {
        this.dao = dao;
        this.generator = new MazeGenerator(rows, cols);
    }

    public void createMaze(String id) throws Exception {
        char[][] grid = generator.generate();
        dao.save(new MazeDataModel(id, grid));
    }

    public List<Point> solveMaze(String id, String solverType) throws Exception {
        MazeDataModel model = dao.find(id);
        IMazeSolver solver = "DFS".equalsIgnoreCase(solverType)
                ? new DFSMazeSolver() : new BFSMazeSolver();
        MazeSolverContext ctx = new MazeSolverContext(solver);
        List<Point> path = ctx.solve(model.getGrid(), new Point(1,1), new Point(model.getGrid().length-2, model.getGrid()[0].length-2));
        model.setPath(path);
        if (!path.isEmpty()) {
            for (Point p : path) model.getGrid()[p.x][p.y] = '.';
            dao.update(model);
        }
        return path;
    }

    public List<String> listMazeIds() throws Exception {
        return dao.listIds();
    }
}