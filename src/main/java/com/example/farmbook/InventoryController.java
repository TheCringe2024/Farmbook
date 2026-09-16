package com.example.farmbook;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.List;

public class InventoryController {
    @FXML
    private TextField itemNameTextField;
    @FXML
    private TextField categoryTextField;
    @FXML
    private TextField unitTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private VBox inventoryContainer;
    @FXML
    private ListView<Inventory> inventoryListView;
    private IInventoryDAO inventoryDAO;
    public InventoryController() {

        inventoryDAO = new SqliteInventoryDAO();

    }


    /**
     * Programmatically selects a contact in the list view and
     * updates the text fields with the contact's information.
     * @param inventory The contact to select.
     */
    private void selectInventory(Inventory inventory) {
        inventoryListView.getSelectionModel().select(inventory);
        itemNameTextField.setText(inventory.getinventoryItemName());
        categoryTextField.setText(inventory.getInventoryCategory());
        unitTextField.setText(inventory.getInventoryUnit());
        quantityTextField.setText(String.valueOf(inventory.getInventoryQuantity()));
    }

    /**
     * Renders a cell in the contacts list view by setting the text to the contact's full name.
     * @param inventoryListView The list view to render the cell for.
     * @return The rendered cell.
     */
    private ListCell<Inventory> renderCell(ListView<Inventory> inventoryListView) {
        return new ListCell<>() {
            /**
             * Handles the event when a contact is selected in the list view.
             * @param mouseEvent The event to handle.
             */
            private void onContactSelected(MouseEvent mouseEvent) {
                ListCell<Inventory> clickedCell = (ListCell<Inventory>) mouseEvent.getSource();
                // Get the selected contact from the list view
                Inventory selectedContact = clickedCell.getItem();
                if (selectedContact != null) selectInventory(selectedContact);
            }

            /**
             * Updates the item in the cell by setting the text to the contact's full name.
             * @param inventory The contact to update the cell with.
             * @param empty Whether the cell is empty.
             */
            @Override
            protected void updateItem(Inventory inventory, boolean empty) {
                super.updateItem(inventory, empty);
                // If the cell is empty, set the text to null, otherwise set it to the contact's full name
                if (empty || inventory == null) {
                    setText(null);
                    super.setOnMouseClicked(this::onContactSelected);
                } else {
                    setText(inventory.getinventoryItemName());
                }
            }
        };
    }

    /**
     * Synchronizes the contacts list view with the contacts in the database.
     */
    private void syncInventorys() {
        inventoryListView.getItems().clear();
        List<Inventory> contacts = inventoryDAO.getInventoryItems();
        boolean hasInventory = !contacts.isEmpty();
        if (hasInventory) {
            inventoryListView.getItems().addAll(contacts);
        }
        // Show / hide based on whether there are contacts
        //inventoryContainer.setVisible(hasInventory);
    }

    @FXML
    public void initialize() {
        inventoryListView.setCellFactory(this::renderCell);
        syncInventorys();
        inventoryListView.getSelectionModel().selectFirst();
        Inventory firstInventory = inventoryListView.getSelectionModel().getSelectedItem();
        if (firstInventory != null) {
            selectInventory(firstInventory);
        }
    }

    @FXML
    private void onEditConfirm() {
        // Get the selected contact from the list view
        Inventory selectedContact = inventoryListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            selectedContact.setinventoryItemName(itemNameTextField.getText());
            selectedContact.setinventoryCategory(categoryTextField.getText());
            selectedContact.setinventoryUnit(unitTextField.getText());
            selectedContact.setinventoryQuantity(Integer.parseInt(quantityTextField.getText()));
            inventoryDAO.updateInventoryItem(selectedContact);
            syncInventorys();
        }
    }

    @FXML
    private void onDelete() {
        // Get the selected contact from the list view
        Inventory selectedInventory = inventoryListView.getSelectionModel().getSelectedItem();
        if (selectedInventory != null) {
            inventoryDAO.deleteInventoryItem(selectedInventory);

            itemNameTextField.clear();
            categoryTextField.clear();
            unitTextField.clear();
            quantityTextField.clear();

            syncInventorys();
        }
    }
    @FXML
    private void onAdd() {
        // Default values for a new contact
        final String DEFAULT_ITEM_NAME = "SEEDX";
        final String DEFAULT_CATEGORY = "CATX";
        final String DEFAULT_UNIT = "KG";
        final int DEFAULT_QUANTITY = 0;
        Inventory newInventory = new Inventory(DEFAULT_ITEM_NAME, DEFAULT_CATEGORY, DEFAULT_UNIT, DEFAULT_QUANTITY);
        // Add the new contact to the database
        inventoryDAO.addInventoryItem(newInventory);
        syncInventorys();
        // Select the new contact in the list view
        // and focus the first name text field
        selectInventory(newInventory);
        itemNameTextField.requestFocus();
    }

    @FXML
    private void onCancel() {
        // Find the selected contact
        Inventory selectedInventory = inventoryListView.getSelectionModel().getSelectedItem();
        if (selectedInventory != null) {
            // Since the contact hasn't been modified,
            // we can just re-select it to refresh the text fields
            selectInventory(selectedInventory);
        }
    }
}
