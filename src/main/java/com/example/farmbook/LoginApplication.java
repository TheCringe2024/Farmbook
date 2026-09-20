package com.example.farmbook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    /**
     *  Loads the login page into the given window sets the title and size
     *  and stops the window from being resized and places it in the middle of the screen
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException if the login-view.fxml file cant be loaded
     */
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
