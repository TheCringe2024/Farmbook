package com.example.farmbook;

public class Crop {
    private int id;
    private String plantName;
    private String cropType;
    private int amount;
    private String datePlanted;
    private String notes;

    // Constructor for creating new records (no ID)
    public Crop(String plantName, String cropType, int amount, String datePlanted, String notes) {
        this.plantName = plantName;
        this.cropType = cropType;
        this.amount = amount;
        this.datePlanted = datePlanted;
        this.notes = notes;
    }

    // Constructor for retrieving records from database (has ID)
    public Crop(int id, String plantName, String cropType, int amount, String datePlanted, String notes) {
        this.id = id;
        this.plantName = plantName;
        this.cropType = cropType;
        this.amount = amount;
        this.datePlanted = datePlanted;
        this.notes = notes;
    }

    // Getters && Setters
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