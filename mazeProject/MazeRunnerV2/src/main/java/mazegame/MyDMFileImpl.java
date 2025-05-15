package main.java.mazegame;

import java.io.*;
import java.util.*;

/**
 * מימוש IDao לשמירה/קריאה מקובץ אחד (Object stream)
 */
public class MyDMFileImpl implements IDao<MazeDataModel> {
    private final File dataFile;

    public MyDMFileImpl(String pathFile) throws IOException {
        this.dataFile = new File(pathFile);
        if (!dataFile.exists()) {
            dataFile.createNewFile();
            saveMap(new HashMap<>());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, MazeDataModel> loadMap() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(dataFile)))) {
            return (Map<String, MazeDataModel>) in.readObject();
        }
    }

    private void saveMap(Map<String, MazeDataModel> map) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(dataFile)))) {
            out.writeObject(map);
        }
    }

    @Override
    public void save(MazeDataModel model) throws Exception {
        Map<String, MazeDataModel> map = loadMap();
        if (map.containsKey(model.getId())) {
            throw new IllegalArgumentException("ID already exists: " + model.getId());
        }
        map.put(model.getId(), model);
        saveMap(map);
    }

    @Override
    public MazeDataModel find(String id) throws Exception {
        return loadMap().get(id);
    }

    @Override
    public void update(MazeDataModel model) throws Exception {
        Map<String, MazeDataModel> map = loadMap();
        if (!map.containsKey(model.getId())) {
            throw new IllegalArgumentException("ID not found: " + model.getId());
        }
        map.put(model.getId(), model);
        saveMap(map);
    }

    @Override
    public void delete(String id) throws Exception {
        Map<String, MazeDataModel> map = loadMap();
        map.remove(id);
        saveMap(map);
    }

    @Override
    public List<String> listIds() throws Exception {
        return new ArrayList<>(loadMap().keySet());
    }
}