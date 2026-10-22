package com.example.farmbook;

import com.example.farmbook.dao.DatabaseConnection;
import com.example.farmbook.dao.LivestockDAO;
import com.example.farmbook.model.Livestock;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class LivestockPersistenceTest {

    @Test
    void saveReturnsFalseWhenRequiredDatabaseFieldIsNull() {
        LivestockDAO dao = new LivestockDAO();

        Livestock invalidLivestock = new Livestock(
                null,
                "INVALID-" + System.nanoTime(),
                "2026-09-20"
        );

        assertFalse(
                dao.save(invalidLivestock),
                "DAO should report failure when a required field violates the database constraint"
        );
    }

    @Test
    void savedLivestockCanBeLoadedFromDatabase() throws Exception {
        String identifier = "TEST-" + System.nanoTime();

        Livestock livestock =
                new Livestock("Cattle", identifier, "2026-09-20");

        LivestockDAO writer = new LivestockDAO();

        try {
            assertTrue(
                    writer.save(livestock),
                    "Livestock should be saved successfully"
            );

            // Use a new DAO instance so data is read through
            // a separate database connection.
            LivestockDAO reader = new LivestockDAO();

            boolean found = reader.findAll().stream()
                    .anyMatch(animal ->
                            identifier.equals(animal.getIdentifier())
                                    && "Cattle".equals(animal.getSpecies())
                                    && "2026-09-20".equals(animal.getDateAcquired())
                    );

            assertTrue(
                    found,
                    "Saved livestock should still be available from SQLite"
            );

        } finally {
            // Remove test data so the development database stays clean.
            try (Connection connection = DatabaseConnection.connect();
                 PreparedStatement statement = connection.prepareStatement(
                         "DELETE FROM livestock WHERE identifier = ?"
                 )) {

                statement.setString(1, identifier);
                statement.executeUpdate();
            }
        }
    }

    @Test
    void updatedLivestockPersistsInSQLite() {
        String identifier = "TEST-UPDATE-" + UUID.randomUUID();
        LivestockDAO dao = new LivestockDAO();

        try {
            assertTrue(dao.save(new Livestock(
                    "Cattle", identifier, "2026-10-20"
            )));

            Livestock saved = dao.findAll().stream()
                    .filter(l -> identifier.equals(l.getIdentifier()))
                    .findFirst()
                    .orElseThrow();

            int originalId = saved.getId();

            Livestock updated = new Livestock(
                    "Sheep", identifier, "2026-10-22"
            );
            updated.setId(originalId);

            assertTrue(dao.update(updated));

            Livestock reloaded = new LivestockDAO()
                    .findAll().stream()
                    .filter(l -> l.getId() == originalId)
                    .findFirst()
                    .orElseThrow();

            assertEquals("Sheep", reloaded.getSpecies());
            assertEquals(identifier, reloaded.getIdentifier());
            assertEquals(originalId, reloaded.getId());
        } finally {
            deleteTestRecord(identifier);
        }
    }

    @Test
    void deletedLivestockDoesNotReappear() {
        String identifier = "TEST-DELETE-" + UUID.randomUUID();
        LivestockDAO dao = new LivestockDAO();

        try {
            assertTrue(dao.save(new Livestock(
                    "Goat", identifier, "2026-10-20"
            )));

            Livestock saved = dao.findAll().stream()
                    .filter(l -> identifier.equals(l.getIdentifier()))
                    .findFirst()
                    .orElseThrow();

            int id = saved.getId();

            assertTrue(dao.delete(id));

            assertTrue(new LivestockDAO().findAll().stream()
                    .noneMatch(l -> l.getId() == id));
        } finally {
            deleteTestRecord(identifier);
        }
    }

    private void deleteTestRecord(String identifier) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM livestock WHERE identifier = ?"
             )) {
            ps.setString(1, identifier);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not clean up test record", e
            );
        }
    }

}