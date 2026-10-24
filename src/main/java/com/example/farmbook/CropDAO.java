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
                            + "notes TEXT, "
                            + "location VARCHAR, "
                            + "status VARCHAR"
                            + ")"
            );

            Statement createNeedsTable = connection.createStatement();
            createNeedsTable.execute(
                    "CREATE TABLE IF NOT EXISTS crop_needs ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "crop_id INTEGER, "
                            + "description TEXT"
                            + ")"
            );

            Statement createGrowthTable = connection.createStatement();
            createGrowthTable.execute(
                    "CREATE TABLE IF NOT EXISTS crop_growth ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "crop_id INTEGER, "
                            + "entry_date TEXT, "
                            + "description TEXT"
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
                    "INSERT INTO crops (plantName, cropType, amount, datePlanted, notes, location, status) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            insertStmt.setString(1, crop.getPlantName());
            insertStmt.setString(2, crop.getCropType());
            insertStmt.setInt(3, crop.getAmount());
            insertStmt.setString(4, crop.getDatePlanted());
            insertStmt.setString(5, crop.getNotes());
            insertStmt.setString(6, crop.getLocation());
            insertStmt.setString(7, crop.getStatus());
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
                    "UPDATE crops SET plantName = ?, cropType = ?, amount = ?, datePlanted = ?, notes = ?, location = ?, status = ? WHERE id = ?"
            );
            updateStmt.setString(1, crop.getPlantName());
            updateStmt.setString(2, crop.getCropType());
            updateStmt.setInt(3, crop.getAmount());
            updateStmt.setString(4, crop.getDatePlanted());
            updateStmt.setString(5, crop.getNotes());
            updateStmt.setString(6, crop.getLocation());
            updateStmt.setString(7, crop.getStatus());
            updateStmt.setInt(8, crop.getId());
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
            PreparedStatement deleteCropStmt = connection.prepareStatement("DELETE FROM crops WHERE id = ?");
            deleteCropStmt.setInt(1, id);
            deleteCropStmt.execute();

            PreparedStatement deleteNeedsStmt = connection.prepareStatement("DELETE FROM crop_needs WHERE crop_id = ?");
            deleteNeedsStmt.setInt(1, id);
            deleteNeedsStmt.execute();

            PreparedStatement deleteGrowthStmt = connection.prepareStatement("DELETE FROM crop_growth WHERE crop_id = ?");
            deleteGrowthStmt.setInt(1, id);
            deleteGrowthStmt.execute();
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
            Statement getAllStmt = connection.createStatement();
            ResultSet rs = getAllStmt.executeQuery("SELECT * FROM crops");
            while (rs.next()) {
                crops.add(new Crop(
                        rs.getInt("id"),
                        rs.getString("plantName"),
                        rs.getString("cropType"),
                        rs.getInt("amount"),
                        rs.getString("datePlanted"),
                        rs.getString("notes"),
                        rs.getString("location"),
                        rs.getString("status")
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

    // Sub-Table Methods

    public void addNeed(int cropId, String description) {
        try {
            PreparedStatement insertNeedStmt = connection.prepareStatement(
                    "INSERT INTO crop_needs (crop_id, description) VALUES (?, ?)"
            );
            insertNeedStmt.setInt(1, cropId);
            insertNeedStmt.setString(2, description);
            insertNeedStmt.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public List<String> getNeeds(int cropId) {
        List<String> list = new ArrayList<>();
        try {
            Statement getNeedsStmt = connection.createStatement();
            ResultSet rs = getNeedsStmt.executeQuery("SELECT description FROM crop_needs WHERE crop_id = " + cropId);
            while (rs.next()) {
                list.add(rs.getString("description"));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return list;
    }

    public void addGrowth(int cropId, String date, String description) {
        try {
            PreparedStatement insertGrowthStmt = connection.prepareStatement(
                    "INSERT INTO crop_growth (crop_id, entry_date, description) VALUES (?, ?, ?)"
            );
            insertGrowthStmt.setInt(1, cropId);
            insertGrowthStmt.setString(2, date);
            insertGrowthStmt.setString(3, description);
            insertGrowthStmt.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public List<String> getGrowth(int cropId) {
        List<String> list = new ArrayList<>();
        try {
            Statement getGrowthStmt = connection.createStatement();
            ResultSet rs = getGrowthStmt.executeQuery("SELECT entry_date, description FROM crop_growth WHERE crop_id = " + cropId + " ORDER BY entry_date DESC");
            while (rs.next()) {
                list.add(rs.getString("entry_date") + ": " + rs.getString("description"));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return list;
    }
}