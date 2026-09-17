package com.example.farmbook.dao;

import com.example.farmbook.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public void save(Item item) {
        String sql = "INSERT INTO items (name, category, unit, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getCategory());
            ps.setString(3, item.getUnit());
            ps.setInt(4, item.getQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save item: " + e.getMessage());
        }
    }

    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Item item = new Item(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("unit"),
                        rs.getInt("quantity")
                );
                item.setId(rs.getInt("id"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Failed to load items: " + e.getMessage());
        }
        return items;
    }

    public void addStock(int itemId, int amount) {
        String sql = "UPDATE items SET quantity = quantity + ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setInt(2, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to add stock: " + e.getMessage());
        }
    }
}
