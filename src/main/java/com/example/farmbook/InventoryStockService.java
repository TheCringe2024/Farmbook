package com.example.farmbook;

/**
 * Stock in and out rules for the app's inventory.
 */
public class InventoryStockService {

    private final IInventoryDAO dao;

    public InventoryStockService(IInventoryDAO dao) {
        this.dao = dao;
    }

    /** Adds stock. Returns false if the amount is not positive or the item is unknown. */
    public boolean addStock(Inventory item, int amount) {
        if (amount <= 0) {
            return false;
        }
        Inventory current = dao.getInventory(item.getId());
        if (current == null) {
            return false;
        }
        current.setInventoryQuantity(current.getInventoryQuantity() + amount);
        dao.updateInventoryItem(current);
        return true;
    }

    /** Removes stock. Returns false if the amount is not positive, too large, or the item is unknown. */
    public boolean removeStock(Inventory item, int amount) {
        if (amount <= 0) {
            return false;
        }
        Inventory current = dao.getInventory(item.getId());
        if (current == null || current.getInventoryQuantity() < amount) {
            return false;
        }
        current.setInventoryQuantity(current.getInventoryQuantity() - amount);
        dao.updateInventoryItem(current);
        return true;
    }
}
