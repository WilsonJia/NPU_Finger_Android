package com.wilsonJia.npu_finger_android.data

class NewsInfo : ArrayList<NewsInfoItem>()

data class NewsInfoItem(
    val newsDate: String,
    val newsTeam: String,
    val newsTitle: String,
    val newsURL: String
)