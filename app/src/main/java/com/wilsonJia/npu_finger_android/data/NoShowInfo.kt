package com.wilsonJia.npu_finger_android.data

data class NoShowInfo(
    val error: String,
    val status: String,
    val value: List<NoShowValue>
)

data class NoShowValue(
    val course1: String,
    val course10: String,
    val course11: String,
    val course12: String,
    val course13: String,
    val course14: String,
    val course15: String,
    val course2: String,
    val course3: String,
    val course4: String,
    val course5: String,
    val course6: String,
    val course7: String,
    val course8: String,
    val course9: String,
    val date: String,
    val id: String
)