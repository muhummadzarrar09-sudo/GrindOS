package com.grindos.app.domain.model

import java.time.LocalDateTime

enum class KanbanStatus {
    BACKLOG, DOING, DONE
}

data class HackathonTask(
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val deadline: LocalDateTime? = null,
    val status: KanbanStatus = KanbanStatus.BACKLOG,
    val priority: Int = 2
)
