import com.example.farmbook.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryTest {
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory("Wheat", "Seeds", "Packets", 5);
    }

    @Test
    void constructor_setsItemName() {
        assertEquals("Wheat", inventory.getinventoryItemName());
    }

    @Test
    void constructor_setsCategory() {
        assertEquals("Seeds", inventory.getInventoryCategory());
    }

    @Test
    void constructor_setsUnit() {
        assertEquals("Packets", inventory.getInventoryUnit());
    }

    @Test
    void constructor_setsQuantity() {
        assertEquals(5, inventory.getInventoryQuantity());
    }

    @Test
    void newItem_hasDefaultIdOfZero() {
        // ID is only set once an item is saved into the db
        assertEquals(0, inventory.getId());
    }

    @Test
    void setId_updatesId() {
        inventory.setId(42);
        assertEquals(42, inventory.getId());
    }

    @Test
    void setItemName_updatesItemName() {
        inventory.setinventoryItemName("Potato");
        assertEquals("Potato", inventory.getinventoryItemName());
    }

    @Test
    void setCategory_updatesCategory() {
        inventory.setinventoryCategory("Fertiliser");
        assertEquals("Fertiliser", inventory.getInventoryCategory());
    }

    @Test
    void setUnit_updatesUnit() {
        inventory.setinventoryUnit("Kilograms");
        assertEquals("Kilograms", inventory.getInventoryUnit());
    }

    @Test
    void setQuantity_updatesQuantity() {
        inventory.setinventoryQuantity(100);
        assertEquals(100, inventory.getInventoryQuantity());
    }

    @Test
    void setQuantity_acceptsZero() {
        inventory.setinventoryQuantity(0);
        assertEquals(0, inventory.getInventoryQuantity());
    }
}
