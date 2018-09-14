package app.weather.fursa.weatherapp.app

class ApiConst {
    companion object {
        const val API_BASE_URL = "http://api.openweathermap.org/data/2.5/"
        const val API_KEY = "c597afaf343d5fb395ce4290cad40186"
        const val DEFAULT_UNITS = "metric"
        //receiver
        const val ACTION_GOT_WEATHER = "com.fursa.weatherapp.ACTION_GOT_WEATHER"
        const val EXTRA_GOT_WEATHER = "current_weather_extra"
        const val EXTRA_CITY_NAME = "city_name"
    }
}