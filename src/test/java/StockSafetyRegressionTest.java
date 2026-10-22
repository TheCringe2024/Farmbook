import com.example.farmbook.dao.DatabaseConnection;
import com.example.farmbook.dao.ItemDAO;
import com.example.farmbook.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/** Regression tests for invalid stock movements (FR05/FR06). */
public class StockSafetyRegressionTest {
    private ItemDAO dao;
    private final List<Integer> testItemIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        DatabaseConnection.initialiseItemTable();
        dao = new ItemDAO();
    }

    private int createTestItem(int quantity) {
        String name = "StockSafety-" + UUID.randomUUID();
        dao.save(new Item(name, "Test", "units", quantity));
        int id = dao.findAll().stream()
                .filter(item -> name.equals(item.getName()))
                .findFirst().orElseThrow().getId();
        testItemIds.add(id);
        return id;
    }

    private int stockOf(int id) {
        return dao.findAll().stream()
                .filter(item -> item.getId() == id)
                .findFirst().orElseThrow().getQuantity();
    }

    @Test
    void negativeOutgoingStockMustNotIncreaseQuantity() {
        int id = createTestItem(5);
        assertFalse(dao.removeStock(id, -3));
        assertEquals(5, stockOf(id));
    }

    @Test
    void zeroOrNegativeIncomingStockMustBeRejected() {
        int id = createTestItem(5);
        assertFalse(dao.addStock(id, 0));
        assertFalse(dao.addStock(id, -2));
        assertEquals(5, stockOf(id));
    }

    @Test
    void missingStockItemMustNotReportSuccess() {
        assertFalse(dao.removeStock(Integer.MAX_VALUE, 1));
        assertFalse(dao.addStock(Integer.MAX_VALUE, 1));
    }

    @Test
    void outgoingStockMustNotBecomeNegative() {
        int id = createTestItem(2);
        assertFalse(dao.removeStock(id, 3));
        assertEquals(2, stockOf(id));
        assertTrue(dao.removeStock(id, 2));
        assertEquals(0, stockOf(id));
    }

    @AfterEach
    void tearDown() throws Exception {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM items WHERE id = ?")) {
            for (int id : testItemIds) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }
}
