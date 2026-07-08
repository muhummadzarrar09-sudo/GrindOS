package com.grindos.app.data.repository

import com.grindos.app.data.local.dao.QuranEntryDao
import com.grindos.app.data.local.entity.QuranEntryEntity
import com.grindos.app.domain.model.QuranEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuranRepository @Inject constructor(
    private val quranEntryDao: QuranEntryDao
) {
    fun observeEntryForDate(date: LocalDate): Flow<QuranEntry?> =
        quranEntryDao.observeEntryForDate(date).map { it?.toDomain() }

    fun getRecentEntries(limit: Int = 30): Flow<List<QuranEntry>> =
        quranEntryDao.getRecentEntries(limit).map { entities -> entities.map { it.toDomain() } }

    suspend fun getEntryForDate(date: LocalDate): QuranEntry? =
        quranEntryDao.getEntryForDate(date)?.toDomain()

    suspend fun insertOrUpdateEntry(entry: QuranEntry): Long =
        quranEntryDao.insertEntry(entry.toEntity())

    suspend fun deleteEntry(entry: QuranEntry) = quranEntryDao.deleteEntry(entry.toEntity())

    private fun QuranEntryEntity.toDomain() = QuranEntry(
        id = id, date = date, targetPages = targetPages,
        pagesRead = pagesRead, notes = notes
    )

    private fun QuranEntry.toEntity() = QuranEntryEntity(
        id = id, date = date, targetPages = targetPages,
        pagesRead = pagesRead, notes = notes
    )
}
