package com.wilsonJia.npu_finger_android.data

data class RewardInfo(
    val status: String,
    val value: List<RewardValue>
)
data class RewardValue(
    val category: String,
    val count: String,
    val date: String,
    val info: String,
)