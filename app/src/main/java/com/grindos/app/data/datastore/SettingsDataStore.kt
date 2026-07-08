package com.grindos.app.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "grindos_settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val NOTIFICATION_MODE = stringPreferencesKey("notification_mode")
        val DEFAULT_REMINDER_MINUTES = intPreferencesKey("default_reminder_minutes")
        val DAILY_STUDY_TARGET_MINUTES = intPreferencesKey("daily_study_target_minutes")
        val DAILY_QURAN_PAGES = intPreferencesKey("daily_quran_pages")
        val DAILY_BOOK_PAGES = intPreferencesKey("daily_book_pages")
        val TOTAL_XP = intPreferencesKey("total_xp")
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val LONGEST_STREAK = intPreferencesKey("longest_streak")
        val LEVEL = stringPreferencesKey("level")
        val LAST_ACTIVE_DATE = stringPreferencesKey("last_active_date")
    }

    val notificationMode: Flow<String> = context.dataStore.data.map { it[Keys.NOTIFICATION_MODE] ?: "GENTLE" }
    val defaultReminderMinutes: Flow<Int> = context.dataStore.data.map { it[Keys.DEFAULT_REMINDER_MINUTES] ?: 15 }
    val dailyStudyTargetMinutes: Flow<Int> = context.dataStore.data.map { it[Keys.DAILY_STUDY_TARGET_MINUTES] ?: 120 }
    val dailyQuranPages: Flow<Int> = context.dataStore.data.map { it[Keys.DAILY_QURAN_PAGES] ?: 2 }
    val dailyBookPages: Flow<Int> = context.dataStore.data.map { it[Keys.DAILY_BOOK_PAGES] ?: 10 }
    val totalXp: Flow<Int> = context.dataStore.data.map { it[Keys.TOTAL_XP] ?: 0 }
    val currentStreak: Flow<Int> = context.dataStore.data.map { it[Keys.CURRENT_STREAK] ?: 0 }
    val longestStreak: Flow<Int> = context.dataStore.data.map { it[Keys.LONGEST_STREAK] ?: 0 }
    val level: Flow<String> = context.dataStore.data.map { it[Keys.LEVEL] ?: "Rookie" }
    val lastActiveDate: Flow<String?> = context.dataStore.data.map { it[Keys.LAST_ACTIVE_DATE] }

    suspend fun setNotificationMode(mode: String) {
        context.dataStore.edit { it[Keys.NOTIFICATION_MODE] = mode }
    }

    suspend fun setDefaultReminderMinutes(minutes: Int) {
        context.dataStore.edit { it[Keys.DEFAULT_REMINDER_MINUTES] = minutes }
    }

    suspend fun setDailyStudyTargetMinutes(minutes: Int) {
        context.dataStore.edit { it[Keys.DAILY_STUDY_TARGET_MINUTES] = minutes }
    }

    suspend fun setDailyQuranPages(pages: Int) {
        context.dataStore.edit { it[Keys.DAILY_QURAN_PAGES] = pages }
    }

    suspend fun setDailyBookPages(pages: Int) {
        context.dataStore.edit { it[Keys.DAILY_BOOK_PAGES] = pages }
    }

    suspend fun addXp(xp: Int) {
        context.dataStore.edit {
            val current = it[Keys.TOTAL_XP] ?: 0
            it[Keys.TOTAL_XP] = current + xp
        }
    }

    suspend fun setCurrentStreak(streak: Int) {
        context.dataStore.edit { it[Keys.CURRENT_STREAK] = streak }
    }

    suspend fun updateLongestStreak(streak: Int) {
        context.dataStore.edit {
            val current = it[Keys.LONGEST_STREAK] ?: 0
            if (streak > current) it[Keys.LONGEST_STREAK] = streak
        }
    }

    suspend fun setLevel(level: String) {
        context.dataStore.edit { it[Keys.LEVEL] = level }
    }

    suspend fun setLastActiveDate(date: String) {
        context.dataStore.edit { it[Keys.LAST_ACTIVE_DATE] = date }
    }
}
