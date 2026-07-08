package com.grindos.app.data.local.dao

import androidx.room.*
import com.grindos.app.data.local.entity.HackathonTaskEntity
import com.grindos.app.domain.model.KanbanStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface HackathonTaskDao {

    @Query("SELECT * FROM hackathon_tasks ORDER BY priority ASC, deadline ASC")
    fun getAllTasks(): Flow<List<HackathonTaskEntity>>

    @Query("SELECT * FROM hackathon_tasks WHERE status = :status ORDER BY priority ASC")
    fun getTasksByStatus(status: KanbanStatus): Flow<List<HackathonTaskEntity>>

    @Query("SELECT * FROM hackathon_tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): HackathonTaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: HackathonTaskEntity): Long

    @Update
    suspend fun updateTask(task: HackathonTaskEntity)

    @Delete
    suspend fun deleteTask(task: HackathonTaskEntity)

    @Query("UPDATE hackathon_tasks SET status = :status WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Long, status: KanbanStatus)
}
