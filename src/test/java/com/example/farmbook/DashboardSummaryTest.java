package com.example.farmbook;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the dashboard numbers, with no database needed.
 */
public class DashboardSummaryTest {

    private static Inventory item(int quantity) {
        return new Inventory("Item", "Category", "kg", quantity);
    }

    @Test
    void countsAnimalsItemsAndCrops() {
        DashboardSummary s = DashboardSummary.from(
                List.of("a", "b"), List.of(item(10), item(20), item(30)), List.of("c"));
        assertEquals(2, s.getAnimals());
        assertEquals(3, s.getItems());
        assertEquals(1, s.getCrops());
    }

    @Test
    void missingLists_countAsZero() {
        DashboardSummary s = DashboardSummary.from(null, null, null);
        assertEquals(0, s.getAnimals());
        assertEquals(0, s.getItems());
        assertEquals(0, s.getLowStock());
        assertEquals(0, s.getCrops());
    }

    @Test
    void emptyLists_allZero() {
        DashboardSummary s = DashboardSummary.from(List.of(), List.of(), List.of());
        assertEquals(0, s.getAnimals() + s.getItems() + s.getLowStock() + s.getCrops());
    }

    @Test
    void lowStock_countsItemsAtOrBelowTheLimit() {
        DashboardSummary s = DashboardSummary.from(
                List.of(), List.of(item(0), item(5), item(6), item(100)), List.of());
        assertEquals(2, s.getLowStock());
    }
}
