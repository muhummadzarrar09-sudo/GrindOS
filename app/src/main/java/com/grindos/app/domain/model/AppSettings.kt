package com.grindos.app.domain.model

data class AppSettings(
    val notificationMode: NotificationTone = NotificationTone.GENTLE,
    val defaultReminderMinutes: Int = 15,
    val dailyStudyTargetMinutes: Int = 120,
    val dailyQuranPages: Int = 2,
    val dailyBookPages: Int = 10,
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val level: String = "Rookie"
)
