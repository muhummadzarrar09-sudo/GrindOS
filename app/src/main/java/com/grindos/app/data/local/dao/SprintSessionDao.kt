package com.grindos.app.data.local.dao

import androidx.room.*
import com.grindos.app.data.local.entity.SprintSessionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface SprintSessionDao {

    @Query("SELECT * FROM sprint_sessions ORDER BY startedAt DESC")
    fun getAllSessions(): Flow<List<SprintSessionEntity>>

    @Query("SELECT * FROM sprint_sessions WHERE startedAt >= :since ORDER BY startedAt DESC")
    fun getSessionsSince(since: LocalDateTime): Flow<List<SprintSessionEntity>>

    @Query("SELECT SUM(xpEarned) FROM sprint_sessions WHERE startedAt >= :since")
    fun getTotalXpSince(since: LocalDateTime): Flow<Int?>

    @Query("SELECT * FROM sprint_sessions WHERE id = :id")
    suspend fun getSessionById(id: Long): SprintSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SprintSessionEntity): Long

    @Update
    suspend fun updateSession(session: SprintSessionEntity)

    @Delete
    suspend fun deleteSession(session: SprintSessionEntity)
}
