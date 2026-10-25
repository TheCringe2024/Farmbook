package com.example.farmbook;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Crop entity within the Farmbook application.
 * This class stores information about a specific planting record:
 * its name, type, quantity, planting date, and additional notes.
 */
public class Crop {
    private final IntegerProperty id;
    private final StringProperty plantName;
    private final StringProperty cropType;
    private final IntegerProperty amount;
    private final StringProperty datePlanted;
    private final StringProperty notes;
    private final StringProperty location;
    private final StringProperty status;

    /**
     * Constructs a new Crop without an ID. So the Database auto-generates it.
     * Used when creating a new record to be inserted into the database,
     *
     * @param plantName   name of the plant
     * @param cropType    category of the crop
     * @param amount      quantity planted
     * @param datePlanted date the crop was planted, String
     * @param notes       additional notes about the crop
     * @param location    field/plot location
     * @param status      current growing status
     */
    public Crop(String plantName, String cropType, int amount, String datePlanted, String notes, String location, String status) {
        this.id = new SimpleIntegerProperty(0);
        this.plantName = new SimpleStringProperty(plantName);
        this.cropType = new SimpleStringProperty(cropType);
        this.amount = new SimpleIntegerProperty(amount);
        this.datePlanted = new SimpleStringProperty(datePlanted);
        this.notes = new SimpleStringProperty(notes);
        this.location = new SimpleStringProperty(location);
        this.status = new SimpleStringProperty(status);
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
     * @param location    field/plot
     * @param status      Growing, Harvested or Failed
     *
     */
    public Crop(int id, String plantName, String cropType, int amount, String datePlanted, String notes, String location, String status) {
        this(plantName, cropType, amount, datePlanted, notes, location, status);
        this.id.set(id);
    }

    /**
     * Getters && Setters
     */

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getPlantName() { return plantName.get(); }
    public void setPlantName(String plantName) { this.plantName.set(plantName); }
    public StringProperty plantNameProperty() { return plantName; }

    public String getCropType() { return cropType.get(); }
    public void setCropType(String cropType) { this.cropType.set(cropType); }
    public StringProperty cropTypeProperty() { return cropType; }

    public int getAmount() { return amount.get(); }
    public void setAmount(int amount) { this.amount.set(amount); }
    public IntegerProperty amountProperty() { return amount; }

    public String getDatePlanted() { return datePlanted.get(); }
    public void setDatePlanted(String datePlanted) { this.datePlanted.set(datePlanted); }
    public StringProperty datePlantedProperty() { return datePlanted; }

    public String getNotes() { return notes.get(); }
    public void setNotes(String notes) { this.notes.set(notes); }
    public StringProperty notesProperty() { return notes; }

    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }
    public StringProperty locationProperty() { return location; }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }
}