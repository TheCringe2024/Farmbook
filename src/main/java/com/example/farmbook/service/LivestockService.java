package com.example.farmbook.service;

import com.example.farmbook.LivestockValidator;
import com.example.farmbook.dao.ILivestockDAO;
import com.example.farmbook.model.Livestock;

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
}