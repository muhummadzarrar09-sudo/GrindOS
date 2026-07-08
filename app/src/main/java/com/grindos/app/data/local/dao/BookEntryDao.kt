package com.grindos.app.data.local.dao

import androidx.room.*
import com.grindos.app.data.local.entity.BookEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface BookEntryDao {

    @Query("SELECT * FROM book_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<BookEntryEntity>>

    @Query("SELECT * FROM book_entries WHERE bookName = :bookName ORDER BY date DESC")
    fun getEntriesByBook(bookName: String): Flow<List<BookEntryEntity>>

    @Query("SELECT * FROM book_entries WHERE date = :date")
    fun getEntriesForDate(date: LocalDate): Flow<List<BookEntryEntity>>

    @Query("SELECT SUM(pagesReadToday) FROM book_entries WHERE date = :date")
    fun getTotalPagesReadOnDate(date: LocalDate): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: BookEntryEntity): Long

    @Update
    suspend fun updateEntry(entry: BookEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: BookEntryEntity)
}
