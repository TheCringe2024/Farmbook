package com.example.farmbook.model;

/**
 * Represents one crop record.
 */
public class Crop {
    private int id;
    private String type;
    private String fieldPlot;
    private int quantity;
    private String datePlanted;

    /**
     * Creates a new crop record.
     * @param type crop type
     * @param fieldPlot field or plot planted in
     * @param quantity amount planted
     * @param datePlanted date planted
     */
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

    /**
     * Shows the crop as readable text.
     * @return formatted crop summary
     */
    @Override
    public String toString() {
        return type + " — " + fieldPlot + " (" + quantity + " planted " + datePlanted + ")";
    }
}
