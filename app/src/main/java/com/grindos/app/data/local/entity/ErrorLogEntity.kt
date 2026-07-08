package com.grindos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grindos.app.domain.model.ErrorStatus
import com.grindos.app.domain.model.MistakeType
import java.time.LocalDate

@Entity(tableName = "error_logs")
data class ErrorLogEntity(
    @PrimaryKey(autoGenerate = true)
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
