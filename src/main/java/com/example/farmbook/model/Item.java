package com.example.farmbook.model;

/**
 * Represents one inventory item.
 */
public class Item {
    private int id;
    private String name;
    private String category;
    private String unit;
    private int quantity;

    /**
     * Creates a new inventory item.
     * @param name item name
     * @param category item category
     * @param unit unit of measurement
     * @param quantity starting quantity
     */
    public Item(String name, String category, String unit, int quantity) {
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getUnit() { return unit; }
    public int getQuantity() { return quantity; }

    /**
     * Shows the item as readable text.
     * @return formatted item summary
     */
    @Override
    public String toString() {
        return name + " (" + category + ") — " + quantity + " " + unit;
    }
}
