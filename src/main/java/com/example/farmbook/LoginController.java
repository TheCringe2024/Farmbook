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
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label accessLabel;

    private final IUserDAO userDAO;

    /**
     * Creates the controller with the SQLite user DAO. JavaFX uses this constructor.
     */
    public LoginController() {
        this(new SqliteUserDAO());
    }

    /**
     * Creates the controller with any IUserDAO, e.g. a fake one for testing.
     * @param userDAO the DAO used to check logins
     */
    public LoginController(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Gets the username and password from the text fields and checks them against the registered users in the database.
     * If a match is found the user is logged in and taken to the dashboard but otherwise an error is shown.
     * @throws IOException if the homepage-view.fxml file cant be loaded
     */
    @FXML
    protected void onLoginClick() throws IOException {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = passwordField.getText();

        try {
            if (userDAO.isValidLogin(enteredUsername, enteredPassword)) {
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
        stage.setScene(new Scene(loader.load(), 1920, 1080));
    }
}