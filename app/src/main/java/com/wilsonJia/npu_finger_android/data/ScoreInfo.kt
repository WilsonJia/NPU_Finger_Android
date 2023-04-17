package com.wilsonJia.npu_finger_android.data

data class ScoreInfo(
    val error: String, //不確定 實驗用
    val avgScore: String,
    val conduct: String,
    val rank: String,
    val value: List<ScoreValue>
)

data class ScoreValue(
    val courseName: String,
    val finalScore: String,
    val midScore: String
)