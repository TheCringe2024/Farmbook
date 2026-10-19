package com.example.farmbook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Reads the 7-day weather forecast from Open-Meteo.
 */
public class WeeklyForecast {

    /** One day of the forecast. */
    public static class Day {
        private final String dayName;
        private final String icon;
        private final double max;
        private final double min;

        public Day(String dayName, String icon, double max, double min) {
            this.dayName = dayName;
            this.icon = icon;
            this.max = max;
            this.min = min;
        }

        public String getDayName() { return dayName; }
        public String getIcon() { return icon; }
        public double getMax() { return max; }
        public double getMin() { return min; }
    }

    private static final String URL = "https://api.open-meteo.com/v1/forecast?latitude=-27.4701&longitude=153.0211"
            + "&daily=weather_code,temperature_2m_max,temperature_2m_min&timezone=auto&forecast_days=7";

    /** Turns a weather code into a small picture. */
    public static String iconFor(int code) {
        if (code == 0) return "☀️";
        if (code <= 2) return "⛅";
        if (code == 3) return "☁️";
        if (code == 45 || code == 48) return "🌫️";
        if (code >= 51 && code <= 67) return "🌧️";
        if (code >= 71 && code <= 77) return "❄️";
        if (code >= 80 && code <= 82) return "🌦️";
        if (code >= 85 && code <= 86) return "❄️";
        if (code >= 95) return "⛈️";
        return "🌡️";
    }

    /** Reads the days out of the JSON that Open-Meteo sends back. Stops early if some data is missing. */
    public static List<Day> parse(String json) throws IOException {
        JsonNode daily = new ObjectMapper().readTree(json).path("daily");
        JsonNode times = daily.path("time");
        JsonNode codes = daily.path("weather_code");
        JsonNode max = daily.path("temperature_2m_max");
        JsonNode min = daily.path("temperature_2m_min");

        List<Day> days = new ArrayList<>();
        if (!times.isArray()) {
            return days;
        }
        for (int i = 0; i < times.size(); i++) {
            if (!codes.has(i) || !max.has(i) || !min.has(i)) {
                break;
            }
            LocalDate date = LocalDate.parse(times.get(i).asText());
            days.add(new Day(
                    date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    iconFor(codes.get(i).asInt()),
                    max.get(i).asDouble(),
                    min.get(i).asDouble()));
        }
        return days;
    }

    /** Downloads the forecast. Throws if there is no internet. */
    public static List<Day> fetch() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return parse(response.body());
    }

    /** Builds the forecast web address for a place. */
    public static String urlFor(double latitude, double longitude) {
        return "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude
                + "&daily=weather_code,temperature_2m_max,temperature_2m_min&timezone=auto&forecast_days=7";
    }

    /** Downloads the forecast for a place. Throws if there is no internet. */
    public static List<Day> fetch(double latitude, double longitude) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlFor(latitude, longitude))).build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return parse(response.body());
    }
}
