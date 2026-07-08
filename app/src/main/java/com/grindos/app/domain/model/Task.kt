package com.grindos.app.domain.model

import java.time.LocalDate
import java.time.LocalTime

enum class TaskCategory {
    STUDY, PRAYER, QURAN, HACKATHON, BOOK, GOLF, PERSONAL
}

enum class TaskPriority(val level: Int) {
    HIGH(1), MEDIUM(2), LOW(3)
}

enum class NotificationTone {
    GENTLE, HYPE, ROAST, DEEN_FIRST, EXAM_WAR
}

enum class RepeatRule {
    NONE, DAILY, WEEKLY
}

data class Task(
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val category: TaskCategory = TaskCategory.PERSONAL,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime? = null,
    val durationMinutes: Int? = null,
    val isCompleted: Boolean = false,
    val reminderEnabled: Boolean = false,
    val notificationTone: NotificationTone = NotificationTone.GENTLE,
    val repeatRule: RepeatRule = RepeatRule.NONE
)
