package com.wilsonJia.npu_finger_android.data.courseTableData

data class CourseTableInfo(
    val haveSatDay: String,
    val status: String,
    val timeList: List<Time>,
    val value: List<Value>
)