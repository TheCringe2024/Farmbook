package com.example.farmbook.model;

public class Item {
    private int id;
    private String name;
    private String category;
    private String unit;
    private int quantity;

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

    @Override
    public String toString() {
        return name + " (" + category + ") — " + quantity + " " + unit;
    }
}
