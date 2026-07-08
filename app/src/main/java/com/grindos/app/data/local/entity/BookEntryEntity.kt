package com.grindos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "book_entries")
data class BookEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val bookName: String,
    val date: LocalDate = LocalDate.now(),
    val pagesReadToday: Int = 0,
    val dailyTarget: Int = 10,
    val totalPagesRead: Int = 0,
    val notes: String? = null
)
