package com.grindos.app.data.repository

import com.grindos.app.data.local.dao.BookEntryDao
import com.grindos.app.data.local.entity.BookEntryEntity
import com.grindos.app.domain.model.BookEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookEntryDao: BookEntryDao
) {
    fun getAllEntries(): Flow<List<BookEntry>> =
        bookEntryDao.getAllEntries().map { entities -> entities.map { it.toDomain() } }

    fun getEntriesByBook(bookName: String): Flow<List<BookEntry>> =
        bookEntryDao.getEntriesByBook(bookName).map { entities -> entities.map { it.toDomain() } }

    fun getEntriesForDate(date: LocalDate): Flow<List<BookEntry>> =
        bookEntryDao.getEntriesForDate(date).map { entities -> entities.map { it.toDomain() } }

    suspend fun insertEntry(entry: BookEntry): Long = bookEntryDao.insertEntry(entry.toEntity())

    suspend fun updateEntry(entry: BookEntry) = bookEntryDao.updateEntry(entry.toEntity())

    suspend fun deleteEntry(entry: BookEntry) = bookEntryDao.deleteEntry(entry.toEntity())

    private fun BookEntryEntity.toDomain() = BookEntry(
        id = id, bookName = bookName, date = date, pagesReadToday = pagesReadToday,
        dailyTarget = dailyTarget, totalPagesRead = totalPagesRead, notes = notes
    )

    private fun BookEntry.toEntity() = BookEntryEntity(
        id = id, bookName = bookName, date = date, pagesReadToday = pagesReadToday,
        dailyTarget = dailyTarget, totalPagesRead = totalPagesRead, notes = notes
    )
}
