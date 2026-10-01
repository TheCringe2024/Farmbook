package com.example.farmbook;

public class weatherData {
    private String time;
    private double temperature;
    private double cloudCover;
    private double weatherCode;
    private double windDirection;
    private double precipitationProbability;

    // Constructor
    public weatherData(String time, double temperature, double cloudCover, double weatherCode, double windDirection, double precipitationProbability) {
        this.time = time;
        this.temperature = temperature;
        this.cloudCover = cloudCover;
        this.weatherCode = weatherCode;
        this.windDirection = windDirection;
        this.precipitationProbability = precipitationProbability;
    }

    // Methods
    public String getTime() {
        return time;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getCloudCover() {
        return cloudCover;
    }

    public double getWeatherCode() {
        return weatherCode;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public double getPrecipitationProbability() {
        return precipitationProbability;
    }
}



