package com.grindos.app.data.repository

import com.grindos.app.data.datastore.SettingsDataStore
import com.grindos.app.domain.model.AppSettings
import com.grindos.app.domain.model.NotificationTone
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) {
    val settings: Flow<AppSettings> = combine(
        settingsDataStore.notificationMode,
        settingsDataStore.defaultReminderMinutes,
        settingsDataStore.dailyStudyTargetMinutes,
        settingsDataStore.dailyQuranPages,
        settingsDataStore.dailyBookPages,
        settingsDataStore.totalXp,
        settingsDataStore.currentStreak,
        settingsDataStore.longestStreak,
        settingsDataStore.level
    ) { values ->
        AppSettings(
            notificationMode = runCatching { NotificationTone.valueOf(values[0] as String) }.getOrDefault(NotificationTone.GENTLE),
            defaultReminderMinutes = values[1] as Int,
            dailyStudyTargetMinutes = values[2] as Int,
            dailyQuranPages = values[3] as Int,
            dailyBookPages = values[4] as Int,
            totalXp = values[5] as Int,
            currentStreak = values[6] as Int,
            longestStreak = values[7] as Int,
            level = values[8] as String
        )
    }

    val totalXp: Flow<Int> = settingsDataStore.totalXp
    val currentStreak: Flow<Int> = settingsDataStore.currentStreak
    val longestStreak: Flow<Int> = settingsDataStore.longestStreak
    val level: Flow<String> = settingsDataStore.level

    suspend fun setNotificationMode(mode: NotificationTone) =
        settingsDataStore.setNotificationMode(mode.name)

    suspend fun setDefaultReminderMinutes(minutes: Int) =
        settingsDataStore.setDefaultReminderMinutes(minutes)

    suspend fun setDailyStudyTargetMinutes(minutes: Int) =
        settingsDataStore.setDailyStudyTargetMinutes(minutes)

    suspend fun setDailyQuranPages(pages: Int) =
        settingsDataStore.setDailyQuranPages(pages)

    suspend fun setDailyBookPages(pages: Int) =
        settingsDataStore.setDailyBookPages(pages)

    suspend fun addXp(xp: Int) = settingsDataStore.addXp(xp)

    suspend fun setCurrentStreak(streak: Int) = settingsDataStore.setCurrentStreak(streak)

    suspend fun updateLongestStreak(streak: Int) = settingsDataStore.updateLongestStreak(streak)

    suspend fun setLevel(level: String) = settingsDataStore.setLevel(level)

    suspend fun setLastActiveDate(date: String) = settingsDataStore.setLastActiveDate(date)
}
