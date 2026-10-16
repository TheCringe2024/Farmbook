package com.example.farmbook;

import java.io.IOException;

/**
 * Turns the weather lookup into text for the home page, with a fallback when it fails.
 */
public class WeatherText {

    /** Anything that can fetch the weather, such as WeatherAPI::getWeather. */
    @FunctionalInterface
    public interface WeatherSource {
        weatherData getWeather() throws IOException, InterruptedException;
    }

    public static final String UNAVAILABLE = "Weather unavailable";

    /** Returns the temperature as text, or UNAVAILABLE if the lookup fails. */
    public static String temperatureText(WeatherSource source) {
        try {
            weatherData weather = source.getWeather();
            if (weather == null) {
                return UNAVAILABLE;
            }
            return weather.getTemperature() + "°C";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return UNAVAILABLE;
        } catch (Exception e) {
            return UNAVAILABLE;
        }
    }
}
