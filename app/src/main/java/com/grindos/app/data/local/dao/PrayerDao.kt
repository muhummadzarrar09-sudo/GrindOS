package com.grindos.app.data.local.dao

import androidx.room.*
import com.grindos.app.data.local.entity.PrayerEntity
import com.grindos.app.domain.model.PrayerName
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PrayerDao {

    @Query("SELECT * FROM prayers WHERE date = :date ORDER BY time ASC")
    fun getPrayersForDate(date: LocalDate): Flow<List<PrayerEntity>>

    @Query("SELECT * FROM prayers WHERE date = :date AND prayerName = :prayerName LIMIT 1")
    suspend fun getPrayer(date: LocalDate, prayerName: PrayerName): PrayerEntity?

    @Query("SELECT COUNT(*) FROM prayers WHERE date = :date AND completed = 1")
    fun getCompletedPrayerCount(date: LocalDate): Flow<Int>

    @Query("SELECT * FROM prayers WHERE date BETWEEN :startDate AND :endDate AND completed = 1 ORDER BY date ASC")
    fun getCompletedPrayersInRange(startDate: LocalDate, endDate: LocalDate): Flow<List<PrayerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayer(prayer: PrayerEntity): Long

    @Update
    suspend fun updatePrayer(prayer: PrayerEntity)

    @Query("UPDATE prayers SET completed = :completed WHERE id = :prayerId")
    suspend fun setPrayerCompleted(prayerId: Long, completed: Boolean)

    @Delete
    suspend fun deletePrayer(prayer: PrayerEntity)
}
