package app.weather.fursa.weatherapp;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import app.weather.fursa.weatherapp.api.ApiFactory;
import app.weather.fursa.weatherapp.api.WeatherApi;
import app.weather.fursa.weatherapp.app.ApiConst;
import app.weather.fursa.weatherapp.pojo.CurrentWeather;
import retrofit2.Call;
import retrofit2.Response;

public class WeatherApiTest {
    private final WeatherApi api = ApiFactory.Companion.createApi();
    private Call<CurrentWeather> currentWeatherCall;
    private Response<CurrentWeather> response;

    public WeatherApiTest() throws IOException {
       currentWeatherCall = api.getCurrentWeather(
                35,
                139,
                ApiConst.API_KEY,
                ApiConst.DEFAULT_UNITS
        );


       response = currentWeatherCall.execute();

    }

    //тестирование ответа от сервера
    @Test
    public void testCurrentWeatherResponse() {
        Assert.assertTrue(response.isSuccessful());
    }

    //название города не null
    @Test
    public void testCityNameIsNotEmpty() {
        Assert.assertNotNull(response.body().getCityName());
    }
    //максимальная и минимальная температура
    @Test
    public void testMaxAndMinTempTest() {
        Assert.assertNotNull(response.body().getMain().getMaxTemp());
        Assert.assertNotNull(response.body().getMain().getMinTemp());
    }
    //Скорость ветра
    @Test
    public void testWindSpeed() {
        Assert.assertTrue(response.body().getWind().getSpeed() > 0);
    }
    //Давление
    @Test
    public void testPressureIsNotNull() {
        Assert.assertNotNull(response.body().getMain().getPressure());
    }
    //влажность
    @Test
    public void testHumidityIsNotNull() {
        Assert.assertNotNull(response.body().getMain().getHumidity());
    }
    //название города
    @Test
    public void testCityLength() {
        String city = response.body().getCityName();
        Assert.assertTrue(city.length() > 0);
    }
}
