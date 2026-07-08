package com.grindos.app.domain.model

import java.time.LocalDate

data class QuranEntry(
    val id: Long = 0L,
    val date: LocalDate = LocalDate.now(),
    val targetPages: Int = 2,
    val pagesRead: Int = 0,
    val notes: String? = null
)
