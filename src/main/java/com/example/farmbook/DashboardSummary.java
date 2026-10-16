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
        int itemCount = 0;
        int low = 0;
        if (items != null) {
            itemCount = items.size();
            for (Inventory item : items) {
                if (item.getInventoryQuantity() <= LOW_STOCK_LIMIT) {
                    low++;
                }
            }
        }
        return new DashboardSummary(
                animals == null ? 0 : animals.size(), itemCount, low, crops == null ? 0 : crops.size());
    }
}
