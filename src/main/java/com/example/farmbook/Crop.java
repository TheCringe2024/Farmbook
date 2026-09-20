package com.example.farmbook;

/**
 * Crop entity within the Farmbook application.
 * This class stores information about a specific planting record:
 * its name, type, quantity, planting date, and additional notes.
 */
public class Crop {
    private int id;
    private String plantName;
    private String cropType;
    private int amount;
    private String datePlanted;
    private String notes;

    /**
     * Constructs a new Crop without an ID. So the Database auto-generates it.
     * Used when creating a new record to be inserted into the database,
     *
     * @param plantName   name of the plant
     * @param cropType    category of the crop
     * @param amount      quantity planted
     * @param datePlanted date the crop was planted, String
     * @param notes       additional notes about the crop
     */
    public Crop(String plantName, String cropType, int amount, String datePlanted, String notes) {
        this.plantName = plantName;
        this.cropType = cropType;
        this.amount = amount;
        this.datePlanted = datePlanted;
        this.notes = notes;
    }

    /**
     * Constructs a Crop with an existing ID.
     * Used when reading an existing record.
     *
     * @param id          unique identifier for the crop
     * @param plantName   name of the plant
     * @param cropType    category of the crop
     * @param amount      quantity planted
     * @param datePlanted date the crop was planted, String
     * @param notes       additional notes about the crop
     */
    public Crop(int id, String plantName, String cropType, int amount, String datePlanted, String notes) {
        this.id = id;
        this.plantName = plantName;
        this.cropType = cropType;
        this.amount = amount;
        this.datePlanted = datePlanted;
        this.notes = notes;
    }

    /**
     * Getters && Setters
     */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }
    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public String getDatePlanted() { return datePlanted; }
    public void setDatePlanted(String datePlanted) { this.datePlanted = datePlanted; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}