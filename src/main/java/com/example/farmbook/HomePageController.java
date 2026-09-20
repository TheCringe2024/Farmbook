package com.example.farmbook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {

    @FXML
    protected void onCrops(ActionEvent event) throws IOException {
        Stage stage = stageOf(event);
        FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("crop-view.fxml"));
        stage.setScene(new Scene(loader.load(), 800, 600));
        stage.setTitle("Crops");
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    @FXML
    protected void onInventory(ActionEvent event) throws IOException {
        Stage stage = stageOf(event);
        FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("inventory-add-view.fxml"));
        stage.setScene(new Scene(loader.load(), 800, 500));
        stage.setTitle("Inventory");
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    @FXML
    protected void onLivestock(ActionEvent event) throws IOException {
        Stage stage = stageOf(event);

        FXMLLoader loader = new FXMLLoader(
                HomePageController.class.getResource("livestock-list-view.fxml")
        );

        stage.setScene(new Scene(loader.load(), 800, 600));
        stage.setTitle("FarmBook - Livestock");
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    @FXML
    protected void onSettings() throws IOException {
        HelloApplication.showSettings();
    }

    @FXML
    protected void onExit() throws IOException {
        SessionState.logout();
        HelloApplication.showLogin();
    }

    private static Stage stageOf(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}