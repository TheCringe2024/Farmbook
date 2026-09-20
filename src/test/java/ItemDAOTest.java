import com.example.farmbook.dao.DatabaseConnection;
import com.example.farmbook.dao.ItemDAO;
import com.example.farmbook.model.Item;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ItemDAO - checks that stock movement behaves correctly.
 */
public class ItemDAOTest {

    @Test
    void savedItemCanBeFound() {
        DatabaseConnection.initialiseItemTable();
        ItemDAO dao = new ItemDAO();

        Item item = new Item("TestSeeds", "Seeds", "packets", 10);
        dao.save(item);

        List<Item> all = dao.findAll();
        boolean found = all.stream().anyMatch(i -> i.getName().equals("TestSeeds"));

        assertTrue(found, "Saved item should appear in findAll()");
    }

    @Test
    void outgoingStockCannotGoBelowZero() {
        DatabaseConnection.initialiseItemTable();
        ItemDAO dao = new ItemDAO();

        Item item = new Item("LowStockItem", "Seeds", "packets", 2);
        dao.save(item);

        Item saved = dao.findAll().stream()
                .filter(i -> i.getName().equals("LowStockItem"))
                .findFirst()
                .orElseThrow();

        boolean result = dao.removeStock(saved.getId(), 100);

        assertFalse(result, "Removing more stock than available should fail, not go negative");
    }

    @Test
    void addingNegativeStockShouldBeRejected() {
        DatabaseConnection.initialiseItemTable();
        ItemDAO dao = new ItemDAO();

        Item item = new Item("NegativeTestItem", "Seeds", "packets", 5);
        dao.save(item);

        Item saved = dao.findAll().stream()
                .filter(i -> i.getName().equals("NegativeTestItem"))
                .findFirst()
                .orElseThrow();

        boolean result = dao.addStock(saved.getId(), -10);

        assertFalse(result, "Adding a negative amount should be rejected, not silently subtract stock");
    }
}
