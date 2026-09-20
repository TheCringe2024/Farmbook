package com.example.farmbook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LivestockValidatorTest {

    @Test
    void acceptsValidIsoDate() {
        assertTrue(LivestockValidator.isValidDate("2026-09-20"));
    }

    @Test
    void rejectsIncorrectFormat() {
        assertFalse(LivestockValidator.isValidDate("20/09/2026"));
    }

    @Test
    void rejectsImpossibleDate() {
        assertFalse(LivestockValidator.isValidDate("2026-02-30"));
    }

    @Test
    void rejectsBlankDate() {
        assertFalse(LivestockValidator.isValidDate(" "));
    }

    @Test
    void rejectsNullDate() {
        assertFalse(LivestockValidator.isValidDate(null));
    }

    @Test
    void detectsBlankSpecies() {
        assertTrue(
                LivestockValidator.hasBlankRequiredFields(
                        "",
                        "CATTLE-01",
                        "2026-09-20"
                )
        );
    }

    @Test
    void detectsBlankIdentifier() {
        assertTrue(
                LivestockValidator.hasBlankRequiredFields(
                        "Cattle",
                        " ",
                        "2026-09-20"
                )
        );
    }

    @Test
    void detectsBlankAcquisitionDate() {
        assertTrue(
                LivestockValidator.hasBlankRequiredFields(
                        "Cattle",
                        "CATTLE-01",
                        ""
                )
        );
    }

    @Test
    void acceptsCompleteRequiredFields() {
        assertFalse(
                LivestockValidator.hasBlankRequiredFields(
                        "Cattle",
                        "CATTLE-01",
                        "2026-09-20"
                )
        );
    }
}