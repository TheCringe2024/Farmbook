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
    public void start(Stage stage) throws Exception {
        Label logo = new Label("Farmbook");
        logo.setStyle("-fx-font-size: 20px;");

        Button homeButton = new Button("Home");
        Button cropsButton = new Button("Crops");
        Button inventoryButton = new Button("Inventory");
        Button livestockButton = new Button("Livestock");

        HBox navigation = new HBox(10);
        navigation.getChildren().addAll(homeButton, cropsButton, inventoryButton, livestockButton);
        navigation.setAlignment(Pos.CENTER);

        HBox header = new HBox(30);
        header.getChildren().addAll(logo, navigation);
        header.setAlignment(Pos.CENTER);

        Separator line = new Separator();
        VBox topSection = new VBox(10);
        topSection.getChildren().addAll(header, line);

        Label welcome = new Label("Welcome to Farmbook");
        welcome.setStyle("-fx-font-size: 40px;");

        Label description = new Label("Farming management application (DRAFT)");
        description.setStyle("-fx-font-size: 20px;");

        Button cropsBodyButton = new Button("Crops");
        Button inventoryBodyButton = new Button("Inventory");
        Button livestockBodyButton = new Button("Livestock");
        Button exitButton = new Button("Exit");

        // WIRE UP: clicking either "Livestock" button opens the Add Livestock screen
        livestockButton.setOnAction(e -> openAddLivestockScreen(stage));
        livestockBodyButton.setOnAction(e -> openAddLivestockScreen(stage));

        // WIRE UP: Exit button actually closes the app
        exitButton.setOnAction(e -> stage.close());

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

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void openAddLivestockScreen(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-livestock-view.fxml"));
            Scene scene = new Scene(loader.load(), 400, 400);
            stage.setScene(scene);
            stage.setTitle("FarmBook - Add Livestock");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
