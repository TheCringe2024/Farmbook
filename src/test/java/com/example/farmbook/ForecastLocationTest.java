package com.example.farmbook;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests choosing, saving and loading the forecast place, with sample replies instead of the internet.
 */
public class ForecastLocationTest {

    @Test
    void parseGeocoding_readsTheFirstResult() throws IOException {
        String json = "{\"results\":[{\"name\":\"Sydney\",\"latitude\":-33.8678,\"longitude\":151.2073,\"country\":\"Australia\"}]}";
        ForecastLocation place = ForecastLocation.parseGeocoding(json).orElseThrow();
        assertEquals("Sydney, Australia", place.getName());
        assertEquals(-33.8678, place.getLatitude());
        assertEquals(151.2073, place.getLongitude());
    }

    @Test
    void parseGeocoding_noResults_isEmpty() throws IOException {
        assertEquals(Optional.empty(), ForecastLocation.parseGeocoding("{}"));
        assertEquals(Optional.empty(), ForecastLocation.parseGeocoding("{\"results\":[]}"));
    }

    @Test
    void parseGeocoding_missingCoordinates_isEmpty() throws IOException {
        assertTrue(ForecastLocation.parseGeocoding("{\"results\":[{\"name\":\"Nowhere\"}]}").isEmpty());
    }

    @Test
    void saveThenLoad_givesTheSamePlace(@TempDir Path dir) {
        Path file = dir.resolve("place.properties");
        assertTrue(new ForecastLocation("Perth, Australia", -31.95, 115.86).save(file));
        ForecastLocation loaded = ForecastLocation.load(file);
        assertEquals("Perth, Australia", loaded.getName());
        assertEquals(-31.95, loaded.getLatitude());
        assertEquals(115.86, loaded.getLongitude());
    }

    @Test
    void load_missingFile_givesBrisbane(@TempDir Path dir) {
        assertEquals("Brisbane", ForecastLocation.load(dir.resolve("none.properties")).getName());
    }

    @Test
    void load_brokenFile_givesBrisbane(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("broken.properties");
        Files.writeString(file, "name=Somewhere\nlatitude=abc\nlongitude=1\n");
        assertEquals("Brisbane", ForecastLocation.load(file).getName());
    }

    @Test
    void constructor_rejectsImpossibleCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> new ForecastLocation("Bad", 95, 0));
        assertThrows(IllegalArgumentException.class, () -> new ForecastLocation("Bad", 0, 200));
    }
}
