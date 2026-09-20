package com.example.farmbook.dao;

import com.example.farmbook.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles everything to do with saving and updating
 * inventory items (like seeds, tools, fertiliser) in the database.
 * It's the only part of the app that actually talks to the database
 * for inventory — everything else just asks this class to do it.
 */
public class ItemDAO {

    /**
     * Saves a brand new item into the inventory.
     * @param item the item the farmer wants to add
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
     * Gets the full list of everything currently in the inventory.
     * @return every saved item, or an empty list if nothing's been added yet
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
     * Adds more stock to an item — used when new stock arrives
     * (e.g. a farmer buys more seeds).
     * @param itemId which item to update
     * @param amount how much stock is being added
     */
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

    /**
     * Removes stock from an item — used when it's used up or sold.
     * Won't let the amount go below zero, so the numbers always stay accurate.
     * @param itemId which item to update
     * @param amount how much stock is being taken away
     * @return true if it worked, false if there wasn't enough stock to remove
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
