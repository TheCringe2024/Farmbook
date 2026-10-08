package com.example.farmbook;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteUserDAO implements IUserDAO {

    private final Connection connection;

    /**
     * Gets the shared database connection and makes sure the users table exists
     * so users can be registered and logged in straight away.
     */
    public SqliteUserDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }

    /**
     * Creates the users table in the database if it doesnt already exist with an id,
     * a unique username, unique email and the hashed password.
     */
    private void createTable() {
        String userSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "email TEXT NOT NULL UNIQUE,"
                + "password TEXT"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(userSQL);
        } catch (SQLException e) {
            System.err.println("Could not create users table: " + e.getMessage());
        }
    }

    /**
     * Hashes the password and inserts a new user into the database.
     * @param username the username entered in the register form
     * @param email the email entered in the register form
     * @param password the plain text password entered in the register form
     * @throws SQLException if the insert fails
     */
    @Override
    public void addUser(String username, String email, String password) throws SQLException {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hash(password));
            stmt.executeUpdate();
        }
    }

    /**
     * Hashes the entered password and checks the database for a user with a matching username and password
     * @param username the username entered in the login form
     * @param password the plain text password entered in the login form
     * @return true if a matching user is found, false if not
     * @throws SQLException if the database query fails
     */
    @Override
    public boolean isValidLogin(String username, String password) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hash(password));
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Checks the database to see if a username has already been taken
     * so each account has a unique username
     * @param username the username that is entered in the register form
     * @return true if the username is already taken and false if its available.
     * @throws SQLException if the database query fails
     */
    @Override
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Checks the database to see if an email has already been taken
     * so each account has a unique email
     * @param email the email that is entered in the register form
     * @return true if the email is already taken and false if its available.
     * @throws SQLException if the database query fails
     */
    @Override
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Wraps PasswordHash so the NoSuchAlgorithmException is handled in one place
     * instead of in every method that needs a hash.
     */
    private String hash(String password) {
        try {
            return PasswordHash.hash(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
