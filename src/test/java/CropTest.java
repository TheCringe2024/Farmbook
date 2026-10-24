import com.example.farmbook.Crop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CropTest {

    private Crop crop;

    @BeforeEach
    void setUp() {

        crop = new Crop("Corn", "Grain", 500, "2023-10-01", "Test planting", "Field A", "Growing");
    }

    @Test
    void constructor_setsPlantName() {
        assertEquals("Corn", crop.getPlantName());
    }

    @Test
    void constructor_setsCropType() {
        assertEquals("Grain", crop.getCropType());
    }

    @Test
    void constructor_setsAmount() {
        assertEquals(500, crop.getAmount());
    }

    @Test
    void constructor_setsDatePlanted() {
        assertEquals("2023-10-01", crop.getDatePlanted());
    }

    @Test
    void constructor_setsNotes() {
        assertEquals("Test planting", crop.getNotes());
    }

    @Test
    void constructor_setsLocation() {
        assertEquals("Field A", crop.getLocation());
    }

    @Test
    void constructor_setsStatus() {
        assertEquals("Growing", crop.getStatus());
    }

    @Test
    void newItem_hasDefaultIdOfZero() {
        assertEquals(0, crop.getId());
    }

    @Test
    void constructorWithId_setsIdCorrectly() {
        Crop cropWithId = new Crop(10, "Wheat", "Cereal", 200, "2023-11-05", "Winter wheat", "Plot 2", "Harvested");
        assertEquals(10, cropWithId.getId());
        assertEquals("Wheat", cropWithId.getPlantName());
        assertEquals("Harvested", cropWithId.getStatus());
    }

    @Test
    void setId_updatesId() {
        crop.setId(99);
        assertEquals(99, crop.getId());
    }

    @Test
    void setPlantName_updatesPlantName() {
        crop.setPlantName("Soybeans");
        assertEquals("Soybeans", crop.getPlantName());
    }

    @Test
    void setCropType_updatesCropType() {
        crop.setCropType("Legume");
        assertEquals("Legume", crop.getCropType());
    }

    @Test
    void setAmount_updatesAmount() {
        crop.setAmount(1200);
        assertEquals(1200, crop.getAmount());
    }

    @Test
    void setAmount_acceptsZero() {
        crop.setAmount(0);
        assertEquals(0, crop.getAmount());
    }

    @Test
    void setDatePlanted_updatesDatePlanted() {
        crop.setDatePlanted("2024-01-15");
        assertEquals("2024-01-15", crop.getDatePlanted());
    }

    @Test
    void setNotes_updatesNotes() {
        crop.setNotes("Updated notes for this crop.");
        assertEquals("Updated notes for this crop.", crop.getNotes());
    }

    @Test
    void setLocation_updatesLocation() {
        crop.setLocation("Greenhouse B");
        assertEquals("Greenhouse B", crop.getLocation());
    }

    @Test
    void setStatus_updatesStatus() {
        crop.setStatus("Failed");
        assertEquals("Failed", crop.getStatus());
    }
}