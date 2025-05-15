package main.java.mazegame;

import java.util.List;

/**
 * ממשק CRUD כללי ל-DataModel
 */
public interface IDao<T> {
    void save(T model) throws Exception;
    T find(String id) throws Exception;
    void update(T model) throws Exception;
    void delete(String id) throws Exception;
    List<String> listIds() throws Exception;
}