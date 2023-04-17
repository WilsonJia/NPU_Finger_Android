package com.wilsonJia.npu_finger_android.data.weatherData

data class Time(
    val dataTime: String,
    val elementValue: List<ElementValue>,
    val endTime: String,
    val startTime: String
)