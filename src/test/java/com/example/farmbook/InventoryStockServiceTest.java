package com.example.farmbook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the stock in and out rules with a fake DAO, so no real database is used.
 */
public class InventoryStockServiceTest {

    private FakeInventoryDAO dao;
    private InventoryStockService service;
    private Inventory seeds;

    @BeforeEach
    void setUp() {
        dao = new FakeInventoryDAO();
        service = new InventoryStockService(dao);
        dao.addInventoryItem(new Inventory("Seeds", "Seeds", "packets", 10));
        seeds = dao.getInventoryItems().get(0);
    }

    private int storedQuantity() {
        return dao.getInventory(seeds.getId()).getInventoryQuantity();
    }

    @Test
    void addStock_increasesQuantityAndSaves() {
        assertTrue(service.addStock(seeds, 5));
        assertEquals(15, storedQuantity());
    }

    @Test
    void addStock_rejectsZeroOrNegative() {
        assertFalse(service.addStock(seeds, 0));
        assertFalse(service.addStock(seeds, -3));
        assertEquals(10, storedQuantity());
    }

    @Test
    void removeStock_decreasesQuantityAndSaves() {
        assertTrue(service.removeStock(seeds, 3));
        assertEquals(7, storedQuantity());
    }

    @Test
    void removeStock_rejectsMoreThanAvailable() {
        assertFalse(service.removeStock(seeds, 11));
        assertEquals(10, storedQuantity());
    }

    @Test
    void removeStock_allowsRemovingExactlyWhatIsLeft() {
        assertTrue(service.removeStock(seeds, 10));
        assertEquals(0, storedQuantity());
    }

    @Test
    void removeStock_rejectsZeroOrNegative() {
        assertFalse(service.removeStock(seeds, 0));
        assertFalse(service.removeStock(seeds, -1));
        assertEquals(10, storedQuantity());
    }

    @Test
    void unknownItem_isRejected() {
        Inventory ghost = new Inventory("Ghost", "None", "none", 1);
        ghost.setId(999);
        assertFalse(service.addStock(ghost, 1));
        assertFalse(service.removeStock(ghost, 1));
    }

    /** In-memory stand-in for the database. It stores copies, so the tests prove the service saves. */
    private static class FakeInventoryDAO implements IInventoryDAO {
        private final List<Inventory> stored = new ArrayList<>();
        private int nextId = 1;

        @Override
        public void addInventoryItem(Inventory inventory) {
            Inventory copy = copyOf(inventory);
            copy.setId(nextId++);
            stored.add(copy);
        }

        @Override
        public void updateInventoryItem(Inventory inventory) {
            for (int i = 0; i < stored.size(); i++) {
                if (stored.get(i).getId() == inventory.getId()) {
                    stored.set(i, copyOf(inventory));
                }
            }
        }

        @Override
        public void deleteInventoryItem(Inventory inventory) {
            stored.removeIf(item -> item.getId() == inventory.getId());
        }

        @Override
        public Inventory getInventory(int id) {
            for (Inventory item : stored) {
                if (item.getId() == id) {
                    return copyOf(item);
                }
            }
            return null;
        }

        @Override
        public List<Inventory> getInventoryItems() {
            List<Inventory> copies = new ArrayList<>();
            for (Inventory item : stored) {
                copies.add(copyOf(item));
            }
            return copies;
        }

        private static Inventory copyOf(Inventory item) {
            Inventory copy = new Inventory(item.getInventoryItemName(), item.getInventoryCategory(),
                    item.getInventoryUnit(), item.getInventoryQuantity());
            copy.setId(item.getId());
            return copy;
        }
    }
}
