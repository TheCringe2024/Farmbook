package com.example.farmbook;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Whenever a screen is swapped in, put the window back to the same size
        stage.sceneProperty().addListener((observable, oldScene, newScene) ->
                Platform.runLater(() -> fillScreen(stage)));

        stage.setTitle("FarmBook");
        showLogin();

        stage.show();
        fillScreen(stage);
    }

    /** Makes the window fill the usable part of the screen, so every screen is the same size. */
    private static void fillScreen(Stage stage) {
        Rectangle2D area = Screen.getPrimary().getVisualBounds();
        stage.setX(area.getMinX());
        stage.setY(area.getMinY());
        stage.setWidth(area.getWidth());
        stage.setHeight(area.getHeight());
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
        new LoginApplication().start(primaryStage);
    }

    public static void showHome() throws IOException {
        if (!SessionState.isAuthenticated()) {
            showLogin();
            return;
        }

        new HomePage_application().start(primaryStage);
        primaryStage.setTitle("Farmbook");
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
    }

    public static void showSettings() throws IOException {
        if (!SessionState.isAuthenticated()) {
            showLogin();
            return;
        }

        changeScene("settings-view.fxml");
    }
}
