package com.example.farmbook;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {

    private static Connection instance = null;

    /**
     * OPens a connection to the inventory.db database file and stores it in the shared isntance.
     * the contructor is private so only the connection can created through getInstance().
     */
    private SqliteConnection() {
        String url = "jdbc:sqlite:inventory.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    /**
     * Gets the shared database connection by creating it the first time it is needed
     * reuses the same connection after that.
     * @return the connection to the inventory.db or null if it could be opened.
     */
    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }
}
