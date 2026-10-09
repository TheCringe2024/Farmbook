package com.example.farmbook;

import com.example.farmbook.dao.ILivestockDAO;
import com.example.farmbook.model.Livestock;
import com.example.farmbook.service.LivestockService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LivestockServiceTest {

    /**
     * Simple in-memory fake used so service tests do not depend
     * on SQLite or any external persistence system.
     */
    private static class FakeLivestockDAO implements ILivestockDAO {

        private boolean saveCalled;
        private boolean saveResult = true;
        private Livestock savedLivestock;

        private final List<Livestock> livestockRecords =
                new ArrayList<>();

        @Override
        public boolean save(Livestock livestock) {
            saveCalled = true;
            savedLivestock = livestock;
            return saveResult;
        }

        @Override
        public List<Livestock> findAll() {
            return new ArrayList<>(livestockRecords);
        }

        @Override
        public void delete(int id) {
            // Not required for these service tests.
        }
    }
    @Test
    void returnsLivestockRecordsFromDao() {
        FakeLivestockDAO dao = new FakeLivestockDAO();

        Livestock expected =
                new Livestock(
                        "Cattle",
                        "CATTLE-02",
                        "2026-10-06"
                );

        dao.livestockRecords.add(expected);

        LivestockService service =
                new LivestockService(dao);

        List<Livestock> result =
                service.getAllLivestock();

        assertEquals(1, result.size());
        assertSame(expected, result.get(0));
    }

    @Test
    void rejectsMissingFieldsWithoutCallingDao() {
        FakeLivestockDAO dao = new FakeLivestockDAO();
        LivestockService service = new LivestockService(dao);

        LivestockService.SaveResult result =
                service.saveLivestock(
                        "",
                        "CATTLE-01",
                        "2026-10-05"
                );

        assertEquals(
                LivestockService.SaveResult.MISSING_FIELDS,
                result
        );

        assertFalse(
                dao.saveCalled,
                "DAO should not be called when required fields are missing"
        );
    }

    @Test
    void rejectsInvalidDateWithoutCallingDao() {
        FakeLivestockDAO dao = new FakeLivestockDAO();
        LivestockService service = new LivestockService(dao);

        LivestockService.SaveResult result =
                service.saveLivestock(
                        "Cattle",
                        "CATTLE-01",
                        "05/10/2026"
                );

        assertEquals(
                LivestockService.SaveResult.INVALID_DATE,
                result
        );

        assertFalse(
                dao.saveCalled,
                "DAO should not be called when the date is invalid"
        );
    }

    @Test
    void savesValidLivestockThroughDao() {
        FakeLivestockDAO dao = new FakeLivestockDAO();
        LivestockService service = new LivestockService(dao);

        LivestockService.SaveResult result =
                service.saveLivestock(
                        "Cattle",
                        "CATTLE-01",
                        "2026-10-05"
                );

        assertEquals(
                LivestockService.SaveResult.SUCCESS,
                result
        );

        assertTrue(dao.saveCalled);
        assertNotNull(dao.savedLivestock);
        assertEquals("Cattle", dao.savedLivestock.getSpecies());
        assertEquals("CATTLE-01", dao.savedLivestock.getIdentifier());
        assertEquals(
                "2026-10-05",
                dao.savedLivestock.getDateAcquired()
        );
    }

    @Test
    void reportsPersistenceFailureWhenDaoCannotSave() {
        FakeLivestockDAO dao = new FakeLivestockDAO();
        dao.saveResult = false;

        LivestockService service = new LivestockService(dao);

        LivestockService.SaveResult result =
                service.saveLivestock(
                        "Cattle",
                        "CATTLE-01",
                        "2026-10-05"
                );

        assertEquals(
                LivestockService.SaveResult.PERSISTENCE_ERROR,
                result
        );
    }
}