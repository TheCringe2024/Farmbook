package com.example.farmbook;
import java.util.List;

public interface IInventoryDAO {

    public void addInventoryItem(Inventory inventory);
    public void updateInventoryItem(Inventory inventory);
    public void deleteInventoryItem(Inventory inventory);
    public Inventory getInventory(int id);
    public List<Inventory> getInventoryItems();
}

