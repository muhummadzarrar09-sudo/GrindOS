package com.grindos.app.data.repository

import com.grindos.app.data.local.dao.GolfEntryDao
import com.grindos.app.data.local.entity.GolfEntryEntity
import com.grindos.app.domain.model.GolfEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GolfRepository @Inject constructor(
    private val golfEntryDao: GolfEntryDao
) {
    fun getAllEntries(): Flow<List<GolfEntry>> =
        golfEntryDao.getAllEntries().map { entities -> entities.map { it.toDomain() } }

    fun getEntriesForDate(date: LocalDate): Flow<List<GolfEntry>> =
        golfEntryDao.getEntriesForDate(date).map { entities -> entities.map { it.toDomain() } }

    suspend fun insertEntry(entry: GolfEntry): Long = golfEntryDao.insertEntry(entry.toEntity())

    suspend fun updateEntry(entry: GolfEntry) = golfEntryDao.updateEntry(entry.toEntity())

    suspend fun deleteEntry(entry: GolfEntry) = golfEntryDao.deleteEntry(entry.toEntity())

    private fun GolfEntryEntity.toDomain() = GolfEntry(
        id = id, date = date, drillName = drillName,
        durationMinutes = durationMinutes, notes = notes
    )

    private fun GolfEntry.toEntity() = GolfEntryEntity(
        id = id, date = date, drillName = drillName,
        durationMinutes = durationMinutes, notes = notes
    )
}
