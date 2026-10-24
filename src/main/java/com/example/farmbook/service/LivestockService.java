
package com.example.farmbook.service;

import com.example.farmbook.LivestockValidator;
import com.example.farmbook.dao.ILivestockDAO;
import com.example.farmbook.model.Livestock;

import java.util.List;
import java.util.Locale;

/**
 * Contains business logic for livestock operations.
 *
 * <p>The service separates livestock validation and
 * business rules from the JavaFX controller and
 * persistence implementation.
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
     * Creates a livestock service using the supplied
     * persistence abstraction.
     *
     * @param livestockDAO DAO used to persist livestock records
     */
    public LivestockService(ILivestockDAO livestockDAO) {
        this.livestockDAO = livestockDAO;
    }

    /**
     * Returns all stored livestock records.
     *
     * <p>The service exposes livestock retrieval to
     * controllers without requiring them to depend
     * directly on the DAO.
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
     * @param dateAcquired acquisition date in
     *                     {@code yyyy-MM-dd} format
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
     * Searches stored livestock by species or identifier.
     *
     * <p>The search ignores letter case and surrounding
     * whitespace. A null or blank query returns all records.
     *
     * @param query the keyword to search for, or {@code null}
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
     * Deletes a livestock record by its database ID.
     *
     * @param id the livestock record ID
     * @return {@code true} if deletion succeeds;
     *         {@code false} if the ID is invalid
     *         or the DAO cannot delete the record
     */
    public boolean deleteLivestock(int id) {
        if (id <= 0) {
            return false;
        }

        return livestockDAO.delete(id);
    }

    /**
     * Represents the possible outcomes when updating livestock.
     */
    public enum UpdateResult {
        SUCCESS,
        INVALID_ID,
        MISSING_FIELDS,
        INVALID_DATE,
        PERSISTENCE_ERROR
    }

    /**
     * Validates and updates an existing livestock record.
     *
     * <p>The record ID must be positive, required fields
     * must be present, and the acquisition date must be
     * valid before the DAO is called.
     *
     * @param id the existing livestock record ID
     * @param species the updated animal species
     * @param identifier the updated animal identifier or tag
     * @param dateAcquired the acquisition date in
     *                     {@code yyyy-MM-dd} format
     * @return {@link UpdateResult#SUCCESS} when successful,
     *         or a result describing a validation
     *         or persistence failure
     */
    public UpdateResult updateLivestock(
            int id,
            String species,
            String identifier,
            String dateAcquired) {

        if (id <= 0) {
            return UpdateResult.INVALID_ID;
        }

        if (LivestockValidator.hasBlankRequiredFields(
                species, identifier, dateAcquired)) {
            return UpdateResult.MISSING_FIELDS;
        }

        if (!LivestockValidator.isValidDate(dateAcquired)) {
            return UpdateResult.INVALID_DATE;
        }

        Livestock updated = new Livestock(
                species.trim(),
                identifier.trim(),
                dateAcquired.trim()
        );

        updated.setId(id);

        return livestockDAO.update(updated)
                ? UpdateResult.SUCCESS
                : UpdateResult.PERSISTENCE_ERROR;
    }
}
