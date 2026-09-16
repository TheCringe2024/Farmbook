package com.example.farmbook;

public class Inventory {
    private int id;
    private String inventoryItemName;
    private String inventoryCategory;
    private String inventoryUnit;
    private int inventoryQuantity;

    public Inventory(String inventoryItemName,  String inventoryCategory, String inventoryUnit, int inventoryQuantity){

        this.inventoryItemName = inventoryItemName;
        this.inventoryCategory = inventoryCategory;
        this.inventoryUnit = inventoryUnit;
        this.inventoryQuantity = inventoryQuantity;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {this.id = id;}

    public String getinventoryItemName() { return inventoryItemName;}
    public void setinventoryItemName(String inventoryItemName) { this.inventoryItemName = inventoryItemName; }

    public String getInventoryCategory() { return inventoryCategory;}
    public void setinventoryCategory(String inventoryCategory) { this.inventoryCategory = inventoryCategory; }

    public String getInventoryUnit() { return inventoryUnit;}
    public void setinventoryUnit(String inventoryUnit) { this.inventoryUnit = inventoryUnit; }

    public int getInventoryQuantity() { return inventoryQuantity;}
    public void setinventoryQuantity(int inventoryQuantity) { this.inventoryQuantity = inventoryQuantity; }
}
