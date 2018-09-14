package app.weather.fursa.weatherapp

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import app.weather.fursa.weatherapp.app.ApiConst.Companion.ACTION_GOT_WEATHER
import app.weather.fursa.weatherapp.app.WeatherChangeListener
import app.weather.fursa.weatherapp.background.receiver.WeatherReceiver
import app.weather.fursa.weatherapp.background.service.LocationService
import app.weather.fursa.weatherapp.pojo.CurrentWeather


class MainActivity : AppCompatActivity(), WeatherChangeListener {
    private lateinit var cityTextView: TextView
    private lateinit var degreesMaxTextView: TextView
    private lateinit var districtTextView: TextView

    private lateinit var locationServiceIntent: Intent

    private val weatherReceiver = WeatherReceiver(this@MainActivity)
    private val intentFilter: IntentFilter = IntentFilter(ACTION_GOT_WEATHER)


    override fun onStart() {
        super.onStart()
        registerReceiver(weatherReceiver, intentFilter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityTextView = findViewById(R.id.city_name_textview)
        degreesMaxTextView = findViewById(R.id.degrees_max_textview)
        districtTextView = findViewById(R.id.district_name_textview)

        checkAndRequestGeoPermission()


    }


    override fun onStop() {
        unregisterReceiver(weatherReceiver)
        super.onStop()
    }

    override fun onWeatherChanged(currentWeather: CurrentWeather, city: String) {
        cityTextView.text = city
        degreesMaxTextView.text = currentWeather.main.temp.toString()
        districtTextView.text = currentWeather.cityName
    }

    private fun checkAndRequestGeoPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_LOCATION_PERMISSION)
        } else {
           startLocationService()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService()
            } else {
                checkAndRequestGeoPermission()
            }
        }
    }

    private fun startLocationService() {
        locationServiceIntent = Intent(this, LocationService::class.java)
        startService(locationServiceIntent)
    }

    companion object {
        const val MAIN_TAG = "Weather/MainActivity"
        const val REQUEST_CODE_LOCATION_PERMISSION = 0
    }

}



