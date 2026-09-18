package com.example.farmbook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;

public class HomePage_application extends Application {

    @Override
    public void start(Stage stage) {

        if (!SessionState.isAuthenticated()) {
            try {
                new LoginApplication().start(stage);
            } catch (java.io.IOException exception) {
                throw new java.io.UncheckedIOException(exception);
            }
            return;
        }
        //-- Header of the app --
        Label logo = new Label("Farmbook");
        logo.setStyle("-fx-font-size: 20px;");

        Button homeButton = new Button("Home");
        Button cropsButton = new Button("Crops");
        Button inventoryButton = new Button("Inventory");
        inventoryButton.setStyle("-fx-font-size: 15px;");

        Button livestockButton = new Button("Livestock");

        HBox navigation = new HBox(10);
        navigation.getChildren().addAll(homeButton, cropsButton, inventoryButton, livestockButton);
        navigation.setAlignment(Pos.CENTER);

        Button settingsButton = new Button("Settings");
        settingsButton.setStyle("-fx-font-size: 15px;");
        settingsButton.setOnAction(event -> {
            try {
                HelloApplication.showSettings();
            } catch (java.io.IOException exception) {
                throw new java.io.UncheckedIOException(exception);
            }
        });
        navigation.getChildren().add(settingsButton);

        // Now putting it all together including the title for the nav bar
        HBox header = new HBox(30);
        header.getChildren().addAll(logo, navigation);
        header.setAlignment(Pos.CENTER);

        Separator line = new Separator();
        VBox topSection = new VBox(10);
        topSection.getChildren().addAll(header, line);

        // -- Main content

        // Title of the homepage
        Label welcome = new Label("Farmbook");
        welcome.setStyle("-fx-font-size: 40px;");

        // Description could be itierated as a feature later
        Label description = new Label("What we up to?");
        description.setStyle("-fx-font-size: 20px;");

        Button cropsBodyButton = new Button("Crops");
        Button inventoryBodyButton = new Button("Inventory");
        Button livestockBodyButton = new Button("Livestock");
        Button exitButton = new Button("Exit");

        livestockButton.setOnAction(e -> openAddLivestockScreen(stage));
        livestockBodyButton.setOnAction(e -> openAddLivestockScreen(stage));

        GridPane bodyButtons = new GridPane();
        bodyButtons.setHgap(20);
        bodyButtons.setVgap(20);
        bodyButtons.setAlignment(Pos.CENTER);

        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        column1.setPercentWidth(30);
        column2.setPercentWidth(30);
        bodyButtons.getColumnConstraints().addAll(column1, column2);

        cropsBodyButton.setMaxWidth(Double.MAX_VALUE);
        inventoryBodyButton.setMaxWidth(Double.MAX_VALUE);
        livestockBodyButton.setMaxWidth(Double.MAX_VALUE);
        exitButton.setMaxWidth(Double.MAX_VALUE);

        bodyButtons.add(cropsBodyButton, 0, 0);
        bodyButtons.add(inventoryBodyButton, 1, 0);
        bodyButtons.add(livestockBodyButton, 0, 2);
        bodyButtons.add(exitButton, 1, 2);

        VBox mainSection = new VBox(20);
        mainSection.getChildren().addAll(welcome, description, bodyButtons);
        mainSection.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(topSection);
        root.setCenter(mainSection);

        inventoryButton.setOnAction(e -> openInventory(inventoryButton));
        inventoryBodyButton.setOnAction(e -> openInventory(inventoryBodyButton));

        cropsButton.setOnAction(e -> openCrops(cropsButton));
        cropsBodyButton.setOnAction(e -> openCrops(cropsBodyButton));

        // Exit button
        exitButton.setOnAction(e -> openLogin(exitButton));

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void openAddLivestockScreen(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-livestock-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            stage.setScene(scene);
            stage.setTitle("FarmBook - Add Livestock");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void openInventory(Button source) {
        try {
            Stage stage = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("inventory-add-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 500);
            stage.setScene(scene);
            stage.setTitle("Inventory");
            stage.sizeToScene();
            stage.centerOnScreen();
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }

    private static void openCrops(Button source) {
        try {
            Stage stage = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HomePage_application.class.getResource("crop-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setScene(scene);
            stage.setTitle("Crops");
            stage.sizeToScene();
            stage.centerOnScreen();
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }

    private static void openLogin(Button source) {
        SessionState.logout();

        try {
            HelloApplication.showLogin();
        } catch (java.io.IOException exception) {
            throw new java.io.UncheckedIOException(exception);
        }
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
