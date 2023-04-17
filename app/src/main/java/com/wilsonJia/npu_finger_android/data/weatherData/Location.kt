package com.wilsonJia.npu_finger_android.data.weatherData

data class Location(
    val dataid: String,
    val datasetDescription: String,
    val location: List<LocationX>,
    val locationsName: String
)