package app.weather.fursa.weatherapp.api

import app.weather.fursa.weatherapp.app.ApiConst
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory {

    companion object {
        fun createApi(): WeatherApi {
            val retrofit = Retrofit.Builder()
                    .baseUrl(ApiConst.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            
            return retrofit.create(WeatherApi::class.java)
        }
    }
}