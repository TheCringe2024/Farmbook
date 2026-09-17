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
        //-- Header of the app --
        Label logo = new Label("Farmbook");
        logo.setStyle("-fx-font-size: 20px;");

        // Individual Buttons
        Button homeButton = new Button("Home");
        homeButton.setStyle("-fx-font-size: 15px;");

        Button cropsButton = new Button("Crops");
        cropsButton.setStyle("-fx-font-size: 15px;");

        Button inventoryButton = new Button("Inventory");
        inventoryButton.setStyle("-fx-font-size: 15px;");


        Button livestockButton = new Button("Livestock");
        livestockButton.setStyle("-fx-font-size: 15px;");

        // Putting it all together except the logo "FARMBOOK" only for the navigation buttons
        HBox navigation = new HBox(10);
        navigation.getChildren().addAll(
                homeButton,
                cropsButton,
                inventoryButton,
                livestockButton
        );
        navigation.setAlignment(Pos.CENTER);

        // Now putting it all together including the title for the nav bar
        HBox header = new HBox(30);
        header.getChildren().addAll(logo,navigation);
        header.setAlignment(Pos.CENTER);

        // Line for aesthetic reason
        Separator line = new Separator();
        VBox topSection = new VBox(10);
        topSection.getChildren().addAll(header,line);

        // -- Main content

        // Title of the homepage
        Label welcome = new Label("Farmbook");
        welcome.setStyle("-fx-font-size: 40px;");

        // Description could be itierated as a feature later
        Label description = new Label("What we up to?");
        description.setStyle("-fx-font-size: 20px;");

        // Creating the individual buttons. Not together yet
        Button cropsBodyButton = new Button("Crops");
        cropsBodyButton.setStyle("-fx-font-size: 20px;");

        Button inventoryBodyButton = new Button("Inventory");
        inventoryBodyButton.setStyle("-fx-font-size: 20px;");

        Button livestockBodyButton = new Button("Livestock");
        livestockBodyButton.setStyle("-fx-font-size: 20px;");

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 20px;");

        // Creating a grid for the individual buttons
        GridPane bodyButtons = new GridPane();
        bodyButtons.setHgap(20);
        bodyButtons.setVgap(20);
        bodyButtons.setAlignment(Pos.CENTER);

        // Making the buttons visually appealing equally.
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();

        column1.setPercentWidth(30);
        column2.setPercentWidth(30);
        bodyButtons.getColumnConstraints().addAll(column1,column2);

        //GPT
        cropsBodyButton.setMaxWidth(Double.MAX_VALUE);
        inventoryBodyButton.setMaxWidth(Double.MAX_VALUE);
        livestockBodyButton.setMaxWidth(Double.MAX_VALUE);
        exitButton.setMaxWidth(Double.MAX_VALUE);

        // Adding the individual buttons into the grid
        bodyButtons.add(cropsBodyButton,0,0);
        bodyButtons.add(inventoryBodyButton,1,0);
        bodyButtons.add(livestockBodyButton,0,2);
        bodyButtons.add(exitButton,1,2);

        // Putting it all altogether under one variable the mainSection variable like the "topSection" variable
        VBox mainSection = new VBox(20);
        mainSection.getChildren().addAll(
                welcome,
                description,
                bodyButtons
        );
       mainSection.setAlignment(Pos.CENTER);


        // -- Page layout
        BorderPane root = new BorderPane();
        root.setTop(topSection);
        root.setCenter(mainSection);


        inventoryButton.setOnAction(e -> openInventory(inventoryButton));
        inventoryBodyButton.setOnAction(e -> openInventory(inventoryBodyButton));

        // Exit button
        exitButton.setOnAction(e -> openLogin(exitButton));

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
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

    private static void openLogin(Button source) {
        try {
            Stage stage = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 450, 550);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.sizeToScene();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        launch();
    }
}