package com.example.farmbook.model;

/**
 * This represents one animal (or a group of animals) that a farmer has recorded.
 * It's basically a container holding the details about that animal —
 * what type it is, its tag/ID, and when the farmer got it.
 */
public class Livestock {
    private int id;
    private String species;
    private String identifier;
    private String dateAcquired;

    /**
     * Creates a new animal record with the basic details a farmer would enter.
     * @param species what kind of animal it is, e.g. "Cow" or "Sheep"
     * @param identifier a name or tag number so the farmer can tell animals apart
     * @param dateAcquired the date the farmer got this animal, or when it was born
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
     * Turns this animal's details into a readable sentence,
     * used when showing it in the livestock list on screen.
     */
    @Override
    public String toString() {
        return species + " — " + identifier + " (acquired " + dateAcquired + ")";
    }
}
