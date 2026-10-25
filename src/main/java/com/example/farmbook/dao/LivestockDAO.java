
package com.example.farmbook.dao;

import com.example.farmbook.SessionState;
import com.example.farmbook.model.Livestock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists livestock records in SQLite and restricts production operations
 * to the user and login session that created this DAO.
 */
public class LivestockDAO implements ILivestockDAO {

    private final String ownerUsername;
    private final boolean sessionBound;
    private final long boundSessionVersion;

    /** Production constructor: binds this DAO to the active login session. */
    public LivestockDAO() {
        this(SessionState.getCurrentUsername(),
                true, SessionState.getSessionVersion());
    }

    /**
     * Package-private test constructor; usable only from DAO-package tests.
     * Production controllers must use the no-argument constructor.
     */
    LivestockDAO(String ownerUsername) {
        this(ownerUsername, false, -1L);
    }

    private LivestockDAO(String ownerUsername,
                         boolean sessionBound,
                         long boundSessionVersion) {
        this.ownerUsername = ownerUsername == null
                ? null : ownerUsername.trim();
        this.sessionBound = sessionBound;
        this.boundSessionVersion = boundSessionVersion;
        createTable();
    }

    private boolean hasOwner() {
        if (ownerUsername == null || ownerUsername.isBlank()) {
            return false;
        }
        return !sessionBound || SessionState.isCurrentSession(
                ownerUsername, boundSessionVersion);
    }

    /** Creates or safely migrates an existing livestock table. */
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS livestock ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "species TEXT NOT NULL, "
                + "identifier TEXT NOT NULL, "
                + "date_acquired TEXT NOT NULL, "
                + "owner_username TEXT"
                + ")";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            boolean hasOwnerColumn = false;
            try (ResultSet columns = stmt.executeQuery(
                    "PRAGMA table_info(livestock)")) {
                while (columns.next()) {
                    if ("owner_username".equalsIgnoreCase(
                            columns.getString("name"))) {
                        hasOwnerColumn = true;
                        break;
                    }
                }
            }

            if (!hasOwnerColumn) {
                stmt.executeUpdate("ALTER TABLE livestock "
                        + "ADD COLUMN owner_username TEXT");
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to initialise livestock table", e);
        }
    }

    /** Saves a livestock record for the currently authorized owner. */
    @Override
    public boolean save(Livestock livestock) {
        if (!hasOwner() || livestock == null) {
            return false;
        }

        String sql = """
                INSERT INTO livestock
                    (species, identifier, date_acquired, owner_username)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, livestock.getSpecies());
            ps.setString(2, livestock.getIdentifier());
            ps.setString(3, livestock.getDateAcquired());
            ps.setString(4, ownerUsername);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Failed to save livestock: " + e.getMessage());
            return false;
        }
    }

    /** Loads only records belonging to the authorized owner. */
    @Override
    public List<Livestock> findAll() {
        List<Livestock> animals = new ArrayList<>();
        if (!hasOwner()) {
            return animals;
        }

        String sql = """
                SELECT id, species, identifier, date_acquired
                FROM livestock
                WHERE owner_username = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerUsername);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Livestock animal = new Livestock(
                            rs.getString("species"),
                            rs.getString("identifier"),
                            rs.getString("date_acquired"));
                    animal.setId(rs.getInt("id"));
                    animals.add(animal);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to load livestock: " + e.getMessage());
        }
        return animals;
    }

    /** Deletes only a record owned by the authorized user. */
    @Override
    public boolean delete(int id) {
        if (!hasOwner() || id <= 0) {
            return false;
        }

        String sql = "DELETE FROM livestock "
                + "WHERE id = ? AND owner_username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, ownerUsername);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Failed to delete livestock: " + e.getMessage());
            return false;
        }
    }

    /** Updates only a record owned by the authorized user. */
    @Override
    public boolean update(Livestock livestock) {
        if (!hasOwner() || livestock == null || livestock.getId() <= 0) {
            return false;
        }

        String sql = """
                UPDATE livestock
                SET species = ?, identifier = ?, date_acquired = ?
                WHERE id = ? AND owner_username = ?
                """;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, livestock.getSpecies());
            ps.setString(2, livestock.getIdentifier());
            ps.setString(3, livestock.getDateAcquired());
            ps.setInt(4, livestock.getId());
            ps.setString(5, ownerUsername);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Failed to update livestock: " + e.getMessage());
            return false;
        }
    }
}
