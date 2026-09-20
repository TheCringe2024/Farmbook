import com.example.farmbook.dao.CropDAO;
import com.example.farmbook.dao.DatabaseConnection;
import com.example.farmbook.model.Crop;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CropDAO - checks saving, loading, and deleting crops.
 */
public class CropDAOTest {

    @Test
    void savedCropCanBeFound() {
        DatabaseConnection.initialiseCropTable();
        CropDAO dao = new CropDAO();

        Crop crop = new Crop("TestWheat", "Field-A", 50, "2025-01-01");
        dao.save(crop);

        List<Crop> all = dao.findAll();
        boolean found = all.stream().anyMatch(c -> c.getType().equals("TestWheat"));

        assertTrue(found, "Saved crop should appear in findAll()");
    }

    @Test
    void deletedCropShouldNoLongerBeFound() {
        DatabaseConnection.initialiseCropTable();
        CropDAO dao = new CropDAO();

        Crop crop = new Crop("TestCorn", "Field-B", 30, "2025-02-01");
        dao.save(crop);

        Crop saved = dao.findAll().stream()
                .filter(c -> c.getType().equals("TestCorn"))
                .findFirst()
                .orElseThrow();

        dao.delete(saved.getId());

        List<Crop> afterDelete = dao.findAll();
        boolean stillExists = afterDelete.stream().anyMatch(c -> c.getType().equals("TestCorn"));

        assertFalse(stillExists, "Deleted crop should not appear in findAll()");
    }
}
