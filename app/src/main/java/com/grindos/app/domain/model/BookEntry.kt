package com.grindos.app.domain.model

import java.time.LocalDate

data class BookEntry(
    val id: Long = 0L,
    val bookName: String,
    val date: LocalDate = LocalDate.now(),
    val pagesReadToday: Int = 0,
    val dailyTarget: Int = 10,
    val totalPagesRead: Int = 0,
    val notes: String? = null
)
