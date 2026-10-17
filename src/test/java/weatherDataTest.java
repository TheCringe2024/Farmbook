import com.example.farmbook.weatherData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class weatherDataTest {

    @Test
    void shouldReturnCorrectTemperature() {
        weatherData weather = new weatherData(
                21,
                12,
                4,
                50,
                21,
                1,
                12,
                67,
                45,
                23,
                12,
                22
        );

        double maxTempResult = weather.getMax_temp();
        assertEquals(21.0, maxTempResult);

        double minTempResult = weather.getMin_temp();
        assertEquals(12.0, minTempResult);

        double weatherCodeResult = weather.getWeather_code();
        assertEquals(4, weatherCodeResult);

        int precipitationProbResult = weather.getPrecipitation_prob();
        assertEquals(50, precipitationProbResult);

        double windspeedTest = weather.getWind_speed();
        assertEquals(21, windspeedTest);

        int UV_indexTest = weather.getUV_index();
        assertEquals(1, UV_indexTest);

        double relative_humidityTest = weather.getRelative_humidity();
        assertEquals(12, relative_humidityTest);

        double ref_etTest = weather.getRef_ET();
        assertEquals(67, ref_etTest);

        double vapourPressureDefTest = weather.getVapour_pressure_def();
        assertEquals(45, vapourPressureDefTest);

        double meanDewpointTest = weather.getMean_dewpoint();
        assertEquals(23, meanDewpointTest);

        double grow_degreeTest = weather.getGrow_degree_days_base();
        assertEquals(12, grow_degreeTest);

        double leafWetnessTest = weather.getMean_leaf_wetness();
        assertEquals(22, leafWetnessTest);
    }

}

