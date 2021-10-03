package  com.proxymitylab.demo.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AQIData(
    @SerializedName("city") var city: String?,
    @SerializedName("aqi") var value: String?,
) : Parcelable












