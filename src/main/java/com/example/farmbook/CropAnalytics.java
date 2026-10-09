package com.example.farmbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Handles data processing and analytics for Crops.
 * Separating this from the controller adheres to the Single Responsibility Principle.
 */
public class CropAnalytics {

    public static ObservableList<PieChart.Data> generatePieChartData(List<Crop> cropList) {
        Map<String, Integer> summary = new HashMap<>();
        for (Crop crop : cropList) {
            String name = crop.getPlantName();
            summary.put(name, summary.getOrDefault(name, 0) + crop.getAmount());
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
        }
        return pieChartData;
    }

    public static XYChart.Series<String, Number> generateTimelineData(List<Crop> cropList) {
        // TreeMap automatically sorts the dates chronologically
        Map<String, Integer> timeline = new TreeMap<>();
        for (Crop crop : cropList) {
            String date = crop.getDatePlanted();
            if (date != null && !date.isEmpty()) {
                timeline.put(date, timeline.getOrDefault(date, 0) + crop.getAmount());
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Crops Planted per Date");
        for (Map.Entry<String, Integer> entry : timeline.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        return series;
    }
}