package com.example.farmbook;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Reads and updates account details in the users table.
 * Login, register and the username and email checks are reused from SqliteUserDAO.
 */
public class SqliteAccountDAO implements IAccountDAO {

    private final Connection connection = SqliteConnection.getInstance();
    private final SqliteUserDAO users = new SqliteUserDAO();

    @Override
    public void addUser(String username, String email, String password) throws SQLException {
        users.addUser(username, email, password);
    }

    @Override
    public boolean isValidLogin(String username, String password) throws SQLException {
        return users.isValidLogin(username, password);
    }

    @Override
    public boolean usernameExists(String username) throws SQLException {
        return users.usernameExists(username);
    }

    @Override
    public boolean emailExists(String email) throws SQLException {
        return users.emailExists(email);
    }

    @Override
    public String getEmail(String username) throws SQLException {
        String sql = "SELECT email FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("email") : null;
            }
        }
    }

    @Override
    public void updateEmail(String username, String newEmail) throws SQLException {
        String sql = "UPDATE users SET email = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newEmail);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updatePassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hash(newPassword));
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    /** Uses the same hash as register and login, so the new password works at the next login. */
    private String hash(String password) {
        try {
            return PasswordHash.hash(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
