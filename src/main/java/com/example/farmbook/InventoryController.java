package com.example.farmbook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
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
    private TableView<Inventory> inventoryTableView;
    @FXML
    private TableColumn<Inventory, String> itemNameColumn;
    @FXML
    private TableColumn<Inventory, String> categoryColumn;
    @FXML
    private TableColumn<Inventory, String> unitColumn;
    @FXML
    private TableColumn<Inventory, Integer> quantityColumn;

    private IInventoryDAO inventoryDAO;

    /**
     * Creates the controller and setups up the SQLite DAO used to load and save the inventory items in the database
     */
    public InventoryController() {
        inventoryDAO = new SqliteInventoryDAO();
    }

    /**
     * Runs when the inventory page is loaded and links each table column to the matching item detail setting
     * up  a row to fill the text fields. It sorts the tables by item nanme ignoring case and then loads the items from
     * the database and selects the first one.
     */
    @FXML
    public void initialize() {

        //temNameColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryItemName"));
        itemNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getinventoryItemName()));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryCategory"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryUnit"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryQuantity"));

        inventoryTableView.setPlaceholder(new Label("No inventory items yet. Click New to add one."));

        TableViewSelectionModel<Inventory> selectionModel = inventoryTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedItemProperty().addListener((observable, oldItem, newItem) -> {
            if (newItem != null) selectInventory(newItem);
        });

        Comparator<String> ignoreCase = Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER);
        itemNameColumn.setComparator(ignoreCase);
        categoryColumn.setComparator(ignoreCase);
        unitColumn.setComparator(ignoreCase);
        itemNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        inventoryTableView.getSortOrder().add(itemNameColumn);

        syncInventorys();
        selectionModel.selectFirst();
    }

    /**
     * Selects an item in the table and fills the text fields with its details.
     * @param inventory The item to select.
     */
    private void selectInventory(Inventory inventory) {
        inventoryTableView.getSelectionModel().select(inventory);
        itemNameTextField.setText(inventory.getinventoryItemName());
        categoryTextField.setText(inventory.getInventoryCategory());
        unitTextField.setText(inventory.getInventoryUnit());
        quantityTextField.setText(String.valueOf(inventory.getInventoryQuantity()));
    }

    /**
     * Reloads the table from the database and re-applies the current sort.
     */
    private void syncInventorys() {
        inventoryTableView.getItems().clear();
        List<Inventory> items = inventoryDAO.getInventoryItems();
        if (!items.isEmpty()) {
            inventoryTableView.getItems().addAll(items);
        }
        inventoryTableView.sort();
    }
    /**
     * Gets the details from the text fields and saves them to the selected
     * item in the database and then reloads the table to show the changes.
     */
    @FXML
    private void onEditConfirm() {
        Inventory selected = inventoryTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setinventoryItemName(itemNameTextField.getText());
            selected.setinventoryCategory(categoryTextField.getText());
            selected.setinventoryUnit(unitTextField.getText());
            selected.setinventoryQuantity(Integer.parseInt(quantityTextField.getText()));
            inventoryDAO.updateInventoryItem(selected);
            syncInventorys();
        }
    }

    /**
     * Deletes the selected item from the database and clears the text fields and reloads the table.
     */
    @FXML
    private void onDelete() {
        Inventory selected = inventoryTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            inventoryDAO.deleteInventoryItem(selected);

            itemNameTextField.clear();
            categoryTextField.clear();
            unitTextField.clear();
            quantityTextField.clear();

            syncInventorys();
        }
    }

    /**
     * Adds a new item with default details to the database. Selects it in the table and puts the
     * cursor in the item name field so the user can change the details straight away.
     */
    @FXML
    private void onAdd() {
        final String DEFAULT_ITEM_NAME = "Wheat";
        final String DEFAULT_CATEGORY = "Seeds";
        final String DEFAULT_UNIT = "Packets";
        final int DEFAULT_QUANTITY = 5;
        Inventory newInventory = new Inventory(DEFAULT_ITEM_NAME, DEFAULT_CATEGORY, DEFAULT_UNIT, DEFAULT_QUANTITY);
        inventoryDAO.addInventoryItem(newInventory);
        syncInventorys();
        selectInventory(newInventory);
        itemNameTextField.requestFocus();
    }

    /**
     * Clears any unsaved edits in the table
     */
    @FXML
    private void onCancel() {
        Inventory selected = inventoryTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectInventory(selected);
        }
    }

    /**
     * Loads the homepage and swaps it into the current window when the return button is clicked.
     * @param actionEvent the click event from the return button
     * @throws IOException if the homepage-view.fxml file cant be loaded
     */
    public void onReturn(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("homepage-view.fxml"));
        Stage stage = (Stage) itemNameTextField.getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 1000, 800));
    }
}