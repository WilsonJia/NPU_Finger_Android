package com.wilsonJia.npu_finger_android.data.weatherData

import com.wilsonJia.npu_finger_android.data.weatherData.Time

data class WeatherElement(
    val description: String,
    val elementName: String,
    val time: List<Time>
)