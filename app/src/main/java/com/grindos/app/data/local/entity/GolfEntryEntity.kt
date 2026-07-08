package com.grindos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "golf_entries")
data class GolfEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: LocalDate = LocalDate.now(),
    val drillName: String,
    val durationMinutes: Int = 30,
    val notes: String? = null
)
