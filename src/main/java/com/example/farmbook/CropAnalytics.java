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
 * Separating this from the controller adheres to the Single Responsibility Principle,
 * isolating calculations from UI logic.
 */
public class CropAnalytics {
    /**
     * Calculates the total amount for each unique plant name to be displayed in a Pie Chart.
     *
     * @param cropList The list of crops to analyze.
     * @return An ObservableList containing formatted PieChart data slices.
     */
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

    /**
     * Groups crops chronologically by their planted date to be displayed in a Bar Chart.
     * Missing or empty dates are ignored.
     *
     * @param cropList The list of crops to analyze.
     * @return A data series mapped with dates on the X-axis and total amounts on the Y-axis.
     */
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