package com.example.farmbook;
import java.util.List;

public interface IInventoryDAO {

    //public void addInventoryItemName(Inventory inventory);
   // public void addInventoryCategory(Inventory inventory);
   // public void addInventoryUnit(Inventory inventory);
   // public void addInventoryQuantity(Inventory inventory);

    public void addInventoryItem(Inventory inventory);
    public void updateInventoryItem(Inventory inventory);
    public void deleteInventoryItem(Inventory inventory);
    public Inventory getInventory(int id);
    public List<Inventory> getInventoryItems();
}

