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
        return changeStock(item, amount, amount);
    }

    /** Removes stock. Returns false if the amount is not positive, too large, or the item is unknown. */
    public boolean removeStock(Inventory item, int amount) {
        return changeStock(item, amount, -amount);
    }

    /**
     * Applies a stock change if it is allowed, then saves it.
     * @param amount how much the user asked to move (must be positive)
     * @param change the signed change to the quantity
     */
    private boolean changeStock(Inventory item, int amount, int change) {
        if (amount <= 0) {
            return false;
        }
        Inventory current = dao.getInventory(item.getId());
        if (current == null || current.getInventoryQuantity() + change < 0) {
            return false;
        }
        current.setInventoryQuantity(current.getInventoryQuantity() + change);
        dao.updateInventoryItem(current);
        return true;
    }
}
