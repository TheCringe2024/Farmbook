package com.example.farmbook;

import javafx.fxml.FXML;
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

    public InventoryController() {
        inventoryDAO = new SqliteInventoryDAO();
    }

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

    @FXML
    private void onCancel() {
        Inventory selected = inventoryTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectInventory(selected);
        }
    }

    @FXML
    private void onReturn() {
        Stage stage = (Stage) inventoryTableView.getScene().getWindow();
        new HomePage_application().start(stage);
    }
}