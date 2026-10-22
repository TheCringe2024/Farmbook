package com.example.farmbook;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.concurrent.Task;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

public class HomePageController {

    // Identification variables for API
    @FXML
    private Label temp_max_min;

    @FXML
    private Label timeLabel;

    @FXML
    private Label wind_speedLabel;

    @FXML
    private Label precipitationLabel;

    @FXML
    private Label UV_indexLabel;

    @FXML
    private Timeline liveClock;

    // farmer weather stats
    @FXML
    private Label relative_humidityLabel;

    @FXML
    private Label ref_ET;

    @FXML
    private Label vapour_pressureLabel;

    @FXML
    private Label meanDewpointLabel;

    @FXML
    private Label grow_degree_days_baseLabel;

    @FXML
    private Label grow_leaf_wetnessLabel;

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
    /** Start the clock immediately and fetch weather off the JavaFX UI thread. */

    @FXML
    public void initialize() {
        startLiveClock();

        temp_max_min.setText("Loading weather...");
        wind_speedLabel.setText("-");
        precipitationLabel.setText("-");
        UV_indexLabel.setText("-");
        relative_humidityLabel.setText("-");
        ref_ET.setText("-");
        vapour_pressureLabel.setText("-");
        meanDewpointLabel.setText("-");
        grow_degree_days_baseLabel.setText("-");
        grow_leaf_wetnessLabel.setText("-");

        Task<weatherData> task = new Task<>() {
            @Override
            protected weatherData call() throws Exception {
                return new WeatherAPI().getWeather();
            }
        };

        task.setOnSucceeded(event -> {
            displayWeather(task.getValue());
        });

        task.setOnFailed(event -> {
            temp_max_min.setText("Weather unavailable");
            System.err.println(
                    "Weather loading failed: " + task.getException()
            );
        });

        Thread worker = new Thread(task, "weather-loader");
        worker.setDaemon(true);
        worker.start();
    }

    private void displayWeather(weatherData weather) {
        temp_max_min.setText(
                weather.getMax_temp() + "/" +
                        weather.getMin_temp() + "°C"
        );

        wind_speedLabel.setText(
                weather.getWind_speed() + "km/h"
        );

        precipitationLabel.setText(
                weather.getPrecipitation_prob() + "%"
        );

        UV_indexLabel.setText(
                String.valueOf(weather.getUV_index())
        );

        relative_humidityLabel.setText(
                weather.getRelative_humidity() + "%"
        );

        ref_ET.setText(
                weather.getRef_ET() + "mm"
        );

        vapour_pressureLabel.setText(
                weather.getVapour_pressure_def() + "kPa"
        );

        meanDewpointLabel.setText(
                weather.getMean_dewpoint() + "°C"
        );

        grow_degree_days_baseLabel.setText(
                weather.getGrow_degree_days_base() + "°C/day"
        );

        grow_leaf_wetnessLabel.setText(
                weather.getMean_leaf_wetness() + "%"
        );
    }

    /** Avoid displaying stale sample values while the API is loading. */
    private void setWeatherPending() {
        temp_max_min.setText("Loading weather...");
        setWeatherPlaceholders();
    }

    private void setWeatherUnavailable() {
        temp_max_min.setText("Weather unavailable");
        setWeatherPlaceholders();
    }

    private void setWeatherPlaceholders() {
        wind_speedLabel.setText("--");
        precipitationLabel.setText("--");
        UV_indexLabel.setText("--");
        relative_humidityLabel.setText("--");
        ref_ET.setText("--");
        vapour_pressureLabel.setText("--");
        meanDewpointLabel.setText("--");
        grow_degree_days_baseLabel.setText("--");
        grow_leaf_wetnessLabel.setText("--");
    }

    /** Update controls only on the JavaFX application thread. */
    private void renderWeather(weatherData weather) {
        temp_max_min.setText(
                weather.getMax_temp() + "/" + weather.getMin_temp() + "°C"
        );

        wind_speedLabel.setText(
                weather.getWind_speed() + "km/h"
        );

        precipitationLabel.setText(
                weather.getPrecipitation_prob() + "%"
        );

        UV_indexLabel.setText(
                weather.getUV_index() + ""
        );

        // weather farmer stats
        relative_humidityLabel.setText(
                weather.getRelative_humidity() + "%"
        );

        ref_ET.setText(
                weather.getRef_ET() + "mm"
        );

        vapour_pressureLabel.setText(
                weather.getVapour_pressure_def() + "kPa"
        );

        meanDewpointLabel.setText(
                weather.getMean_dewpoint() + "°C"
        );

        grow_degree_days_baseLabel.setText(
                weather.getGrow_degree_days_base() + "°C/day"
        );

        grow_leaf_wetnessLabel.setText(
                weather.getMean_leaf_wetness() + "%"
        );

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
        stage.setScene(new Scene(loader.load(), 1920, 1080));
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