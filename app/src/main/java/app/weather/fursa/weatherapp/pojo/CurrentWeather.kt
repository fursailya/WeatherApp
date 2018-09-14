package app.weather.fursa.weatherapp.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CurrentWeather(
        val main: ForecastMain,
        val wind: ForecastWind,

        @SerializedName("name")
        val cityName: String


) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readParcelable(ForecastMain::class.java.classLoader),
                parcel.readParcelable(ForecastWind::class.java.classLoader),
                parcel.readString())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(main, flags)
                parcel.writeParcelable(wind, flags)
                parcel.writeString(cityName)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<CurrentWeather> {
                override fun createFromParcel(parcel: Parcel): CurrentWeather {
                        return CurrentWeather(parcel)
                }

                override fun newArray(size: Int): Array<CurrentWeather?> {
                        return arrayOfNulls(size)
                }
        }
}