package com.example.farmbook;

/**
 * Factory class for creating Crop objects.
 * Encapsulates instantiation logic, making the code more flexible if we
 * later need to return subclasses (e.g., OrganicCrop vs GMOCrop)
 * without modifying the controller or DAO layer.
 */
public class CropFactory {

    /**
     * Creates a new Crop instance intended for database insertion (ID will be auto-generated).
     */
    public static Crop createNewCrop(String plantName, String cropType, int amount, String datePlanted, String notes, String location, String status) {
        return new Crop(plantName, cropType, amount, datePlanted, notes, location, status);
    }

    /**
     * Reconstitutes an existing Crop instance from database records.
     */
    public static Crop createExistingCrop(int id, String plantName, String cropType, int amount, String datePlanted, String notes, String location, String status) {
        return new Crop(id, plantName, cropType, amount, datePlanted, notes, location, status);
    }
}