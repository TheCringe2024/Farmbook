package com.example.farmbook.dao;

import com.example.farmbook.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and updates inventory items in the database.
 */
public class ItemDAO {

    /**
     * Saves a new item.
     * @param item the item to save
     */
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

    /**
     * Returns all saved items.
     * @return list of every inventory item
     */
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

    /**
     * Adds stock to an item.
     * @param itemId item to update
     * @param amount stock to add
     * @return true if successful
     */
    public boolean addStock(int itemId, int amount) {
        if (amount < 0) {
            return false;
        }
        String sql = "UPDATE items SET quantity = quantity + ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setInt(2, itemId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to add stock: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes stock from an item.
     * @param itemId item to update
     * @param amount stock to remove
     * @return true if successful, false if not enough stock
     */
    public boolean removeStock(int itemId, int amount) {
        String checkSql = "SELECT quantity FROM items WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, itemId);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                int current = rs.getInt("quantity");
                if (current < amount) {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to check stock: " + e.getMessage());
            return false;
        }

        String updateSql = "UPDATE items SET quantity = quantity - ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setInt(1, amount);
            ps.setInt(2, itemId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to remove stock: " + e.getMessage());
            return false;
        }
    }
}
