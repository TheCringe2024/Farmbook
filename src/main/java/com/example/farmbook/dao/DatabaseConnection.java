package com.example.farmbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:farmbook.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialiseItemTable() {
        String sql = "CREATE TABLE IF NOT EXISTS items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "category TEXT NOT NULL," +
                "unit TEXT NOT NULL," +
                "quantity INTEGER NOT NULL DEFAULT 0" +
                ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to create items table: " + e.getMessage());
        }
    }
}
