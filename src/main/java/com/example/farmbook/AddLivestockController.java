package com.example.farmbook;

import com.example.farmbook.dao.LivestockDAO;
import com.example.farmbook.service.LivestockService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controls the Add Livestock screen.
 *
 * UI responsibilities remain in the controller while livestock
 * business logic is delegated to LivestockService.
 */
public class AddLivestockController {

    @FXML
    private TextField speciesField;

    @FXML
    private TextField identifierField;

    @FXML
    private TextField dateAcquiredField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button backButton;

    private final LivestockService livestockService =
            new LivestockService(new LivestockDAO());

    /**
     * Sends user input to the livestock service and displays
     * the resulting status to the user.
     */
    @FXML
    private void handleSaveLivestock() {
        String species = speciesField.getText();
        String identifier = identifierField.getText();
        String dateAcquired = dateAcquiredField.getText();

        LivestockService.SaveResult result =
                livestockService.saveLivestock(
                        species,
                        identifier,
                        dateAcquired
                );

        switch (result) {
            case MISSING_FIELDS -> showError(
                    "Please fill in all fields."
            );

            case INVALID_DATE -> showError(
                    "Date must use YYYY-MM-DD format."
            );

            case PERSISTENCE_ERROR -> showError(
                    "Failed to save livestock. Please try again."
            );

            case SUCCESS -> {
                showSuccess(
                        "Saved: "
                                + species
                                + " - "
                                + identifier
                                + " (acquired "
                                + dateAcquired
                                + ")"
                );

                speciesField.clear();
                identifierField.clear();
                dateAcquiredField.clear();
            }
        }
    }

    /**
     * Displays an error message.
     *
     * @param message message to show
     */
    private void showError(String message) {
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setText(message);
    }

    /**
     * Displays a successful operation message.
     *
     * @param message message to show
     */
    private void showSuccess(String message) {
        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setText(message);
    }

    /**
     * Returns to the livestock list.
     */
    @FXML
    private void handleBack() {
        try {
            Stage stage =
                    (Stage) backButton.getScene().getWindow();

            FXMLLoader loader =
                    new FXMLLoader(
                            HelloApplication.class.getResource(
                                    "livestock-list-view.fxml"
                            )
                    );

            Scene scene =
                    new Scene(loader.load(), 800, 600);

            stage.setScene(scene);
            stage.setTitle(
                    "Farmbook - Livestock List"
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}