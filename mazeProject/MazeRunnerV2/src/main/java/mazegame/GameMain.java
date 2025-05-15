// GameMain.java
package main.java.mazegame;

import java.awt.Point;
import java.util.List;

public class GameMain {
    public static void main(String[] args) throws Exception {
        String dataSourceFile = "txt.DataSource";
        MyDMFileImpl dao = new MyDMFileImpl(dataSourceFile);
        MazeService service = new MazeService(dao, 21, 21);

        String id = "level1";
        // 1. אם אין את ה-id כבר — צור, אחרת דלג
        if (!service.listMazeIds().contains(id)) {
            service.createMaze(id);
        } else {
            dao.delete(id);
            service.createMaze(id);
            System.out.println("Re-created maze: " + id);
        }


        // 2. פתרון מבוך על אותו id
        String solverType = args.length > 0 ? args[0] : "BFS";
        List<Point> path = service.solveMaze(id, solverType);
        System.out.println("Solved maze with " + solverType + ", path length: " + path.size());

        // 3. הדפסת המבוך המעודכן
        MazeDataModel model = dao.find(id);
        char[][] grid = model.getGrid();
        System.out.println("\n-- Maze [" + solverType + "] --");
        for (char[] row : grid) {
            System.out.println(new String(row));
        }
    }
}
