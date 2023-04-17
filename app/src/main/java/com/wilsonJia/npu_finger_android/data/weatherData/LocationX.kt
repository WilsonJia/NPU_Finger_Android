package com.wilsonJia.npu_finger_android.data.weatherData

data class LocationX(
    val geocode: String,
    val lat: String,
    val locationName: String,
    val lon: String,
    val weatherElement: List<WeatherElement>
)