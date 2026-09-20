package com.example.farmbook;

import com.example.farmbook.dao.LivestockDAO;
import com.example.farmbook.model.Livestock;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controls the Add Livestock screen.
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

    private final LivestockDAO livestockDAO = new LivestockDAO();

    /**
     * Validates and saves a new animal.
     */
    @FXML
    private void handleSaveLivestock() {
        String species = speciesField.getText();
        String identifier = identifierField.getText();
        String dateAcquired = dateAcquiredField.getText();

        if (!LivestockValidator.isValidDate(dateAcquired)) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Date must use YYYY-MM-DD format.");
            return;
        }

        if (species.isBlank() || identifier.isBlank() || dateAcquired.isBlank()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        Livestock livestock = new Livestock(species, identifier, dateAcquired);
        livestockDAO.save(livestock);

        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setText("Saved: " + livestock);

        speciesField.clear();
        identifierField.clear();
        dateAcquiredField.clear();
    }

    /**
     * Returns to the livestock list.
     */
    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("livestock-list-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            stage.setScene(scene);
            stage.setTitle("Farmbook - Livestock List");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
