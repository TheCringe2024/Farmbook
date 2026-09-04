package com.example.farmbook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        stage.setTitle("FarmBook");
        showLogin();

        stage.show();
    }

    private static void changeScene(String fxmlFile) throws IOException {
        var resource = HelloApplication.class.getResource(
                "/com/example/farmbook/" + fxmlFile
        );

        if (resource == null) {
            throw new IllegalStateException(
                    "FXML file not found: " + fxmlFile
            );
        }

        FXMLLoader loader = new FXMLLoader(resource);
        Scene scene = new Scene(loader.load(), 500, 400);

        primaryStage.setScene(scene);
    }

    public static void showLogin() throws IOException {
        changeScene("login-view.fxml");
    }

    public static void showHome() throws IOException {
        if (!SessionState.isAuthenticated()) {
            showLogin();
            return;
        }

        changeScene("home-view.fxml");
    }

    public static void showSettings() throws IOException {
        if (!SessionState.isAuthenticated()) {
            showLogin();
            return;
        }

        changeScene("settings-view.fxml");
    }
}