package com.example.farmbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class CropController {

    // link var to fxml filke
    @FXML private TableView<Crop> table;
    @FXML private TableColumn<Crop, Integer> idCol;
    @FXML private TableColumn<Crop, String> nameCol;
    @FXML private TableColumn<Crop, String> typeCol;
    @FXML private TableColumn<Crop, Integer> amountCol;
    @FXML private TableColumn<Crop, String> dateCol;
    @FXML private TableColumn<Crop, String> notesCol;

    @FXML private PieChart summaryChart;
    @FXML private HBox formLayout;
    @FXML private TextField nameInput;
    @FXML private TextField typeInput;
    @FXML private TextField amountInput;
    @FXML private DatePicker dateInput;
    @FXML private TextField notesInput;
    @FXML private Button btnToggleView;
    @FXML private Button btnBack;

    private final CropDAO cropDAO = new CropDAO();

    // initialize() runs automatically right after the FXML is loaded.
    @FXML
    public void initialize() {
        cropDAO.createTable();

        // Bind table columns to Crop object properties
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("plantName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("cropType"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("datePlanted"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        refreshTable();

        // Listener to populate inputs when a table row is clicked
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameInput.setText(newSelection.getPlantName());
                typeInput.setText(newSelection.getCropType());
                amountInput.setText(String.valueOf(newSelection.getAmount()));
                dateInput.setValue(java.time.LocalDate.parse(newSelection.getDatePlanted()));
                notesInput.setText(newSelection.getNotes());
            }
        });
    }

    @FXML
    void handleAddCrop(ActionEvent event) {
        Crop newCrop = new Crop(
                nameInput.getText(), typeInput.getText(),
                Integer.parseInt(amountInput.getText()),
                dateInput.getValue() != null ? dateInput.getValue().toString() : "",
                notesInput.getText()
        );
        cropDAO.insert(newCrop);
        refreshTable();
    }

    @FXML
    void handleUpdateCrop(ActionEvent event) {
        Crop selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setPlantName(nameInput.getText());
            selected.setCropType(typeInput.getText());
            selected.setAmount(Integer.parseInt(amountInput.getText()));
            selected.setDatePlanted(dateInput.getValue() != null ? dateInput.getValue().toString() : "");
            selected.setNotes(notesInput.getText());

            cropDAO.update(selected);
            refreshTable();
        }
    }

    @FXML
    void handleDeleteCrop(ActionEvent event) {
        Crop selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cropDAO.delete(selected.getId());
            refreshTable();
        }
    }

    @FXML
    void handleToggleView(ActionEvent event) {
        if (table.isVisible()) {
            table.setVisible(false);
            summaryChart.setVisible(true);
            btnToggleView.setText("Show Table");
            formLayout.setDisable(true);
        } else {
            summaryChart.setVisible(false);
            table.setVisible(true);
            btnToggleView.setText("Show Chart");
            formLayout.setDisable(false);
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        try {
            // Get the current stage from the button, and launch HomePage back onto it
            Stage stage = (Stage) btnBack.getScene().getWindow();
            new HomePage_application().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        ObservableList<Crop> cropList = FXCollections.observableArrayList(cropDAO.getAll());
        table.setItems(cropList);
        updateChartData(cropList);
    }

    private void updateChartData(ObservableList<Crop> cropList) {
        Map<String, Integer> summary = new HashMap<>();
        for (Crop crop : cropList) {
            String name = crop.getPlantName();
            summary.put(name, summary.getOrDefault(name, 0) + crop.getAmount());
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
        }
        summaryChart.setData(pieChartData);
    }
}