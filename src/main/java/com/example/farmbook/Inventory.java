package com.example.farmbook;

public class Inventory {
    private int id;
    private String inventoryItemName;
    private String inventoryCategory;
    private String inventoryUnit;
    private int inventoryQuantity;

    /**
     * Creates a new inventory item with its details, if the ID is not set here because
     * it is given by the database when the item is saved.
     * @param inventoryItemName the name of the item e.g. Wheat
     * @param inventoryCategory the category of th item belongs to e.g. Seeds
     * @param inventoryUnit the unit the item is measured in e.g. Packets
     * @param inventoryQuantity how many items stored e.g. 54
     */
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

    public String getInventoryItemName() { return inventoryItemName;}
    public void setInventoryItemName(String inventoryItemName) { this.inventoryItemName = inventoryItemName; }

    public String getInventoryCategory() { return inventoryCategory;}
    public void setInventoryCategory(String inventoryCategory) { this.inventoryCategory = inventoryCategory; }

    public String getInventoryUnit() { return inventoryUnit;}
    public void setInventoryUnit(String inventoryUnit) { this.inventoryUnit = inventoryUnit; }

    public int getInventoryQuantity() { return inventoryQuantity;}
    public void setInventoryQuantity(int inventoryQuantity) { this.inventoryQuantity = inventoryQuantity; }
}
