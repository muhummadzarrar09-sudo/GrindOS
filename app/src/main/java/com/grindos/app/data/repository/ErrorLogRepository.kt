package com.grindos.app.data.repository

import com.grindos.app.data.local.dao.ErrorLogDao
import com.grindos.app.data.local.entity.ErrorLogEntity
import com.grindos.app.domain.model.ErrorLog
import com.grindos.app.domain.model.ErrorStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorLogRepository @Inject constructor(
    private val errorLogDao: ErrorLogDao
) {
    fun getAllErrorLogs(): Flow<List<ErrorLog>> =
        errorLogDao.getAllErrorLogs().map { entities -> entities.map { it.toDomain() } }

    fun getErrorLogsByStatus(status: ErrorStatus): Flow<List<ErrorLog>> =
        errorLogDao.getErrorLogsByStatus(status).map { entities -> entities.map { it.toDomain() } }

    fun getErrorsForRedo(date: LocalDate): Flow<List<ErrorLog>> =
        errorLogDao.getErrorsForRedo(date).map { entities -> entities.map { it.toDomain() } }

    suspend fun getErrorLogById(id: Long): ErrorLog? = errorLogDao.getErrorLogById(id)?.toDomain()

    suspend fun insertErrorLog(errorLog: ErrorLog): Long = errorLogDao.insertErrorLog(errorLog.toEntity())

    suspend fun updateErrorLog(errorLog: ErrorLog) = errorLogDao.updateErrorLog(errorLog.toEntity())

    suspend fun deleteErrorLog(errorLog: ErrorLog) = errorLogDao.deleteErrorLog(errorLog.toEntity())

    suspend fun updateStatus(errorLogId: Long, status: ErrorStatus) =
        errorLogDao.updateStatus(errorLogId, status)

    private fun ErrorLogEntity.toDomain() = ErrorLog(
        id = id, date = date, section = section, topic = topic, question = question,
        mistakeType = mistakeType, whyItHappened = whyItHappened, correctMethod = correctMethod,
        shortcut = shortcut, redoDate = redoDate, status = status
    )

    private fun ErrorLog.toEntity() = ErrorLogEntity(
        id = id, date = date, section = section, topic = topic, question = question,
        mistakeType = mistakeType, whyItHappened = whyItHappened, correctMethod = correctMethod,
        shortcut = shortcut, redoDate = redoDate, status = status
    )
}
