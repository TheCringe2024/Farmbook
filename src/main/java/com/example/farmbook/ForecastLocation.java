package com.example.farmbook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

/**
 * The place the weather forecast is for. Can be searched by name, saved and loaded.
 */
public class ForecastLocation {

    public static final ForecastLocation DEFAULT = new ForecastLocation("Brisbane", -27.4701, 153.0211);

    private final String name;
    private final double latitude;
    private final double longitude;

    public ForecastLocation(String name, double latitude, double longitude) {
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("coordinates out of range");
        }
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    /** Reads the first place from Open-Meteo's search reply. Empty if there is no usable result. */
    public static Optional<ForecastLocation> parseGeocoding(String json) throws IOException {
        JsonNode first = new ObjectMapper().readTree(json).path("results").path(0);
        if (!first.hasNonNull("name") || !first.hasNonNull("latitude") || !first.hasNonNull("longitude")) {
            return Optional.empty();
        }
        String name = first.get("name").asText();
        if (first.hasNonNull("country")) {
            name += ", " + first.get("country").asText();
        }
        return Optional.of(new ForecastLocation(name, first.get("latitude").asDouble(), first.get("longitude").asDouble()));
    }

    /** Looks a town or city up by name. Needs the internet. */
    public static Optional<ForecastLocation> search(String query) throws IOException, InterruptedException {
        String url = "https://geocoding-api.open-meteo.com/v1/search?count=1&language=en&format=json&name="
                + URLEncoder.encode(query, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return parseGeocoding(response.body());
    }

    /** Saves the place to a small file. Returns false if it could not be saved. */
    public boolean save(Path file) {
        Properties properties = new Properties();
        properties.setProperty("name", name);
        properties.setProperty("latitude", String.valueOf(latitude));
        properties.setProperty("longitude", String.valueOf(longitude));
        try (OutputStream out = Files.newOutputStream(file)) {
            properties.store(out, "FarmBook forecast location");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /** Loads the saved place. Falls back to Brisbane if there is no file or it is broken. */
    public static ForecastLocation load(Path file) {
        if (!Files.exists(file)) {
            return DEFAULT;
        }
        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(file)) {
            properties.load(in);
            String name = properties.getProperty("name");
            if (name == null || name.isBlank()) {
                return DEFAULT;
            }
            return new ForecastLocation(name,
                    Double.parseDouble(properties.getProperty("latitude")),
                    Double.parseDouble(properties.getProperty("longitude")));
        } catch (IOException | RuntimeException e) {
            return DEFAULT;
        }
    }
}
