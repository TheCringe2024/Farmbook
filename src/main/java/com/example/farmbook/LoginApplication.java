package com.example.farmbook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        //stage.setTitle("Hello!");
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login-view.fxml"));

        // The page of the login application
        Scene scene = new Scene(fxmlLoader.load(), 450, 550);

        // Settings
        stage.setTitle("Farmbook - Login");
        stage.setScene(scene);

        // Making it a consistent size
        stage.setResizable(false);

        // Aesthetic via putting it in the middle
        stage.centerOnScreen();

        // Displaying it
        stage.show();
    }
}
