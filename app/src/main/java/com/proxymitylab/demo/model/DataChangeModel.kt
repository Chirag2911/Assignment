package com.proxymitylab.demo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataChangeModel(val city:String,val aqiValue:String?, val timeMilli:Long): Parcelable {
}