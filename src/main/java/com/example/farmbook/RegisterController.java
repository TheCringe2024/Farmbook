package com.example.farmbook;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final Connection connection = SqliteConnection.getInstance();

    /**
     *Runs when the register page is loaded and makes sure the user
     * tables exists in the database.
     */
    @FXML
    public void initialize() {
        createTable();
    }

    /**
     * Gets the username, email and password from the text fields and checks
     * the email is a valid email through regex and hashes the password
     * and stores it's into the database.
     */
    @FXML
    protected void onRegisterClick() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        String regex =  "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

        if (username.isEmpty()) {
            showError("Please enter a username.");
            return;
        }

        if (!email.matches(regex)) {
            showError("Please enter a valid email.");
            return;
        }
        if (password.isEmpty()) {
            showError("Please enter a password.");
            return;
        }

        try {
            if (usernameExists(username)) {
                showError("That username is already taken.");
                return;
            }

            if (emailExists(email)) {
                showError("That email is already taken.");
                return;
            }

            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, PasswordHash.hash(password));
                stmt.executeUpdate();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            showSuccess("Registration successful!");
            usernameField.clear();
            emailField.clear();
            passwordField.clear();
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    /**
     * Checks the database to see if a user with a username has already been
     * taken so each account has a unique username
     * @param username the username that is entered in the register form
     * @return true if the username is already taken and false if its available.
     * @throws SQLException if the database query fails
     */
    private boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Checks the database to see if a user with an email has already been
     * taken so each account has a unique email
     * @param email the email that is entered in the register form
     * @return true if the email is already taken and false if its available.
     * @throws SQLException if the database query fails
     */
    private boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }


    /**
     * Creates the users table in the database if it doesnt already exist with an id,
     * a unique username, unique email and the hashed password. Displays an error message if the table cant be reached.
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
            showError("Could not create users table: " + e.getMessage());
        }
    }

    /**
     * Loads the logon page and swaps it into the current window when the back button is clicked
     * @throws IOException if the login-view.fxml file cant be loaded
     */
    @FXML
    protected void onGoBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 450, 550));
    }

    /**
     * Displays an error message in red below the register form
     * @param msg the error message to show the user
     */
    private void showError(String msg) {
        messageLabel.setStyle("-fx-text-fill: #c0392b;");
        messageLabel.setText(msg);
    }

    /**
     * Displays the success messsage in green below the register form
     * @param msg the success message to show the user
     */
    private void showSuccess(String msg) {
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
        messageLabel.setText(msg);
    }
}