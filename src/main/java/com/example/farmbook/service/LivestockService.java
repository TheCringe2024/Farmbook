package com.example.farmbook.service;

import com.example.farmbook.LivestockValidator;
import com.example.farmbook.dao.ILivestockDAO;
import com.example.farmbook.model.Livestock;
import java.util.List;
import java.util.Locale;

/**
 * Contains business logic for livestock operations.
 *
 * The service separates livestock validation and business rules
 * from the JavaFX controller and persistence implementation.
 */
public class LivestockService {

    /**
     * Represents the possible outcomes when saving livestock.
     */
    public enum SaveResult {
        SUCCESS,
        MISSING_FIELDS,
        INVALID_DATE,
        PERSISTENCE_ERROR
    }

    private final ILivestockDAO livestockDAO;

    /**
     * Creates a livestock service using the supplied persistence abstraction.
     *
     * @param livestockDAO DAO used to persist livestock records
     */
    public LivestockService(ILivestockDAO livestockDAO) {
        this.livestockDAO = livestockDAO;
    }
    /**
     * Returns all stored livestock records.
     *
     * The service exposes livestock retrieval to controllers
     * without requiring them to depend directly on the DAO.
     *
     * @return all stored livestock records
     */
    public List<Livestock> getAllLivestock() {
        return livestockDAO.findAll();
    }

    /**
     * Validates livestock input and saves the record when valid.
     *
     * @param species species entered by the user
     * @param identifier identifier or tag entered by the user
     * @param dateAcquired acquisition date in YYYY-MM-DD format
     * @return result describing whether the operation succeeded
     */
    public SaveResult saveLivestock(
            String species,
            String identifier,
            String dateAcquired) {

        if (LivestockValidator.hasBlankRequiredFields(
                species,
                identifier,
                dateAcquired)) {

            return SaveResult.MISSING_FIELDS;
        }

        if (!LivestockValidator.isValidDate(dateAcquired)) {
            return SaveResult.INVALID_DATE;
        }

        Livestock livestock =
                new Livestock(
                        species,
                        identifier,
                        dateAcquired
                );

        boolean saved = livestockDAO.save(livestock);

        if (!saved) {
            return SaveResult.PERSISTENCE_ERROR;
        }

        return SaveResult.SUCCESS;
    }
    /**
     * Searches livestock by species or identifier.
     *
     * @param query search keyword
     * @return matching livestock records
     */
    public List<Livestock> searchLivestock(String query) {
        String keyword = query == null
                ? ""
                : query.trim().toLowerCase(Locale.ROOT);

        if (keyword.isEmpty()) {
            return getAllLivestock();
        }

        return getAllLivestock().stream()
                .filter(animal ->
                        animal.getSpecies()
                                .toLowerCase(Locale.ROOT)
                                .contains(keyword)
                                || animal.getIdentifier()
                                .toLowerCase(Locale.ROOT)
                                .contains(keyword))
                .toList();
    }
    /**
     * Deletes a livestock record by ID.
     *
     * @param id livestock record ID
     * @return true when deletion succeeds
     */
    public boolean deleteLivestock(int id) {
        if (id <= 0) {
            return false;
        }

        return livestockDAO.delete(id);
    }
}