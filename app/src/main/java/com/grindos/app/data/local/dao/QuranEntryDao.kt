package com.grindos.app.data.local.dao

import androidx.room.*
import com.grindos.app.data.local.entity.QuranEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface QuranEntryDao {

    @Query("SELECT * FROM quran_entries WHERE date = :date LIMIT 1")
    suspend fun getEntryForDate(date: LocalDate): QuranEntryEntity?

    @Query("SELECT * FROM quran_entries WHERE date = :date LIMIT 1")
    fun observeEntryForDate(date: LocalDate): Flow<QuranEntryEntity?>

    @Query("SELECT * FROM quran_entries ORDER BY date DESC LIMIT :limit")
    fun getRecentEntries(limit: Int = 30): Flow<List<QuranEntryEntity>>

    @Query("SELECT SUM(pagesRead) FROM quran_entries WHERE date BETWEEN :startDate AND :endDate")
    fun getTotalPagesInRange(startDate: LocalDate, endDate: LocalDate): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: QuranEntryEntity): Long

    @Update
    suspend fun updateEntry(entry: QuranEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: QuranEntryEntity)
}
