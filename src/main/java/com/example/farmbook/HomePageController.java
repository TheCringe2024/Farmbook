package com.example.farmbook;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HomePageController {

    // Identification variables for API
    @FXML
    private Label weatherLabel;

    @FXML
    private Label timeLabel;

    private Timeline liveClock;

    @FXML private Label welcomeLabel;
    @FXML private Label animalCountLabel;
    @FXML private Label itemCountLabel;
    @FXML private Label lowStockLabel;
    @FXML private Label cropCountLabel;

    private void startLiveClock() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        liveClock = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    timeLabel.setText(
                            LocalTime.now().format(formatter)
                    );
                }),
                new KeyFrame(Duration.seconds(1))
        );
        liveClock.setCycleCount(Timeline.INDEFINITE);
        liveClock.play();
    }
    // Connecting the app to the backend of the API
    public void initialize() throws IOException, InterruptedException{
        WeatherAPI weatherAPI = new WeatherAPI();
        weatherData weather = weatherAPI.getWeather();

        weatherLabel.setText(weather.getTemperature() + "°C");
        startLiveClock();
        loadDashboard();
    }

    @FXML
    protected void onCrops(ActionEvent event) throws IOException {
        Stage stage = stageOf(event);
        FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("crop-view.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("FarmBook - Crops");
        stage.setMaximized(true);
    }

    @FXML
    protected void onInventory(ActionEvent event) throws IOException {
        Stage stage = stageOf(event);
        FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("inventory-add-view.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Inventory");
        stage.setMaximized(true);
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

    /** Fills the welcome line and the cards with the user's real data. */
    private void loadDashboard() {
        String name = SessionState.getCurrentUsername();
        welcomeLabel.setText(name == null ? "Welcome back" : "Welcome back, " + name);

        DashboardSummary summary;
        try {
            com.example.farmbook.dao.DatabaseConnection.initialiseLivestockTable();
            CropDAO crops = new CropDAO();
            crops.createTable();
            summary = DashboardSummary.from(
                    new com.example.farmbook.dao.LivestockDAO().findAll(),
                    new SqliteInventoryDAO().getInventoryItems(),
                    crops.getAll());
        } catch (Exception e) {
            summary = DashboardSummary.from(null, null, null);
        }

        animalCountLabel.setText(String.valueOf(summary.getAnimals()));
        itemCountLabel.setText(String.valueOf(summary.getItems()));
        lowStockLabel.setText(summary.getLowStock() + " low on stock");
        cropCountLabel.setText(String.valueOf(summary.getCrops()));
    }
}
