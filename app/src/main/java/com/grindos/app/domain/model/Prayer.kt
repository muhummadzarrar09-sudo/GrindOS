package com.grindos.app.domain.model

import java.time.LocalDate
import java.time.LocalTime

enum class PrayerName {
    FAJR, DHUHR, ASR, MAGHRIB, ISHA
}

data class Prayer(
    val id: Long = 0L,
    val prayerName: PrayerName,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.of(12, 0),
    val completed: Boolean = false,
    val reminderMinutesBefore: Int = 15
)
