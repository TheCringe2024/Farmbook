package com.example.farmbook.dao;

import com.example.farmbook.model.Livestock;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LivestockPersistenceTest {

    private static final String TEST_OWNER =
            "fr13-persistence-" + UUID.randomUUID();

    @Test
    void saveReturnsFalseWhenRequiredDatabaseFieldIsNull() {
        LivestockDAO dao = new LivestockDAO(TEST_OWNER);
        Livestock invalidLivestock = new Livestock(
                null, "INVALID-" + UUID.randomUUID(), "2026-09-20");
        assertFalse(dao.save(invalidLivestock));
    }

    @Test
    void savedLivestockCanBeLoadedFromDatabase() {
        String identifier = "TEST-" + UUID.randomUUID();
        LivestockDAO writer = new LivestockDAO(TEST_OWNER);
        try {
            assertTrue(writer.save(new Livestock(
                    "Cattle", identifier, "2026-09-20")));
            LivestockDAO reader = new LivestockDAO(TEST_OWNER);
            assertTrue(reader.findAll().stream().anyMatch(animal ->
                    identifier.equals(animal.getIdentifier())
                            && "Cattle".equals(animal.getSpecies())
                            && "2026-09-20".equals(animal.getDateAcquired())));
        } finally {
            deleteTestRecord(identifier);
        }
    }

    @Test
    void updatedLivestockPersistsInSQLite() {
        String identifier = "TEST-UPDATE-" + UUID.randomUUID();
        LivestockDAO dao = new LivestockDAO(TEST_OWNER);
        try {
            assertTrue(dao.save(new Livestock(
                    "Cattle", identifier, "2026-10-20")));
            Livestock saved = dao.findAll().stream()
                    .filter(l -> identifier.equals(l.getIdentifier()))
                    .findFirst().orElseThrow();
            int originalId = saved.getId();
            Livestock updated = new Livestock(
                    "Sheep", identifier, "2026-10-22");
            updated.setId(originalId);
            assertTrue(dao.update(updated));
            Livestock reloaded = new LivestockDAO(TEST_OWNER)
                    .findAll().stream()
                    .filter(l -> l.getId() == originalId)
                    .findFirst().orElseThrow();
            assertEquals("Sheep", reloaded.getSpecies());
            assertEquals(identifier, reloaded.getIdentifier());
        } finally {
            deleteTestRecord(identifier);
        }
    }

    @Test
    void deletedLivestockDoesNotReappear() {
        String identifier = "TEST-DELETE-" + UUID.randomUUID();
        LivestockDAO dao = new LivestockDAO(TEST_OWNER);
        try {
            assertTrue(dao.save(new Livestock(
                    "Goat", identifier, "2026-10-20")));
            Livestock saved = dao.findAll().stream()
                    .filter(l -> identifier.equals(l.getIdentifier()))
                    .findFirst().orElseThrow();
            int id = saved.getId();
            assertTrue(dao.delete(id));
            assertTrue(new LivestockDAO(TEST_OWNER).findAll().stream()
                    .noneMatch(l -> l.getId() == id));
        } finally {
            deleteTestRecord(identifier);
        }
    }

    private static void deleteTestRecord(String identifier) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM livestock WHERE identifier = ? "
                             + "AND owner_username = ?")) {
            ps.setString(1, identifier);
            ps.setString(2, TEST_OWNER);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Could not clean up test record", e);
        }
    }
}
