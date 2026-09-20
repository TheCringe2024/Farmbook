package com.example.farmbook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterApplication extends Application {

    /**
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException if it cant load register-view.fxml
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(RegisterApplication.class.getResource("register-view.fxml"));
        Scene scene = new Scene(loader.load(), 450, 550);
        stage.setTitle("FarmBook - Register");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the application
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}