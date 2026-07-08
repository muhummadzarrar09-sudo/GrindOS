package com.grindos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "quran_entries")
data class QuranEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: LocalDate = LocalDate.now(),
    val targetPages: Int = 2,
    val pagesRead: Int = 0,
    val notes: String? = null
)
