package com.example.farmbook;

import java.util.List;

public interface ICropDAO {
    void createTable();
    void insert(Crop crop);
    void update(Crop crop);
    void delete(int id);
    List<Crop> getAll();
    Crop getById(int id);
    void close();
}
