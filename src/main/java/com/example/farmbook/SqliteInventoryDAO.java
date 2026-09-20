package com.example.farmbook;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class SqliteInventoryDAO implements IInventoryDAO {

    private Connection connection;

    /**
     * Gets the shared database connection and makes sure the inventory table exists
     * so items can be saved and loaded straight away.
     */
    public SqliteInventoryDAO() {
        connection = SqliteConnection.getInstance();
        createTable();

    }

    /**
     * creates an inventory table in the database if it doesnt already exist
     * with an id, item name, category, unit and quantity for each item.
     */
    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS inventory ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "itemName VARCHAR NOT NULL,"
                    + "category VARCHAR NOT NULL,"
                    + "unit VARCHAR NOT NULL,"
                    + "quantity INTEGER NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a new inventory item into the database and sets the items id
     * to the id the database generated for it.
     * @param inventory the item to add to the inventory
     */
    @Override
    public void addInventoryItem(Inventory inventory){
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO inventory (itemName, category, unit, quantity) VALUES (?, ?, ?, ?)");
            statement.setString(1, inventory.getinventoryItemName());
            statement.setString(2, inventory.getInventoryCategory());
            statement.setString(3, inventory.getInventoryUnit());
            statement.setInt(4, inventory.getInventoryQuantity());
            statement.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                inventory.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing inventory item in the database with the items
     * current name, category, unit and quantity, matched by its ID
     * @param inventory the item with its updated details
     */
    @Override
    public void updateInventoryItem(Inventory inventory)
    {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE inventory SET itemName = ?, category = ?, unit = ?, quantity = ? WHERE id = ?");
            statement.setString(1, inventory.getinventoryItemName());
            statement.setString(2, inventory.getInventoryCategory());
            statement.setString(3, inventory.getInventoryUnit());
            statement.setInt(4, inventory.getInventoryQuantity());
            statement.setInt(5, inventory.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an inventory item from the database using its id.
     * @param inventory the item to delete
     */
    @Override
    public void deleteInventoryItem(Inventory inventory) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM inventory WHERE id = ?");
            statement.setInt(1, inventory.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a single inventory item in the database by its ID
     * @param id the id of the item to find
     * @return the matching item or null if not item has that id
     */
    @Override
    public Inventory getInventory(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM inventory WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String itemName = resultSet.getString("itemName");
                String category = resultSet.getString("category");
                String unit = resultSet.getString("unit");
                int quantity = resultSet.getInt("quantity");
                Inventory inventory = new Inventory(itemName, category, unit, quantity);
                inventory.setId(id);
                return inventory;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Loads every item from the inventory table in the database.
     * @return a list of all inventory items or an empty list if there are no items
     */
    public List<Inventory> getInventoryItems() {
        List<Inventory> inventorys = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM inventory";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String itemName = resultSet.getString("itemName");
                String category = resultSet.getString("category");
                String unit = resultSet.getString("unit");
                int quantity = resultSet.getInt("quantity");
                Inventory inventory = new Inventory(itemName, category, unit, quantity);
                inventory.setId(id);
                inventorys.add(inventory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inventorys;
    }


}
