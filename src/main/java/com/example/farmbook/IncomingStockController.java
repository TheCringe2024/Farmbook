package com.example.farmbook;

import com.example.farmbook.dao.ItemDAO;
import com.example.farmbook.model.Item;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * Controls the Incoming Stock screen.
 */
public class IncomingStockController {

    @FXML
    private ComboBox<Item> itemComboBox;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField sourceField;

    @FXML
    private Label statusLabel;

    private final ItemDAO itemDAO = new ItemDAO();

    /**
     * Loads items into the dropdown when the screen opens.
     */
    @FXML
    public void initialize() {
        List<Item> items = itemDAO.findAll();
        itemComboBox.setItems(FXCollections.observableArrayList(items));
    }

    /**
     * Validates input and adds stock to the selected item.
     */
    @FXML
    private void handleAddStock() {
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
            itemDAO.addStock(selectedItem.getId(), quantity);

            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Added " + quantity + " " + selectedItem.getUnit()
                    + " to " + selectedItem.getName());

            quantityField.clear();
            dateField.clear();
            sourceField.clear();

            itemComboBox.setItems(FXCollections.observableArrayList(itemDAO.findAll()));
        } catch (NumberFormatException e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Quantity must be a number.");
        }
    }
}
