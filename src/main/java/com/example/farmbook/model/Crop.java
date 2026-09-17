package com.example.farmbook.model;

public class Crop {
    private int id;
    private String type;
    private String fieldPlot;
    private int quantity;
    private String datePlanted;

    public Crop(String type, String fieldPlot, int quantity, String datePlanted) {
        this.type = type;
        this.fieldPlot = fieldPlot;
        this.quantity = quantity;
        this.datePlanted = datePlanted;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public String getFieldPlot() { return fieldPlot; }
    public int getQuantity() { return quantity; }
    public String getDatePlanted() { return datePlanted; }

    @Override
    public String toString() {
        return type + " — " + fieldPlot + " (" + quantity + ") planted " + datePlanted;
    }
}
