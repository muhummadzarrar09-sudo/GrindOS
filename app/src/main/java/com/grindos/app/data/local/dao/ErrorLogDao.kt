package com.grindos.app.data.local.dao

import androidx.room.*
import com.grindos.app.data.local.entity.ErrorLogEntity
import com.grindos.app.domain.model.ErrorStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ErrorLogDao {

    @Query("SELECT * FROM error_logs ORDER BY date DESC")
    fun getAllErrorLogs(): Flow<List<ErrorLogEntity>>

    @Query("SELECT * FROM error_logs WHERE status = :status ORDER BY date DESC")
    fun getErrorLogsByStatus(status: ErrorStatus): Flow<List<ErrorLogEntity>>

    @Query("SELECT * FROM error_logs WHERE section = :section ORDER BY date DESC")
    fun getErrorLogsBySection(section: String): Flow<List<ErrorLogEntity>>

    @Query("SELECT * FROM error_logs WHERE redoDate = :date AND status = 'OPEN'")
    fun getErrorsForRedo(date: LocalDate): Flow<List<ErrorLogEntity>>

    @Query("SELECT * FROM error_logs WHERE id = :id")
    suspend fun getErrorLogById(id: Long): ErrorLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertErrorLog(errorLog: ErrorLogEntity): Long

    @Update
    suspend fun updateErrorLog(errorLog: ErrorLogEntity)

    @Delete
    suspend fun deleteErrorLog(errorLog: ErrorLogEntity)

    @Query("UPDATE error_logs SET status = :status WHERE id = :errorLogId")
    suspend fun updateStatus(errorLogId: Long, status: ErrorStatus)
}
