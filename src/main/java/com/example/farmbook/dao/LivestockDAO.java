package com.example.farmbook.dao;

import com.example.farmbook.model.Livestock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is in charge of saving and loading livestock records
 * (like cows, sheep, chickens) to and from the database.
 * Think of it as the "middleman" between the app and the database —
 * nothing else in the app talks to the database directly for livestock.
 */
public class LivestockDAO {

    /**
     * Saves a new animal to the database.
     * @param livestock the animal we want to save
     */
    public void save(Livestock livestock) {
        String sql = "INSERT INTO livestock (species, identifier, date_acquired) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, livestock.getSpecies());
            ps.setString(2, livestock.getIdentifier());
            ps.setString(3, livestock.getDateAcquired());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save livestock: " + e.getMessage());
        }
    }

    /**
     * Gets every animal that's been saved so far.
     * Used to show the farmer the full list of their livestock.
     * @return a list of all animals, or an empty list if none exist yet
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
     * Removes an animal record completely, based on its ID.
     * Used if a farmer added something by mistake or an animal is no longer being tracked.
     * @param id the ID of the animal record to remove
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
