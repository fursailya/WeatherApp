package app.weather.fursa.weatherapp.background.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import app.weather.fursa.weatherapp.api.ApiFactory
import app.weather.fursa.weatherapp.app.ApiConst
import app.weather.fursa.weatherapp.app.ApiConst.Companion.ACTION_GOT_WEATHER
import app.weather.fursa.weatherapp.app.ApiConst.Companion.EXTRA_CITY_NAME
import app.weather.fursa.weatherapp.app.ApiConst.Companion.EXTRA_GOT_WEATHER
import app.weather.fursa.weatherapp.pojo.CurrentWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationService : Service(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var criteria: Criteria
    private lateinit var bestProvider: String
    private lateinit var lastKnownLocation: Location

    private val api = ApiFactory.createApi()

    override fun onCreate() {
        super.onCreate()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        criteria = Criteria()
        bestProvider = locationManager.getBestProvider(criteria, true)

        Log.d(LOG_TAG, "Best location provider is $bestProvider")

        if (bestProvider != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }


            lastKnownLocation = this!!.getLastKnownLocation()!!

            if(lastKnownLocation != null) {
                Log.d(LOG_TAG, "Last known location is $lastKnownLocation")

                getCurrentWeather(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
            }

            locationManager.requestLocationUpdates(
                    bestProvider,
                    0L,
                    0f,
                    this
            )

        }

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "onStartCommand()")
        return Service.START_STICKY
    }

    companion object {
        const val LOG_TAG = "Weather/LocationService"
    }

    override fun onLocationChanged(location: Location?) {
        Log.d(LOG_TAG, "Location changed: lat: ${location!!.latitude} lng: ${location.longitude}")
        getCurrentWeather(location.latitude, location.longitude)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d(LOG_TAG, "Status changed Provider: $provider Status $status")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d(LOG_TAG, "Provider enabled: $provider")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d(LOG_TAG, "Disabled provider is $provider")
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val location = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                bestLocation = location
            }
        }

        val geocoder = Geocoder(this)
        val address = geocoder.getFromLocation(bestLocation!!.latitude, bestLocation.longitude, 1)
        if(address.isNotEmpty()) {
            Log.d(LOG_TAG, "city = ${address[0].locality}")
        }

        return bestLocation
    }

    private fun getCurrentWeather(lat: Double, lng: Double) {

        val call: Call<CurrentWeather> = api.getCurrentWeather(
                lat,
                lng,
                ApiConst.API_KEY,
                ApiConst.DEFAULT_UNITS
        )

        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Toast.makeText(baseContext, "Oops! Something went wrong!", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                if (response.isSuccessful) {
                    val currentWeather = response.body()
                    val geocoder = Geocoder(this@LocationService)
                    val address = geocoder.getFromLocation(lat, lng, 1)
                    Log.d(LOG_TAG, "Weather in ${address[0].locality}")

                    val currentWeatherIntent = Intent(ACTION_GOT_WEATHER)
                    currentWeatherIntent.putExtra(EXTRA_GOT_WEATHER, currentWeather)
                    currentWeatherIntent.putExtra(EXTRA_CITY_NAME, address[0].locality)

                    sendBroadcast(currentWeatherIntent)
                }
            }

        })
    }

}
