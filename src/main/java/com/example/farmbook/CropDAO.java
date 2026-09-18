package com.example.farmbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CropDAO implements ICropDAO {
    private Connection connection;

    public CropDAO() {
        connection = SqliteConnection.getInstance();
    }

    @Override
    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS crops ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "plantName VARCHAR NOT NULL, "
                            + "cropType VARCHAR, "
                            + "amount INTEGER, "
                            + "datePlanted VARCHAR, "
                            + "notes TEXT"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void insert(Crop crop) {
        try {
            PreparedStatement insertStmt = connection.prepareStatement(
                    "INSERT INTO crops (plantName, cropType, amount, datePlanted, notes) VALUES (?, ?, ?, ?, ?)"
            );
            insertStmt.setString(1, crop.getPlantName());
            insertStmt.setString(2, crop.getCropType());
            insertStmt.setInt(3, crop.getAmount());
            insertStmt.setString(4, crop.getDatePlanted());
            insertStmt.setString(5, crop.getNotes());
            insertStmt.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void update(Crop crop) {
        try {
            PreparedStatement updateStmt = connection.prepareStatement(
                    "UPDATE crops SET plantName = ?, cropType = ?, amount = ?, datePlanted = ?, notes = ? WHERE id = ?"
            );
            updateStmt.setString(1, crop.getPlantName());
            updateStmt.setString(2, crop.getCropType());
            updateStmt.setInt(3, crop.getAmount());
            updateStmt.setString(4, crop.getDatePlanted());
            updateStmt.setString(5, crop.getNotes());
            updateStmt.setInt(6, crop.getId());
            updateStmt.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM crops WHERE id = ?");
            deleteStmt.setInt(1, id);
            deleteStmt.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public List<Crop> getAll() {
        List<Crop> crops = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT * FROM crops");
            while (rs.next()) {
                crops.add(new Crop(
                        rs.getInt("id"),
                        rs.getString("plantName"),
                        rs.getString("cropType"),
                        rs.getInt("amount"),
                        rs.getString("datePlanted"),
                        rs.getString("notes")
                ));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return crops;
    }

    @Override
    public Crop getById(int id) {
        return null;
    }

    @Override
    public void close() {
        try {
            if(connection != null) connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}