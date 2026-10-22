
package com.example.farmbook;

import com.example.farmbook.dao.LivestockDAO;
import com.example.farmbook.model.Livestock;
import com.example.farmbook.service.LivestockService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
/**
 * Controls the Livestock List screen.
 * Supports listing, searching, adding and deleting livestock.
 */
public class LivestockListController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Livestock> livestockListView;

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    private final LivestockService livestockService =
            new LivestockService(new LivestockDAO());

    /**
     * Initializes the screen and search functionality.
     */
    @FXML
    public void initialize() {
        loadLivestock();

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> loadLivestock()
        );
    }

    /**
     * Loads livestock records matching the search keyword.
     */
    private void loadLivestock() {
        List<Livestock> animals =
                livestockService.searchLivestock(searchField.getText());

        livestockListView.setItems(
                FXCollections.observableArrayList(animals)
        );

        livestockListView.setPlaceholder(
                new Label("No matching livestock records.")
        );
    }

    /**
     * Deletes the selected livestock record after confirmation.
     */
    @FXML
    private void handleDeleteSelected() {
        Livestock selected =
                livestockListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Please select an animal to delete."
            ).showAndWait();
            return;
        }

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete "
                        + selected.getIdentifier() + "?",
                ButtonType.OK,
                ButtonType.CANCEL
        );

        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Livestock Record");

        if (confirmation.showAndWait()
                .orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        boolean deleted =
                livestockService.deleteLivestock(selected.getId());

        if (deleted) {
            loadLivestock();

            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Livestock record deleted successfully."
            ).showAndWait();
        } else {
            new Alert(
                    Alert.AlertType.ERROR,
                    "Failed to delete livestock record."
            ).showAndWait();
        }
    }

    /**
     * Allows the user to edit a selected livestock record.
     * Validation is handled by LivestockService.
     */
    @FXML
    private void handleEditSelected() {
        Livestock selected =
                livestockListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Please select an animal to edit."
            ).showAndWait();
            return;
        }

        TextField speciesField =
                new TextField(selected.getSpecies());

        TextField identifierField =
                new TextField(selected.getIdentifier());

        TextField dateField =
                new TextField(selected.getDateAcquired());

        dateField.setPromptText("YYYY-MM-DD");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));

        form.addRow(0, new Label("Species:"), speciesField);
        form.addRow(1, new Label("Identifier:"), identifierField);
        form.addRow(2, new Label("Date acquired:"), dateField);
        form.add(errorLabel, 1, 3);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Livestock");
        dialog.setHeaderText(
                "Editing: " + selected.getIdentifier()
        );

        dialog.getDialogPane().setContent(form);

        ButtonType saveType = new ButtonType(
                "Save Changes",
                ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane().getButtonTypes().addAll(
                saveType,
                ButtonType.CANCEL
        );

        Button saveButton = (Button)
                dialog.getDialogPane().lookupButton(saveType);

        saveButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    LivestockService.UpdateResult result =
                            livestockService.updateLivestock(
                                    selected.getId(),
                                    speciesField.getText(),
                                    identifierField.getText(),
                                    dateField.getText()
                            );

                    if (result ==
                            LivestockService.UpdateResult.SUCCESS) {
                        loadLivestock();
                    } else {
                        String message = switch (result) {
                            case INVALID_ID ->
                                    "Invalid livestock record.";
                            case MISSING_FIELDS ->
                                    "Please fill in all fields.";
                            case INVALID_DATE ->
                                    "Date must use YYYY-MM-DD format.";
                            case PERSISTENCE_ERROR ->
                                    "Could not update the record.";
                            case SUCCESS -> "";
                        };

                        errorLabel.setText(message);
                        event.consume();
                    }
                }
        );

        dialog.showAndWait();
    }


    /**
     * Opens the Add Livestock screen.
     */
    @FXML
    private void handleAddNew() {
        try {
            Stage stage =
                    (Stage) addButton.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource(
                            "add-livestock-view.fxml"
                    )
            );

            Scene scene = new Scene(loader.load(), 800, 600);

            stage.setScene(scene);
            stage.setTitle("FarmBook - Add Livestock");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns to the dashboard.
     *
     * @throws IOException if the dashboard cannot be loaded
     */
    @FXML
    private void handleBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                LivestockListController.class.getResource(
                        "homepage-view.fxml"
                )
        );

        Stage stage =
                (Stage) livestockListView.getScene().getWindow();

        stage.setScene(new Scene(loader.load(), 1000, 800));
        stage.setTitle("FarmBook");
        stage.sizeToScene();
        stage.centerOnScreen();
    }
}
