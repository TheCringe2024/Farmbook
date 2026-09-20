package com.example.farmbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Provides SQLite database connections and initialises
 * the persistence tables used by Farmbook.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:farmbook.db";

    /**
     * Opens a connection to the Farmbook SQLite database.
     *
     * @return an active database connection
     * @throws SQLException if the connection cannot be created
     */

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialiseCropTable() {
        String sql = "CREATE TABLE IF NOT EXISTS crops (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT NOT NULL," +
                "field_plot TEXT NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "date_planted TEXT NOT NULL" +
                ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to create crops table: " + e.getMessage());
        }
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

    /**
     * Creates the livestock table when it does not already exist.
     * Required livestock fields are protected by NOT NULL constraints.
     */
    public static void initialiseLivestockTable() {
        String sql = "CREATE TABLE IF NOT EXISTS livestock (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "species TEXT NOT NULL," +
                "identifier TEXT NOT NULL," +
                "date_acquired TEXT NOT NULL" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to create livestock table: " + e.getMessage());
        }
    }
}
