package com.example.farmbook.dao;

import com.example.farmbook.model.Crop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads crop records from the database.
 */
public class CropDAO {

    /**
     * Saves a new crop record.
     * @param crop the crop to save
     */
    public void save(Crop crop) {
        String sql = "INSERT INTO crops (type, field_plot, quantity, date_planted) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, crop.getType());
            ps.setString(2, crop.getFieldPlot());
            ps.setInt(3, crop.getQuantity());
            ps.setString(4, crop.getDatePlanted());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save crop: " + e.getMessage());
        }
    }

    /**
     * Returns all saved crops.
     * @return list of every crop record
     */
    public List<Crop> findAll() {
        List<Crop> crops = new ArrayList<>();
        String sql = "SELECT * FROM crops";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Crop crop = new Crop(
                        rs.getString("type"),
                        rs.getString("field_plot"),
                        rs.getInt("quantity"),
                        rs.getString("date_planted")
                );
                crop.setId(rs.getInt("id"));
                crops.add(crop);
            }
        } catch (SQLException e) {
            System.err.println("Failed to load crops: " + e.getMessage());
        }
        return crops;
    }

    /**
     * Deletes a crop record by ID.
     * @param id the record to delete
     */
    public void delete(int id) {
        String sql = "DELETE FROM crops WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to delete crop: " + e.getMessage());
        }
    }
}
