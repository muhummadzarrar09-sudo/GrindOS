package com.grindos.app.data.repository

import com.grindos.app.data.local.dao.SprintSessionDao
import com.grindos.app.data.local.entity.SprintSessionEntity
import com.grindos.app.domain.model.SprintSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SprintRepository @Inject constructor(
    private val sprintSessionDao: SprintSessionDao
) {
    fun getAllSessions(): Flow<List<SprintSession>> =
        sprintSessionDao.getAllSessions().map { entities -> entities.map { it.toDomain() } }

    fun getSessionsSince(since: LocalDateTime): Flow<List<SprintSession>> =
        sprintSessionDao.getSessionsSince(since).map { entities -> entities.map { it.toDomain() } }

    fun getTotalXpSince(since: LocalDateTime): Flow<Int?> =
        sprintSessionDao.getTotalXpSince(since)

    suspend fun insertSession(session: SprintSession): Long =
        sprintSessionDao.insertSession(session.toEntity())

    suspend fun updateSession(session: SprintSession) =
        sprintSessionDao.updateSession(session.toEntity())

    private fun SprintSessionEntity.toDomain() = SprintSession(
        id = id, mode = mode, customDurationMinutes = customDurationMinutes,
        linkedTaskId = linkedTaskId, linkedTopicId = linkedTopicId,
        startedAt = startedAt, completedAt = completedAt, xpEarned = xpEarned
    )

    private fun SprintSession.toEntity() = SprintSessionEntity(
        id = id, mode = mode, customDurationMinutes = customDurationMinutes,
        linkedTaskId = linkedTaskId, linkedTopicId = linkedTopicId,
        startedAt = startedAt, completedAt = completedAt, xpEarned = xpEarned
    )
}
