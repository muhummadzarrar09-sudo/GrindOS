package com.grindos.app.data.local.dao

import androidx.room.*
import com.grindos.app.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY priority ASC, time ASC")
    fun getTasksForDate(date: LocalDate): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND date = :date ORDER BY priority ASC LIMIT 3")
    fun getTopPriorityTasks(date: LocalDate): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY date ASC, priority ASC")
    fun getAllPendingTasks(): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE date = :date AND isCompleted = 1")
    fun getCompletedCountForDate(date: LocalDate): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE date = :date")
    fun getTotalCountForDate(date: LocalDate): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("UPDATE tasks SET isCompleted = :completed WHERE id = :taskId")
    suspend fun setTaskCompleted(taskId: Long, completed: Boolean)
}
