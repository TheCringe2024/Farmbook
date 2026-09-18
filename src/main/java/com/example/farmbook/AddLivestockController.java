package com.example.farmbook;

import com.example.farmbook.dao.LivestockDAO;
import com.example.farmbook.model.Livestock;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddLivestockController {

    @FXML
    private TextField speciesField;

    @FXML
    private TextField identifierField;

    @FXML
    private TextField dateAcquiredField;

    @FXML
    private Label statusLabel;

    private final LivestockDAO livestockDAO = new LivestockDAO();

    @FXML
    private void handleSaveLivestock() {
        String species = speciesField.getText();
        String identifier = identifierField.getText();
        String dateAcquired = dateAcquiredField.getText();

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
}
