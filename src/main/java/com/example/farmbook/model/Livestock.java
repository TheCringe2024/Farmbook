package com.example.farmbook.model;

/**
 * Represents one animal record.
 */
public class Livestock {
    private int id;
    private String species;
    private String identifier;
    private String dateAcquired;

    /**
     * Creates a new animal record.
     * @param species type of animal
     * @param identifier tag or name
     * @param dateAcquired date acquired or born
     */
    public Livestock(String species, String identifier, String dateAcquired) {
        this.species = species;
        this.identifier = identifier;
        this.dateAcquired = dateAcquired;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSpecies() { return species; }
    public String getIdentifier() { return identifier; }
    public String getDateAcquired() { return dateAcquired; }

    /**
     * Shows the animal as readable text.
     * @return formatted animal summary
     */
    @Override
    public String toString() {
        return species + " - " + identifier + " (acquired " + dateAcquired + ")";
    }
}
