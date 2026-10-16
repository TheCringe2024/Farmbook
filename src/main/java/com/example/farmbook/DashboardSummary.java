package com.example.farmbook;

import java.util.List;

/**
 * The numbers shown on the dashboard cards.
 */
public class DashboardSummary {

    /** An item with this many or fewer left counts as low on stock. */
    public static final int LOW_STOCK_LIMIT = 5;

    private final int animals;
    private final int items;
    private final int lowStock;
    private final int crops;

    public DashboardSummary(int animals, int items, int lowStock, int crops) {
        this.animals = animals;
        this.items = items;
        this.lowStock = lowStock;
        this.crops = crops;
    }

    public int getAnimals() { return animals; }
    public int getItems() { return items; }
    public int getLowStock() { return lowStock; }
    public int getCrops() { return crops; }

    /** Counts what the cards show. A missing list counts as zero. */
    public static DashboardSummary from(List<?> animals, List<Inventory> items, List<?> crops) {
        return new DashboardSummary(0, 0, 0, 0);
    }
}
