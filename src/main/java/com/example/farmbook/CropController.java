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
import java.util.stream.Collectors;

/**
 * Controller for Crop UI view (MVC Pattern).
 * Handles user interactions, delegates data fetching to the DAO layer,
 * and leverages Factory instantiation and Dependency Injection.
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

    @FXML private Label lblError;
    @FXML private TextField nameInput;
    @FXML private TextField notesInput;
    @FXML private TextField locationInput;
    @FXML private DatePicker dateInput;

    @FXML private ComboBox<String> typeInput;
    @FXML private ComboBox<String> amountInput;
    @FXML private ComboBox<String> statusInput;
    @FXML private CheckBox chkShowInactive;

    // Sub-menus
    @FXML private ListView<String> needsList;
    @FXML private TextField newNeedInput;
    @FXML private ListView<String> growthList;
    @FXML private TextField newGrowthInput;

    @FXML private Button btnToggleView;
    @FXML private Button btnBack;

    // Interface used for loose coupling
    private ICropDAO cropDAO;
    private int viewState = 0;

    /**
     * Dependency Injection setup.
     * Allows inserting a mock DAO for testing instead of relying on the concrete SQLite DB.
     */
    public void setCropDAO(ICropDAO cropDAO) {
        this.cropDAO = cropDAO;
    }

    @FXML
    public void initialize() {
        // Fallback to Singleton if no dependency was injected prior to init
        if (this.cropDAO == null) {
            this.cropDAO = CropDAO.getInstance();
        }

        cropDAO.createTable();

        // Populate predefined items for the ComboBox dropdowns
        typeInput.getItems().addAll("Grain", "Vegetable", "Fruit", "Legume", "Root", "Forage");
        amountInput.getItems().addAll("10", "50", "100", "500", "1000", "5000");
        statusInput.getItems().addAll("Growing", "Harvested", "Failed");
        statusInput.setValue("Growing");

        // The Observer pattern here automatically reflects Model property updates to the View
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("plantName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("cropType"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("datePlanted"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        refreshTable();

        // Register observer listener for selection changes
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lblError.setText("");
                nameInput.setText(newSelection.getPlantName());
                typeInput.setValue(newSelection.getCropType());
                amountInput.setValue(String.valueOf(newSelection.getAmount()));

                if (newSelection.getDatePlanted() != null && !newSelection.getDatePlanted().isEmpty()) {
                    dateInput.setValue(java.time.LocalDate.parse(newSelection.getDatePlanted()));
                }
                notesInput.setText(newSelection.getNotes());
                locationInput.setText(newSelection.getLocation());
                statusInput.setValue(newSelection.getStatus());

                if(cropDAO instanceof CropDAO daoConcrete) {
                    needsList.setItems(FXCollections.observableArrayList(daoConcrete.getNeeds(newSelection.getId())));
                    growthList.setItems(FXCollections.observableArrayList(daoConcrete.getGrowth(newSelection.getId())));
                }
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

        if (CropValidator.hasBlankFields(nameInput.getText(), amountText)) {
            lblError.setStyle("-fx-text-fill: #ff5959;"); //Red
            lblError.setText("Error: Name and Amount are required.");
            return;
        }
        if (!CropValidator.isNumeric(amountText)) {
            lblError.setStyle("-fx-text-fill: #ff5959;"); //Red
            lblError.setText("Error: Amount must be a number.");
            return;
        }

        // Factory Pattern applied here for dynamic instantiation
        Crop newCrop = CropFactory.createNewCrop(
                nameInput.getText(), type,
                Integer.parseInt(amountText),
                dateInput.getValue() != null ? dateInput.getValue().toString() : "",
                notesInput.getText(), locationInput.getText(), statusInput.getValue()
        );
        cropDAO.insert(newCrop);

        lblError.setStyle("-fx-text-fill: #00ff00;"); // Green for success
        lblError.setText("Crop successfully added!");

        // Clear the form fields after adding
        nameInput.clear();
        amountInput.getEditor().clear();
        notesInput.clear();
        locationInput.clear();

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
            if (!CropValidator.isNumeric(amountText)) {
                lblError.setText("Error: Amount must be a number.");
                return;
            }

            // Using Observer Pattern: Because properties are updated,
            // the TableView updates in real-time as well, but we still commit to the DB.
            selected.setPlantName(nameInput.getText());
            selected.setCropType(typeInput.getEditor().getText());
            selected.setAmount(Integer.parseInt(amountText));
            selected.setDatePlanted(dateInput.getValue() != null ? dateInput.getValue().toString() : "");
            selected.setNotes(notesInput.getText());
            selected.setLocation(locationInput.getText());
            selected.setStatus(statusInput.getValue());

            cropDAO.update(selected);
            lblError.setText("Crop updated.");
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

    @FXML
    void handleAddNeed(ActionEvent event) {
        Crop selected = table.getSelectionModel().getSelectedItem();
        String need = newNeedInput.getText();
        if (selected != null && need != null && !need.isEmpty() && cropDAO instanceof CropDAO daoConcrete) {
            daoConcrete.addNeed(selected.getId(), need);
            newNeedInput.clear();
            needsList.setItems(FXCollections.observableArrayList(daoConcrete.getNeeds(selected.getId())));
        }
    }

    @FXML
    void handleAddGrowth(ActionEvent event) {
        Crop selected = table.getSelectionModel().getSelectedItem();
        String growth = newGrowthInput.getText();
        if (selected != null && growth != null && !growth.isEmpty() && cropDAO instanceof CropDAO daoConcrete) {
            String today = java.time.LocalDate.now().toString();
            daoConcrete.addGrowth(selected.getId(), today, growth);
            newGrowthInput.clear();
            growthList.setItems(FXCollections.observableArrayList(daoConcrete.getGrowth(selected.getId())));
        }
    }

    @FXML
    void onFilterToggled(ActionEvent event) {
        refreshTable();
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

        // Filter out Harvested/Failed unless Checkbox is checked
        if (!chkShowInactive.isSelected()) {
            rawList = rawList.stream()
                    .filter(c -> "Growing".equalsIgnoreCase(c.getStatus()))
                    .collect(Collectors.toList());
        }

        ObservableList<Crop> cropList = FXCollections.observableArrayList(rawList);
        table.setItems(cropList);
        summaryChart.setData(CropAnalytics.generatePieChartData(rawList));
        timeChart.getData().clear();
        timeChart.getData().add(CropAnalytics.generateTimelineData(rawList));
    }
}