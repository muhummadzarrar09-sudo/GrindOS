package com.grindos.app.domain.model

import java.time.LocalDate

enum class MistakeType {
    CONCEPT, FORMULA, CALCULATION, TIME, PANIC, SILLY
}

enum class ErrorStatus {
    OPEN, FIXED
}

data class ErrorLog(
    val id: Long = 0L,
    val date: LocalDate = LocalDate.now(),
    val section: String,
    val topic: String,
    val question: String,
    val mistakeType: MistakeType = MistakeType.CONCEPT,
    val whyItHappened: String,
    val correctMethod: String,
    val shortcut: String? = null,
    val redoDate: LocalDate? = null,
    val status: ErrorStatus = ErrorStatus.OPEN
)
