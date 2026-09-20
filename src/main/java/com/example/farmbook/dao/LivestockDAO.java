package com.example.farmbook.dao;

import com.example.farmbook.model.Livestock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads livestock records from the database.
 */
public class LivestockDAO {

    /**
     * Saves a new animal record.
     * @param livestock the animal to save
     * @return true if saved successfully
     */
    public boolean save(Livestock livestock) {
        String sql = "INSERT INTO livestock (species, identifier, date_acquired) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, livestock.getSpecies());
            ps.setString(2, livestock.getIdentifier());
            ps.setString(3, livestock.getDateAcquired());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to save livestock: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns all saved animals.
     * @return list of every livestock record
     */
    public List<Livestock> findAll() {
        List<Livestock> list = new ArrayList<>();
        String sql = "SELECT * FROM livestock";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Livestock l = new Livestock(
                        rs.getString("species"),
                        rs.getString("identifier"),
                        rs.getString("date_acquired")
                );
                l.setId(rs.getInt("id"));
                list.add(l);
            }
        } catch (SQLException e) {
            System.err.println("Failed to load livestock: " + e.getMessage());
        }
        return list;
    }

    /**
     * Deletes an animal record by ID.
     * @param id the record to delete
     */
    public void delete(int id) {
        String sql = "DELETE FROM livestock WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to delete livestock: " + e.getMessage());
        }
    }
}
