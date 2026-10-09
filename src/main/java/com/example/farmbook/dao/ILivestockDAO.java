package com.example.farmbook.dao;

import com.example.farmbook.model.Livestock;

import java.util.List;

/**
 * Defines persistence operations for livestock records.
 *
 * Separating the interface from the concrete SQLite implementation
 * reduces coupling and allows the service layer to be tested without
 * requiring a real database.
 */
public interface ILivestockDAO {

    boolean save(Livestock livestock);

    List<Livestock> findAll();

    void delete(int id);
}