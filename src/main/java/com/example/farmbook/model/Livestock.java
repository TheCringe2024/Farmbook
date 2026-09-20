package com.example.farmbook.model;

public class Livestock {
    private int id;
    private String species;
    private String identifier;
    private String dateAcquired;

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

    @Override
    public String toString() {
        return species + " - " + identifier + " (acquired " + dateAcquired + ")";
    }
}
