package com.grindos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grindos.app.domain.model.PrayerName
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "prayers")
data class PrayerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val prayerName: PrayerName,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.of(12, 0),
    val completed: Boolean = false,
    val reminderMinutesBefore: Int = 15
)
