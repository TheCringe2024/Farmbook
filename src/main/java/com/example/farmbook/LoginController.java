package com.example.farmbook;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label accessLabel;
    @FXML
    private Button logonButton;
    @FXML
    private Button regiserButton;

    private final Connection connection = SqliteConnection.getInstance();

    /**
     * Gets the username and password from the text fields and checks them agaist the registered users in the database.
     * If a match is found the user is logged in and taken to the dashboard but otherwise an error is shown.
     * @throws IOException if the homepage-view.fxml file cant be loaded
     */
    @FXML
    protected void onLoginClick() throws IOException {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = passwordField.getText();

        try {
            if (isValidLogin(enteredUsername, enteredPassword)) {
                accessLabel.setText("Login success");
                SessionState.login();
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("homepage-view.fxml"));
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(loader.load(), 1000, 800));
            } else {
                accessLabel.setText("Wrong password or username! Try again!");
            }
        } catch (SQLException e) {
            accessLabel.setText("Wrong password or username! Try again");
        }
    }

    /**
     * Loads the register page and swaps it into the current window when the register button is clicked
     * @throws IOException If the register-view.fxml file cant be loaded
     */
    @FXML
    protected void onRegisterClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 450, 550));
    }

    /**
     * Hashes the enetered password and checks the database for a user wtih a matching username and password
     * @param username the username entered in the login farm
     * @param password the plain text password entered in the login form
     * @return if a matching user is found, false if not
     * @throws SQLException if the database query fails
     */
    private boolean isValidLogin(String username, String password) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, PasswordHash.hash(password));
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}