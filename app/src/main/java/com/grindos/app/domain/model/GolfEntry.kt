package com.grindos.app.domain.model

import java.time.LocalDate

data class GolfEntry(
    val id: Long = 0L,
    val date: LocalDate = LocalDate.now(),
    val drillName: String,
    val durationMinutes: Int = 30,
    val notes: String? = null
)
