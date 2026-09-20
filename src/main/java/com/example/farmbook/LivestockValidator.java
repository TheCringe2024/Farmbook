package com.example.farmbook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validates user input for livestock records.
 */
public final class LivestockValidator {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE;

    private LivestockValidator() {
    }
    /**
     * Checks whether any required livestock field is missing or blank.
     *
     * @param species livestock species entered by the user
     * @param identifier livestock identifier entered by the user
     * @param dateAcquired acquisition or birth date entered by the user
     * @return true when at least one required field is blank or null
     */
    public static boolean hasBlankRequiredFields(
            String species,
            String identifier,
            String dateAcquired) {

        return species == null || species.isBlank()
                || identifier == null || identifier.isBlank()
                || dateAcquired == null || dateAcquired.isBlank();
    }

    /**
     * Checks whether a date uses the ISO YYYY-MM-DD format
     * and represents a real calendar date.
     *
     * @param date date entered by the user
     * @return true when the date is valid; otherwise false
     */
    public static boolean isValidDate(String date) {
        if (date == null || date.isBlank()) {
            return false;
        }

        try {
            LocalDate.parse(date, DATE_FORMAT);
            return true;
        } catch (DateTimeParseException exception) {
            return false;
        }
    }
}