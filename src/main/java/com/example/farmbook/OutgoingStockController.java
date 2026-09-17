package com.example.farmbook;

import com.example.farmbook.dao.ItemDAO;
import com.example.farmbook.model.Item;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class OutgoingStockController {

    @FXML
    private ComboBox<Item> itemComboBox;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField dateField;

    @FXML
    private Label statusLabel;

    private final ItemDAO itemDAO = new ItemDAO();

    @FXML
    public void initialize() {
        List<Item> items = itemDAO.findAll();
        itemComboBox.setItems(FXCollections.observableArrayList(items));
    }

    @FXML
    private void handleRemoveStock() {
        Item selectedItem = itemComboBox.getValue();
        String quantityText = quantityField.getText();
        String date = dateField.getText();

        if (selectedItem == null) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Please select an item.");
            return;
        }
        if (quantityText.isBlank() || date.isBlank()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Please fill in quantity and date.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            boolean success = itemDAO.removeStock(selectedItem.getId(), quantity);

            if (success) {
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("Removed " + quantity + " " + selectedItem.getUnit()
                        + " from " + selectedItem.getName());
                quantityField.clear();
                dateField.clear();
                itemComboBox.setItems(FXCollections.observableArrayList(itemDAO.findAll()));
            } else {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Not enough stock available.");
            }
        } catch (NumberFormatException e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Quantity must be a number.");
        }
    }
}
