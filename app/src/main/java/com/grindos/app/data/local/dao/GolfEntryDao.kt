package com.grindos.app.data.local.dao

import androidx.room.*
import com.grindos.app.data.local.entity.GolfEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface GolfEntryDao {

    @Query("SELECT * FROM golf_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<GolfEntryEntity>>

    @Query("SELECT * FROM golf_entries WHERE date = :date")
    fun getEntriesForDate(date: LocalDate): Flow<List<GolfEntryEntity>>

    @Query("SELECT SUM(durationMinutes) FROM golf_entries WHERE date BETWEEN :startDate AND :endDate")
    fun getTotalDurationInRange(startDate: LocalDate, endDate: LocalDate): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: GolfEntryEntity): Long

    @Update
    suspend fun updateEntry(entry: GolfEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: GolfEntryEntity)
}
