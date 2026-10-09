package com.example.farmbook;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final IUserDAO userDAO;

    /**
     * Creates the controller with the SQLite user DAO, which also makes sure
     * the users table exists. JavaFX uses this constructor.
     */
    public RegisterController() {
        this(new SqliteUserDAO());
    }

    /**
     * Creates the controller with any IUserDAO, e.g. a fake one for testing.
     * @param userDAO the DAO used to check and save users
     */
    public RegisterController(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Gets the username, email and password from the text fields and checks
     * the email is a valid email through regex, then saves the new user
     * through the DAO.
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
            if (userDAO.usernameExists(username)) {
                showError("That username is already taken.");
                return;
            }

            if (userDAO.emailExists(email)) {
                showError("That email is already taken.");
                return;
            }

            userDAO.addUser(username, email, password);

            showSuccess("Registration successful!");
            usernameField.clear();
            emailField.clear();
            passwordField.clear();
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
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
        stage.setScene(new Scene(loader.load(), 1920, 1080));
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