package com.grindos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grindos.app.domain.model.KanbanStatus
import java.time.LocalDateTime

@Entity(tableName = "hackathon_tasks")
data class HackathonTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val deadline: LocalDateTime? = null,
    val status: KanbanStatus = KanbanStatus.BACKLOG,
    val priority: Int = 2
)
