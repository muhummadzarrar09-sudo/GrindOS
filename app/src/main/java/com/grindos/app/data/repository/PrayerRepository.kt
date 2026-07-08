package com.grindos.app.data.repository

import com.grindos.app.data.local.dao.PrayerDao
import com.grindos.app.data.local.entity.PrayerEntity
import com.grindos.app.domain.model.Prayer
import com.grindos.app.domain.model.PrayerName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerRepository @Inject constructor(
    private val prayerDao: PrayerDao
) {
    fun getPrayersForDate(date: LocalDate): Flow<List<Prayer>> =
        prayerDao.getPrayersForDate(date).map { entities -> entities.map { it.toDomain() } }

    fun getCompletedPrayerCount(date: LocalDate): Flow<Int> =
        prayerDao.getCompletedPrayerCount(date)

    suspend fun getPrayer(date: LocalDate, prayerName: PrayerName): Prayer? =
        prayerDao.getPrayer(date, prayerName)?.toDomain()

    suspend fun insertPrayer(prayer: Prayer): Long = prayerDao.insertPrayer(prayer.toEntity())

    suspend fun updatePrayer(prayer: Prayer) = prayerDao.updatePrayer(prayer.toEntity())

    suspend fun setPrayerCompleted(prayerId: Long, completed: Boolean) =
        prayerDao.setPrayerCompleted(prayerId, completed)

    suspend fun ensureDayPrayersExist(date: LocalDate) {
        PrayerName.entries.forEach { name ->
            if (prayerDao.getPrayer(date, name) == null) {
                prayerDao.insertPrayer(
                    PrayerEntity(
                        prayerName = name,
                        date = date,
                        time = getDefaultPrayerTime(name)
                    )
                )
            }
        }
    }

    private fun getDefaultPrayerTime(name: PrayerName) = when (name) {
        PrayerName.FAJR -> java.time.LocalTime.of(5, 0)
        PrayerName.DHUHR -> java.time.LocalTime.of(12, 30)
        PrayerName.ASR -> java.time.LocalTime.of(15, 45)
        PrayerName.MAGHRIB -> java.time.LocalTime.of(18, 30)
        PrayerName.ISHA -> java.time.LocalTime.of(20, 0)
    }

    private fun PrayerEntity.toDomain() = Prayer(
        id = id, prayerName = prayerName, date = date, time = time,
        completed = completed, reminderMinutesBefore = reminderMinutesBefore
    )

    private fun Prayer.toEntity() = PrayerEntity(
        id = id, prayerName = prayerName, date = date, time = time,
        completed = completed, reminderMinutesBefore = reminderMinutesBefore
    )
}
