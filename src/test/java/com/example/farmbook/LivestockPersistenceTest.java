package com.example.farmbook;

import com.example.farmbook.dao.DatabaseConnection;
import com.example.farmbook.dao.LivestockDAO;
import com.example.farmbook.model.Livestock;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LivestockPersistenceTest {

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
}