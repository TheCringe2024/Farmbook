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
    private HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.open-meteo.com/v1/forecast?latitude=-27.4679&longitude=153.0281&hourly=temperature_2m,relative_humidity_2m,soil_temperature_0cm,soil_temperature_6cm,soil_temperature_18cm,vapour_pressure_deficit,et0_fao_evapotranspiration,evapotranspiration,cloud_cover,surface_pressure,weather_code,wind_speed_10m,soil_moisture_0_to_1cm,soil_moisture_3_to_9cm,precipitation_probability&current=temperature_2m,surface_pressure,weather_code,cloud_cover,wind_speed_10m,relative_humidity_2m&timezone=Australia%2FSydney"))
            .build();

    // The method that obtain the needed information - "Data variables delivered and is stored in this method"
    public weatherData getWeather() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString() // "give it to me as a string"
        );

        ObjectMapper mapper = new ObjectMapper(); // A tool that helps Java work with JSON FORMAT -> API gives JSON format and Java just see it as string
        JsonNode weatherData = mapper.readTree(response.body()); // Reading the JSON and creating a "tree". In this API there's Current and Hourly types or branches

        JsonNode currentForecast = weatherData.get("current"); // "Access the weatherData and give me data from the Current group" - output single values
        JsonNode hourlyRate = weatherData.get("hourly"); // "Access the weatherData and give me data from the Hourly group" - output an array of values

       // String time = currentForecast.get("time").asText();
        double temperature = currentForecast.get("temperature_2m").asDouble();
        double cloud_cover = currentForecast.get("cloud_cover").asDouble();
        double weather_code = currentForecast.get("weather_code").asDouble();

        // Getting weather data at the current time
        //double wind_direction = currentForecast.get("wind_direction_10m").asDouble();

        // Obtaining weather data from the hourly forecast
        double precipitation_probability = hourlyRate.get("precipitation_probability").asDouble();


        //System.out.println("temperature: " + temperature + "*c");
        //System.out.println("time: " + time);
        //System.out.println("cloud_cover: " + cloud_cover);
        //System.out.println("weather_code: " + weather_code);
        //System.out.println("wind_direction: " + wind_direction);
        //System.out.println("precipitation_probability: " + precipitation_probability);

        return new weatherData(
                temperature,
                cloud_cover,
                weather_code,
               // wind_direction,
                precipitation_probability
        );
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        WeatherAPI weatherAPI = new WeatherAPI();
        weatherData weather = weatherAPI.getWeather();

        System.out.println("Temperature: " + weather.getTemperature() + "°C");
        System.out.println("Time: " + weather.getTime());
        System.out.println("Cloud cover: " + weather.getCloudCover() + "'m");
        System.out.println("Weather Code: " + weather.getWeatherCode() + "'m");
        System.out.println("Wind Direction: " + weather.getWindDirection() + "'m");
        System.out.println("Precipitation: " + weather.getPrecipitationProbability() + "'m");
    }

}
