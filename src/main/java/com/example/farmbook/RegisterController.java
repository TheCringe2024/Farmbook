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

    @FXML
    public void initialize() {
        createTable();
    }

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

    private boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

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

    @FXML
    protected void onGoBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 450, 550));
    }

    private void showError(String msg) {
        messageLabel.setStyle("-fx-text-fill: #c0392b;");
        messageLabel.setText(msg);
    }

    private void showSuccess(String msg) {
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
        messageLabel.setText(msg);
    }
}