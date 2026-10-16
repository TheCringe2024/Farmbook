package com.example.farmbook;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;

/**
 * Controls the stock in / out screen for the app's real inventory.
 */
public class StockMovementController {

    @FXML
    private ComboBox<Inventory> itemComboBox;

    @FXML
    private TextField amountField;

    @FXML
    private Label statusLabel;

    private final IInventoryDAO dao = new SqliteInventoryDAO();
    private final InventoryStockService service = new InventoryStockService(dao);

    /** Fills the dropdown when the screen opens. */
    @FXML
    public void initialize() {
        itemComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Inventory item) {
                if (item == null) {
                    return "";
                }
                return item.getInventoryItemName() + " - " + item.getInventoryQuantity()
                        + " " + item.getInventoryUnit();
            }

            @Override
            public Inventory fromString(String text) {
                return null;
            }
        });
        loadItems();
    }

    private void loadItems() {
        itemComboBox.setItems(FXCollections.observableArrayList(dao.getInventoryItems()));
    }

    @FXML
    protected void onAddStock() {
        move(true);
    }

    @FXML
    protected void onRemoveStock() {
        move(false);
    }

    /** Checks the input, asks the service to move the stock, and shows the result. */
    private void move(boolean adding) {
        Inventory selected = itemComboBox.getValue();
        if (selected == null) {
            showMessage("Please choose an item.", false);
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(amountField.getText().trim());
        } catch (NumberFormatException e) {
            showMessage("Amount must be a whole number.", false);
            return;
        }
        boolean ok = adding ? service.addStock(selected, amount) : service.removeStock(selected, amount);
        if (!ok) {
            showMessage(adding ? "Amount must be more than zero."
                    : "Not enough stock, or the amount is not more than zero.", false);
            return;
        }
        int id = selected.getId();
        loadItems();
        for (Inventory item : itemComboBox.getItems()) {
            if (item.getId() == id) {
                itemComboBox.setValue(item);
                break;
            }
        }
        amountField.clear();
        showMessage((adding ? "Added " : "Removed ") + amount + " successfully.", true);
    }

    private void showMessage(String text, boolean success) {
        statusLabel.setStyle("-fx-text-fill: " + (success ? "#7CFC9A" : "#ff8080") + ";");
        statusLabel.setText(text);
    }

    /** Goes back to the inventory screen. */
    @FXML
    protected void onBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("inventory-add-view.fxml"));
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 1920, 1080));
        stage.sizeToScene();
        stage.centerOnScreen();
    }
}
