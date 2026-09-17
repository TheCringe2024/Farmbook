package com.example.farmbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.chart.PieChart;

public class CropUI {

    private static CropDAO cropDAO = new CropDAO();

    public static Scene getScene(Stage stage, Scene homeScene) {
        // Initialize Database Table
        cropDAO.createTable();

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        // Table view
        TableView<Crop> table = new TableView<>();

        TableColumn<Crop, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Crop, String> nameCol = new TableColumn<>("Plant Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("plantName"));

        TableColumn<Crop, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("cropType"));

        TableColumn<Crop, Integer> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Crop, String> dateCol = new TableColumn<>("Date Planted");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("datePlanted"));

        TableColumn<Crop, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        table.getColumns().addAll(idCol, nameCol, typeCol, amountCol, dateCol, notesCol);
        table.setItems(getCropList());

        // Summary Chart Setup
        PieChart summaryChart = new PieChart();
        summaryChart.setTitle("Total Crop Amounts");
        summaryChart.setVisible(false); // Hide the chart by default

        // StackPane to hold both Table and Chart, toggle visibility to switch views.
        StackPane displayArea = new StackPane();
        displayArea.getChildren().addAll(summaryChart, table);
        VBox.setVgrow(displayArea, Priority.ALWAYS); // Makes sure the area fills the screen properly

        // Input
        TextField nameInput = new TextField();
        nameInput.setPromptText("Plant Name (e.g. Corn)");

        TextField typeInput = new TextField();
        typeInput.setPromptText("Type (e.g. Grain)");

        TextField amountInput = new TextField();
        amountInput.setPromptText("Amount");

        DatePicker dateInput = new DatePicker();
        dateInput.setPromptText("Date Planted");

        TextField notesInput = new TextField();
        notesInput.setPromptText("Notes");

        HBox formLayout = new HBox(10, nameInput, typeInput, amountInput, dateInput, notesInput);

        // Butoons
        Button btnAdd = new Button("Add Crop");
        Button btnUpdate = new Button("Update Selected");
        Button btnDelete = new Button("Delete Selected");
        Button btnToggleView = new Button("Show Chart");
        Button btnBack = new Button("Back to Home");

        HBox buttonLayout = new HBox(10, btnAdd, btnUpdate, btnDelete, btnToggleView, btnBack);

        // Inputs when clicking a table row
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameInput.setText(newSelection.getPlantName());
                typeInput.setText(newSelection.getCropType());
                amountInput.setText(String.valueOf(newSelection.getAmount()));
                dateInput.setValue(java.time.LocalDate.parse(newSelection.getDatePlanted()));
                notesInput.setText(newSelection.getNotes());
            }
        });

        btnAdd.setOnAction(e -> {
            Crop newCrop = new Crop(
                    nameInput.getText(), typeInput.getText(),
                    Integer.parseInt(amountInput.getText()),
                    dateInput.getValue() != null ? dateInput.getValue().toString() : "",
                    notesInput.getText()
            );
            cropDAO.insert(newCrop);
            table.setItems(getCropList()); // refresh table
            updateChartData(summaryChart); // refresh chart
        });

        btnUpdate.setOnAction(e -> {
            Crop selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setPlantName(nameInput.getText());
                selected.setCropType(typeInput.getText());
                selected.setAmount(Integer.parseInt(amountInput.getText()));
                selected.setDatePlanted(dateInput.getValue() != null ? dateInput.getValue().toString() : "");
                selected.setNotes(notesInput.getText());

                cropDAO.update(selected);
                table.refresh();
                updateChartData(summaryChart);
            }
        });

        btnDelete.setOnAction(e -> {
            Crop selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                cropDAO.delete(selected.getId());
                table.setItems(getCropList());
                updateChartData(summaryChart);
            }
        });

        // NEW: Action for the Toggle View Button
        btnToggleView.setOnAction(e -> {
            if (table.isVisible()) {
                // Switch to Chart View
                table.setVisible(false);
                summaryChart.setVisible(true);
                btnToggleView.setText("Show Table");
                updateChartData(summaryChart);
                formLayout.setDisable(true); //disable text inputs while viewing chart
            } else {
                // Switch to Table View
                summaryChart.setVisible(false);
                table.setVisible(true);
                btnToggleView.setText("Show Chart");
                formLayout.setDisable(false);
            }
        });

        btnBack.setOnAction(e -> stage.setScene(homeScene)); // Return to homepage

        layout.getChildren().addAll(new Label("Crop Management"), displayArea, formLayout, buttonLayout);
        return new Scene(layout, 800, 600);
    }

    // helper method to gather data + build the PieChart
    private static void updateChartData(PieChart chart) {
        //Add by name
        Map<String, Integer> summary = new HashMap<>();
        for (Crop crop : getCropList()) {
            String name = crop.getPlantName();
            int currentTotal = summary.getOrDefault(name, 0);
            summary.put(name, currentTotal + crop.getAmount());
        }

        // convert to piechart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            // format slice name quantity
            pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
        }
        chart.setData(pieChartData);
    }

    private static ObservableList<Crop> getCropList() {
        return FXCollections.observableArrayList(cropDAO.getAll());
    }
}