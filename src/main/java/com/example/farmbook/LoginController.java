package com.example.farmbook;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label accessLabel;
    @FXML
    private Button logonButton;

    //TODO: store in database or encrypt or just not hardcoded
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    @FXML
    protected void onLoginClick() throws IOException {
        //welcomeText.setText("Welcome to JavaFX Application!");
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        if (enteredUsername.equals(USERNAME) && enteredPassword.equals(PASSWORD))
        {
            accessLabel.setText("Login success");

            SessionState.login();
            HelloApplication.showHome();

        }
        else
        {
            accessLabel.setText("Wrong password or username! Try again!");
        }
    }
}
