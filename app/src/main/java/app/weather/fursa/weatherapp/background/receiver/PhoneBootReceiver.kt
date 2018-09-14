package app.weather.fursa.weatherapp.background.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import app.weather.fursa.weatherapp.background.service.LocationService

class PhoneBootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(LOG_TAG, "onReceive")

        if(Intent.ACTION_BOOT_COMPLETED == intent!!.action) {
            context!!.startService(Intent(context, LocationService::class.java))

            Log.d(LOG_TAG, "ACTION_BOOT_COMPLETED")
        }
    }

    companion object {
        const val LOG_TAG = "Weather/BootReceiver"
    }
}