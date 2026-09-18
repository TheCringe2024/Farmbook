package com.example.farmbook.dao;

import com.example.farmbook.model.Livestock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivestockDAO {

    public LivestockDAO() {
        DatabaseConnection.initialiseLivestockTable();
    }

    public boolean save(Livestock livestock) {
        String sql = "INSERT INTO livestock (species, identifier, date_acquired) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, livestock.getSpecies());
            ps.setString(2, livestock.getIdentifier());
            ps.setString(3, livestock.getDateAcquired());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Failed to save livestock: " + e.getMessage());
            return false;
        }
    }

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
}
