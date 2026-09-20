package com.example.farmbook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePage_application extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        if (!SessionState.isAuthenticated()) {
            new LoginApplication().start(stage);
            return;
        }

        FXMLLoader loader = new FXMLLoader(HomePage_application.class.getResource("home-view.fxml"));
        stage.setScene(new Scene(loader.load(), 1000, 800));
        stage.setTitle("Farmbook");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        // Kill Database connection when stopped
        SqliteConnection.getInstance().close();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}