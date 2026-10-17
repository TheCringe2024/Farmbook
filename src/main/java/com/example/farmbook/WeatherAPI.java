package com.example.farmbook;

// IMPORTANT NOTE
// THE API IS SET IN BRISBANE

// Importing the tools needed for the API
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient; // "Internet messenger - the one your talking to"
import java.net.http.HttpRequest; // "The message you're sending"
import java.net.http.HttpResponse; // "The reply you get back"
import com.fasterxml.jackson.databind.JsonNode; // Jackson obtained. An online tool found via research
import com.fasterxml.jackson.databind.ObjectMapper; // Jackson obtained. An online tool found via research



public class WeatherAPI {
    //
    private HttpClient client = HttpClient.newBuilder().build();

    // The information needed from the API or third party source - "I wanted these data variables"
    // Additional note. Seperated it to make it more organised.
    String baseURL = "https://api.open-meteo.com/v1/forecast";

    String location = "latitude=-27.4701&longitude=153.0211"
    + "&timezone=auto" + "&forecast_days=1";

    String daily = "daily=weather_code,temperature_2m_max,temperature_2m_min,"
            + "precipitation_sum,precipitation_probability_max,uv_index_max,"
            + "dew_point_2m_mean,et0_fao_evapotranspiration_sum,"
            + "relative_humidity_2m_mean,vapour_pressure_deficit_max,"
            + "growing_degree_days_base_0_limit_50,leaf_wetness_probability_mean,"
            + "surface_pressure_mean,cloud_cover_mean,wet_bulb_temperature_2m_mean,"
            + "visibility_mean,shortwave_radiation_sum,cape_mean,"
            + "wind_speed_10m_max";

    String hourly = "hourly=temperature_2m,weather_code,surface_pressure,"
            + "cloud_cover,dew_point_2m,relative_humidity_2m";

    String current = "current=temperature_2m,relative_humidity_2m,"
            + "weather_code,cloud_cover,surface_pressure,precipitation";

    // The weather API link together
    String url = baseURL + "?" + location + "&" + daily + "&" + hourly + "&" + current;
    private HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

    // The method that obtain the needed information - "Data variables delivered and is stored in this method"
    public weatherData getWeather() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString() // "give it to me as a string"

        );

       System.out.println("Status: " + response.statusCode());
       System.out.println("Response: " + response.body());

        ObjectMapper mapper = new ObjectMapper(); // A tool that helps Java work with JSON FORMAT -> API gives JSON format and Java just see it as string
        JsonNode weatherData = mapper.readTree(response.body()); // Reading the JSON and creating a "tree". In this API there's Current and Hourly types or branches

        JsonNode currentForecast = weatherData.get("current"); // "Access the weatherData and give me data from the Current group" - output single values
        JsonNode hourlyRate = weatherData.get("hourly"); // "Access the weatherData and give me data from the Hourly group" - output an array of values
        JsonNode dailyRate = weatherData.get("daily"); // "Access the weatherData and give me data from the daily group" - output an array of values

        double max_temp = dailyRate.get("temperature_2m_max").get(0).asDouble(); // Incase your asking with the .get(0)? - Daily sends an array of info and we only wanted the current day to display.
        double min_temp = dailyRate.get("temperature_2m_min").get(0).asDouble();
        int weather_code = dailyRate.get("weather_code").get(0).asInt();
        int precip_prob = dailyRate.get("precipitation_probability_max").get(0).asInt();
        double wind_speed = dailyRate.get("wind_speed_10m_max").get(0).asDouble();
        int UV_index_max = dailyRate.get("uv_index_max").get(0).asInt();

        // weather farmer statistics
        double relative_humidity = dailyRate.get("relative_humidity_2m_mean").get(0).asDouble();
        double ref_ET =  dailyRate.get("et0_fao_evapotranspiration_sum").get(0).asDouble();
        double vapour_pressure_def = dailyRate.get("vapour_pressure_deficit_max").get(0).asDouble();
        double mean_dewpoint = dailyRate.get("dew_point_2m_mean").get(0).asDouble();
        double grow_degree_days_base = dailyRate.get("growing_degree_days_base_0_limit_50").get(0).asDouble();
        double mean_leaf_wetness =  dailyRate.get("leaf_wetness_probability_mean").get(0).asDouble();

        return new weatherData(
                max_temp,
                min_temp,
                weather_code,
                precip_prob,
                wind_speed,
                UV_index_max,
                relative_humidity,
                ref_ET,
                vapour_pressure_def,
                mean_dewpoint,
                grow_degree_days_base,
                mean_leaf_wetness
        );

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        WeatherAPI weatherAPI = new WeatherAPI();
        weatherData weather = weatherAPI.getWeather();


        System.out.println("max temp: " + weather.getMax_temp() + "°C");
        System.out.println("min temp: " + weather.getMin_temp() + "°C");
        System.out.println("weather code: " + weather.getWeather_code());
        System.out.println("precipitation prob: " + weather.getPrecipitation_prob() + "%");
        System.out.println("wind speed: " + weather.getWind_speed() + "km/h");
        System.out.println("UV index: " + weather.getUV_index());
        System.out.println("Relative humidity: " + weather.getRelative_humidity() + "%");
    }

}

