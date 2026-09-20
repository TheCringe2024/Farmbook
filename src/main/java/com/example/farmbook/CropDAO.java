package com.example.farmbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * data access object for handling operations related to Crops
 * methods to create the table, insert, read, update, delete
 */
public class CropDAO implements ICropDAO {
    private Connection connection;
    /**
     * Establishes a connection to the SQLite db using the singleton SqliteConnection instance.
     */
    public CropDAO() {
        connection = SqliteConnection.getInstance();
    }
    /**
     * Creates the crops table in the database if it does not already exist.
     */
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

    /**
     * Inserts a new Crop record into the database
     * @param crop Crop object containing the data to insert
     */
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
    /**
     * Updates existing Crop record in the database based on its ID
     * @param crop the object containing the updated data
     */
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

    /**
     * Deletes a Crop record from the database
     * @param id the unique identifier of the crop to delete
     */
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

    /**
     * Retrieves all Crop records from the database
     * @return A List of Crop objects representing all rows in the crops table
     */
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

    /**
     * Retrieves a specific Crop record by its ID
     * @param id unique identifier of  crop
     * @return if found, or null if it does not exist
     */
    @Override
    public Crop getById(int id) {
        return null;
    }
    /**
     * closes the connection to the database
     */
    @Override
    public void close() {
        try {
            if(connection != null) connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}