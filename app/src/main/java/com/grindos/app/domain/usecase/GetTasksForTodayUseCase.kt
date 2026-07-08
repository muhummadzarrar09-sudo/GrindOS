package com.grindos.app.domain.usecase

import com.grindos.app.data.repository.TaskRepository
import com.grindos.app.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTasksForTodayUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(date: LocalDate = LocalDate.now()): Flow<List<Task>> =
        taskRepository.getTasksForDate(date)
}
