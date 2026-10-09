import com.example.farmbook.weatherData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class weatherDataTest {

    @Test
    void shouldReturnCorrectTemperature() {
        weatherData weather = new weatherData(
                "2026-10-09T15;00",
                22.0,
                40.0,
                1.0,
                180.0,
                20
        );

        double TempResult = weather.getTemperature();
        assertEquals(22.0,TempResult);

        double CcResult = weather.getCloudCover();
        assertEquals(40.0,CcResult);

        double WCodeResult  = weather.getWeatherCode();
        assertEquals(1.0,WCodeResult);

        double WindDirResult = weather.getWindDirection();
        assertEquals(180.0,WindDirResult);

        double precipResult = weather.getPrecipitationProbability();
        assertEquals(20,precipResult);


    }

}

