package app.weather.fursa.weatherapp.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ForecastMain(
        val temp: Float,
        val pressure: Float,
        val humidity: Float,

        @SerializedName("temp_min")
        val minTemp: Float,

        @SerializedName("temp_max")
        val maxTemp: Float) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readFloat(),
                parcel.readFloat(),
                parcel.readFloat(),
                parcel.readFloat(),
                parcel.readFloat())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeFloat(temp)
                parcel.writeFloat(pressure)
                parcel.writeFloat(humidity)
                parcel.writeFloat(minTemp)
                parcel.writeFloat(maxTemp)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<ForecastMain> {
                override fun createFromParcel(parcel: Parcel): ForecastMain {
                        return ForecastMain(parcel)
                }

                override fun newArray(size: Int): Array<ForecastMain?> {
                        return arrayOfNulls(size)
                }
        }
}