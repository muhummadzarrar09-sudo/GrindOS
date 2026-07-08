package com.grindos.app.data.repository

import com.grindos.app.data.local.dao.TaskDao
import com.grindos.app.data.local.entity.TaskEntity
import com.grindos.app.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getTasksForDate(date: LocalDate): Flow<List<Task>> =
        taskDao.getTasksForDate(date).map { entities -> entities.map { it.toDomain() } }

    fun getTopPriorityTasks(date: LocalDate): Flow<List<Task>> =
        taskDao.getTopPriorityTasks(date).map { entities -> entities.map { it.toDomain() } }

    fun getAllPendingTasks(): Flow<List<Task>> =
        taskDao.getAllPendingTasks().map { entities -> entities.map { it.toDomain() } }

    fun getCompletedCountForDate(date: LocalDate): Flow<Int> =
        taskDao.getCompletedCountForDate(date)

    fun getTotalCountForDate(date: LocalDate): Flow<Int> =
        taskDao.getTotalCountForDate(date)

    suspend fun getTaskById(id: Long): Task? = taskDao.getTaskById(id)?.toDomain()

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task.toEntity())

    suspend fun updateTask(task: Task) = taskDao.updateTask(task.toEntity())

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task.toEntity())

    suspend fun setTaskCompleted(taskId: Long, completed: Boolean) =
        taskDao.setTaskCompleted(taskId, completed)

    private fun TaskEntity.toDomain() = Task(
        id = id, title = title, description = description, category = category,
        priority = priority, date = date, time = time, durationMinutes = durationMinutes,
        isCompleted = isCompleted, reminderEnabled = reminderEnabled,
        notificationTone = notificationTone, repeatRule = repeatRule
    )

    private fun Task.toEntity() = TaskEntity(
        id = id, title = title, description = description, category = category,
        priority = priority, date = date, time = time, durationMinutes = durationMinutes,
        isCompleted = isCompleted, reminderEnabled = reminderEnabled,
        notificationTone = notificationTone, repeatRule = repeatRule
    )
}
