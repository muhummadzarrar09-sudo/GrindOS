package com.grindos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grindos.app.domain.model.SprintMode
import java.time.LocalDateTime

@Entity(tableName = "sprint_sessions")
data class SprintSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val mode: SprintMode = SprintMode.FOCUS_SPRINT,
    val customDurationMinutes: Int? = null,
    val linkedTaskId: Long? = null,
    val linkedTopicId: Long? = null,
    val startedAt: LocalDateTime = LocalDateTime.now(),
    val completedAt: LocalDateTime? = null,
    val xpEarned: Int = 0
)
