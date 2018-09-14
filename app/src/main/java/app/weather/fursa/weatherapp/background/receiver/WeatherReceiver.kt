package app.weather.fursa.weatherapp.background.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import app.weather.fursa.weatherapp.app.ApiConst
import app.weather.fursa.weatherapp.app.ApiConst.Companion.EXTRA_CITY_NAME
import app.weather.fursa.weatherapp.app.WeatherChangeListener
import app.weather.fursa.weatherapp.pojo.CurrentWeather

class WeatherReceiver: BroadcastReceiver {
    private val listener: WeatherChangeListener
    private lateinit var currentWeather: CurrentWeather
    private lateinit var city: String

    constructor(listener: WeatherChangeListener) : super() {
        this.listener = listener
    }


    override fun onReceive(context: Context?, intent: Intent?) {
            if(ApiConst.ACTION_GOT_WEATHER == (intent!!.action)) {
                currentWeather = intent.getParcelableExtra(ApiConst.EXTRA_GOT_WEATHER)
                city = intent.getStringExtra(EXTRA_CITY_NAME)

                listener.onWeatherChanged(currentWeather, city)
            }
        }


    companion object {
        const val RECEIVER_TAG = "Weather/WeatherReceiver"
    }
}