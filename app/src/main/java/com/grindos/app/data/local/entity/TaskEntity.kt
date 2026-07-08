package com.grindos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grindos.app.domain.model.NotificationTone
import com.grindos.app.domain.model.RepeatRule
import com.grindos.app.domain.model.TaskCategory
import com.grindos.app.domain.model.TaskPriority
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
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
