package com.grindos.app.data.repository

import com.grindos.app.data.local.dao.HackathonTaskDao
import com.grindos.app.data.local.entity.HackathonTaskEntity
import com.grindos.app.domain.model.HackathonTask
import com.grindos.app.domain.model.KanbanStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HackathonRepository @Inject constructor(
    private val hackathonTaskDao: HackathonTaskDao
) {
    fun getAllTasks(): Flow<List<HackathonTask>> =
        hackathonTaskDao.getAllTasks().map { entities -> entities.map { it.toDomain() } }

    fun getTasksByStatus(status: KanbanStatus): Flow<List<HackathonTask>> =
        hackathonTaskDao.getTasksByStatus(status).map { entities -> entities.map { it.toDomain() } }

    suspend fun getTaskById(id: Long): HackathonTask? = hackathonTaskDao.getTaskById(id)?.toDomain()

    suspend fun insertTask(task: HackathonTask): Long = hackathonTaskDao.insertTask(task.toEntity())

    suspend fun updateTask(task: HackathonTask) = hackathonTaskDao.updateTask(task.toEntity())

    suspend fun deleteTask(task: HackathonTask) = hackathonTaskDao.deleteTask(task.toEntity())

    suspend fun updateTaskStatus(taskId: Long, status: KanbanStatus) =
        hackathonTaskDao.updateTaskStatus(taskId, status)

    private fun HackathonTaskEntity.toDomain() = HackathonTask(
        id = id, title = title, description = description,
        deadline = deadline, status = status, priority = priority
    )

    private fun HackathonTask.toEntity() = HackathonTaskEntity(
        id = id, title = title, description = description,
        deadline = deadline, status = status, priority = priority
    )
}
