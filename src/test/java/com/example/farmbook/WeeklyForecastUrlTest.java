package com.example.farmbook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that the forecast address uses the chosen place.
 */
public class WeeklyForecastUrlTest {

    @Test
    void urlFor_containsThePlacesCoordinates() {
        String url = WeeklyForecast.urlFor(-33.87, 151.21);
        assertTrue(url.contains("latitude=-33.87"));
        assertTrue(url.contains("longitude=151.21"));
        assertTrue(url.contains("forecast_days=7"));
    }
}
