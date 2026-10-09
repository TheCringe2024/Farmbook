package com.example.farmbook.dao;

import com.example.farmbook.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and updates inventory items in the database.
 */
public class ItemDAO implements IItemDAO {

    /**
     * Saves a new item.
     * @param item the item to save
     */
    public void save(Item item) {
        executeUpdate(
                "INSERT INTO items (name, category, unit, quantity) VALUES (?, ?, ?, ?)",
                "Failed to save item",
                item.getName(), item.getCategory(), item.getUnit(), item.getQuantity());
    }

    /**
     * Returns all saved items.
     * @return list of every inventory item
     */
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM items")) {
            while (rs.next()) {
                items.add(mapRow(rs));
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
     * @return true if successful, false if the amount is negative
     */
    public boolean addStock(int itemId, int amount) {
        if (amount < 0) {
            return false;
        }
        return executeUpdate(
                "UPDATE items SET quantity = quantity + ? WHERE id = ?",
                "Failed to add stock",
                amount, itemId);
    }

    /**
     * Removes stock from an item.
     * @param itemId item to update
     * @param amount stock to remove
     * @return true if successful, false if not enough stock
     */
    public boolean removeStock(int itemId, int amount) {
        if (!hasEnoughStock(itemId, amount)) {
            return false;
        }
        return executeUpdate(
                "UPDATE items SET quantity = quantity - ? WHERE id = ?",
                "Failed to remove stock",
                amount, itemId);
    }

    /**
     * Runs one INSERT/UPDATE with the given values.
     * @return true if it worked, false if the database reported an error
     */
    private boolean executeUpdate(String sql, String errorMessage, Object... values) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(errorMessage + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks the item has at least this much stock.
     * @return false if there isn't enough, or the check fails
     */
    private boolean hasEnoughStock(int itemId, int amount) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT quantity FROM items WHERE id = ?")) {
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            return !rs.next() || rs.getInt("quantity") >= amount;
        } catch (SQLException e) {
            System.err.println("Failed to check stock: " + e.getMessage());
            return false;
        }
    }

    /** Turns one database row into an Item. */
    private Item mapRow(ResultSet rs) throws SQLException {
        Item item = new Item(
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("unit"),
                rs.getInt("quantity"));
        item.setId(rs.getInt("id"));
        return item;
    }
}
