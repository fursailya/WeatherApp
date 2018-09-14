package app.weather.fursa.weatherapp.app

import app.weather.fursa.weatherapp.pojo.CurrentWeather

interface WeatherChangeListener {

    fun onWeatherChanged(currentWeather: CurrentWeather, city: String)

}