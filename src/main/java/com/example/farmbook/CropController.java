package com.example.farmbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

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
    @FXML private BarChart<String, Number> timeChart;
    @FXML private GridPane formLayout;

    @FXML private TextField nameInput;
    @FXML private TextField notesInput;
    @FXML private DatePicker dateInput;

    @FXML private ComboBox<String> typeInput;
    @FXML private ComboBox<String> amountInput;

    @FXML private Button btnToggleView;
    @FXML private Button btnBack;

    private final CropDAO cropDAO = new CropDAO();
    private int viewState = 0;

    /**
     * Initialize method automatically called after the FXML file.
     * Sets up the database table, binds table columns to the Crop properties,
     * loads data and starts row selection listener.
     */
    @FXML
    public void initialize() {
        cropDAO.createTable();

        // Populate predefined items for the ComboBox dropdowns
        typeInput.getItems().addAll("Grain", "Vegetable", "Fruit", "Legume", "Root", "Forage");
        amountInput.getItems().addAll("10", "50", "100", "500", "1000", "5000");

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
                typeInput.setValue(newSelection.getCropType());
                amountInput.setValue(String.valueOf(newSelection.getAmount()));

                if (newSelection.getDatePlanted() != null && !newSelection.getDatePlanted().isEmpty()) {
                    dateInput.setValue(java.time.LocalDate.parse(newSelection.getDatePlanted()));
                }
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
        String type = typeInput.getEditor().getText();
        String amountText = amountInput.getEditor().getText();

        int parsedAmount = 0;
        try {
            parsedAmount = Integer.parseInt(amountText);
        } catch (NumberFormatException ignored) {}

        Crop newCrop = new Crop(
                nameInput.getText(), type,
                parsedAmount,
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
            String type = typeInput.getEditor().getText();
            String amountText = amountInput.getEditor().getText();

            int parsedAmount = 0;
            try {
                parsedAmount = Integer.parseInt(amountText);
            } catch (NumberFormatException ignored) {}

            selected.setPlantName(nameInput.getText());
            selected.setCropType(type);
            selected.setAmount(parsedAmount);
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
        // Cycle through states: 0 -> 1 -> 2 -> 0
        viewState = (viewState + 1) % 3;

        table.setVisible(viewState == 0);
        summaryChart.setVisible(viewState == 1);
        timeChart.setVisible(viewState == 2);

        if (viewState == 0) {
            btnToggleView.setText("Show Distribution (Pie Chart)");
            formLayout.setDisable(false);
        } else if (viewState == 1) {
            btnToggleView.setText("Show Timeline (Bar Chart)");
            formLayout.setDisable(true);
        } else {
            btnToggleView.setText("Show Data Table");
            formLayout.setDisable(true);
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
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.setMaximized(true);
    }

    /**
     * refreshes the TableView fetching data from the database
     * also updates the PieChart data
     */
    private void refreshTable() {
        List<Crop> rawList = cropDAO.getAll();
        ObservableList<Crop> cropList = FXCollections.observableArrayList(rawList);
        table.setItems(cropList);

        summaryChart.setData(CropAnalytics.generatePieChartData(rawList));
        timeChart.getData().clear();
        timeChart.getData().add(CropAnalytics.generateTimelineData(rawList));
    }
}