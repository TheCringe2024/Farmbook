package com.example.farmbook;

/**
 * Stock in and stock out rules for the app's inventory.
 */
public class InventoryStockService {

    private final IInventoryDAO dao;

    public InventoryStockService(IInventoryDAO dao) {
        this.dao = dao;
    }

    /** Adds stock. Returns false if the amount is not positive or the item is unknown. */
    public boolean addStock(Inventory item, int amount) {
        return false;
    }

    /** Removes stock. Returns false if the amount is not positive, too large, or the item is unknown. */
    public boolean removeStock(Inventory item, int amount) {
        return false;
    }
}
