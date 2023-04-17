package com.wilsonJia.npu_finger_android.data

data class CalendarInfo(
    val accessRole: String,
    val defaultReminders: List<Any>,
    val description: String,
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val summary: String,
    val timeZone: String,
    val updated: String
)

data class Creator(
    val email: String
)

data class End(
    val date: String
)

data class Item(
    val created: String,
    val creator: Creator,
    val end: End,
    val etag: String,
    val eventType: String,
    val htmlLink: String,
    val iCalUID: String,
    val id: String,
    val kind: String,
    val organizer: Organizer,
    val sequence: Int,
    val start: Start,
    val status: String,
    val summary: String,
    val transparency: String,
    val updated: String
)

data class Organizer(
    val displayName: String,
    val email: String,
    val self: Boolean
)

data class Start(
    val date: String
)