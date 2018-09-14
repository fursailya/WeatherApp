package app.weather.fursa.weatherapp.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ForecastWind(
        val speed: Float,

        @SerializedName("deg")
        val degree: Float) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readFloat(),
                parcel.readFloat())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeFloat(speed)
                parcel.writeFloat(degree)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<ForecastWind> {
                override fun createFromParcel(parcel: Parcel): ForecastWind {
                        return ForecastWind(parcel)
                }

                override fun newArray(size: Int): Array<ForecastWind?> {
                        return arrayOfNulls(size)
                }
        }
}