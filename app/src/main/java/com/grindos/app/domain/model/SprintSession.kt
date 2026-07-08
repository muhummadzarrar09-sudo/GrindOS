package com.grindos.app.domain.model

import java.time.LocalDateTime

enum class SprintMode(val durationMinutes: Int, val label: String) {
    MINI_RESET(5, "Mini Reset"),
    FOCUS_SPRINT(25, "Focus Sprint"),
    DEEP_WORK(50, "Deep Work"),
    MCQ_SPEED_DRILL(20, "MCQ Speed Drill")
}

data class SprintSession(
    val id: Long = 0L,
    val mode: SprintMode = SprintMode.FOCUS_SPRINT,
    val customDurationMinutes: Int? = null,
    val linkedTaskId: Long? = null,
    val linkedTopicId: Long? = null,
    val startedAt: LocalDateTime = LocalDateTime.now(),
    val completedAt: LocalDateTime? = null,
    val xpEarned: Int = 0
)
