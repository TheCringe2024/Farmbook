package com.example.farmbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for  Crop UI view
 * Handles user interactions. Updates the TableView and PieChart.
 * Communicates with the database via the CropDAO
 */
public class CropController {

    // link var to fxml file
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

    /**
     * Initialize method automatically called after the FXML file.
     * Sets up the database table, binds table columns to the Crop properties,
     * loads data and starts row selection listener.
     */
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

    /**
     * handles the action of adding a new crop
     * retrieves data from the input fields, inserts it into the database
     * refreshes the table and chart views.
     *
     * @param event ActionEvent triggered by clicking the Add Crop button.
     */
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

    /**
     * handles updating an existing crop record
     * modifies the currently selected crop using the text input fields
     *
     * @param event ActionEvent triggered by clicking the Update Selected button
     */
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

    /**
     * handles the deletion of the selected crop
     * Removes the record from the database and updates the UI
     *
     * @param event ActionEvent triggered by clicking the Delete Selected button
     */
    @FXML
    void handleDeleteCrop(ActionEvent event) {
        Crop selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cropDAO.delete(selected.getId());
            refreshTable();
        }
    }

    /**
     * toggles the UI view between the data table and the summary pie chart
     * also enables or disables the input form to prevent editing while viewing the chart
     *
     * @param event ActionEvent triggered by clicking the toggle view button
     */
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

    /**
     * Navigates the user back to the main Home Page
     *
     * @param event ActionEvent triggered by clicking the back to Home button
     * @throws IOException If the homepage-view.fxml file cannot be loaded
     */
    @FXML
    void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("homepage-view.fxml"));
        Stage stage = (Stage) nameInput.getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 1000, 800));
    }

    /**
     * refreshes the TableView fetching data from the database
     * also updates the PieChart data
     */
    private void refreshTable() {
        ObservableList<Crop> cropList = FXCollections.observableArrayList(cropDAO.getAll());
        table.setItems(cropList);
        updateChartData(cropList);
    }

    /**
     * Calculates the total amount for each unique plant name
     * updates the PieChart with the aggregated data
     *
     * @param cropList The current list of crops retrieved from the database
     */
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