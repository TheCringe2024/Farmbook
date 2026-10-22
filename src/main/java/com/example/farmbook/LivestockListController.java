
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
