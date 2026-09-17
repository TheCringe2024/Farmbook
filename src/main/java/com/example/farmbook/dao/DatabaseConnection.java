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

    public static void initialiseLivestockTable() {
        String sql = "CREATE TABLE IF NOT EXISTS livestock (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "species TEXT NOT NULL," +
                "identifier TEXT NOT NULL," +
                "date_acquired TEXT NOT NULL" +
                ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to create livestock table: " + e.getMessage());
        }
    }
}
