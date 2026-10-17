package com.example.farmbook;

public class weatherData {
    private String time;
    private double max_temp;
    private double min_temp;
    private int weather_code;
    private int precipitation_prob;
    private double wind_speed;
    private int UV_index;
    private double relative_humidity;
    private double ref_ET;
    private double vapour_pressure_def;
    private double mean_dewpoint;
    private double grow_degree_days_base;
    private double mean_leaf_wetness;
    //private double wind_direction;
    //private double weatherCode;
   // private double windDirection;
    //private double precipitationProbability;

    // Constructor
    public weatherData(double max_temp, double min_temp, int weather_code, int precipitation_prob,  double wind_speed, int UV_index, double relative_humidity, double ref_ET, double vapour_pressure_def, double mean_dewpoint, double grow_degree_days_base, double mean_leaf_wetness) {
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.weather_code = weather_code;
        this.precipitation_prob = precipitation_prob;
        this.wind_speed = wind_speed;
        this.UV_index = UV_index;
        this.relative_humidity = relative_humidity;
        this.ref_ET = ref_ET;
        this.vapour_pressure_def = vapour_pressure_def;
        this.mean_dewpoint = mean_dewpoint;
        this.grow_degree_days_base = grow_degree_days_base;
        this.mean_leaf_wetness = mean_leaf_wetness;
       // this.weatherCode = weatherCode;
        //this.windDirection = windDirection;
        //this.precipitationProbability = precipitationProbability;
    }

    // Methods
    public double getMax_temp() {
        return max_temp;
    }

    public double getMin_temp() {
        return min_temp;
    }

    public int getWeather_code() {
        return weather_code;
    }

    public int getPrecipitation_prob() {
        return precipitation_prob;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public int getUV_index() {
        return UV_index;
    }

    public double getRelative_humidity() {
        return relative_humidity;
    }

    public double getRef_ET() {
        return ref_ET;
    }

    public double getVapour_pressure_def() {
        return vapour_pressure_def;
    }

    public double getMean_dewpoint() {
        return mean_dewpoint;
    }

    public double getGrow_degree_days_base() {
        return grow_degree_days_base;
    }

    public double getMean_leaf_wetness() {
        return mean_leaf_wetness;
    }
/*
    public double getWindDirection() {
        return windDirection;
    }

    public double getPrecipitationProbability() {
        return precipitationProbability;
    }

 */
}



