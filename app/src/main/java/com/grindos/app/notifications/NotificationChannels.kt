package com.grindos.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {

    const val CHANNEL_PRAYER = "prayer_reminders"
    const val CHANNEL_STUDY = "study_reminders"
    const val CHANNEL_HABIT = "habit_reminders"
    const val CHANNEL_HACKATHON = "hackathon_reminders"
    const val CHANNEL_PANIC = "panic_reset"

    fun createAllChannels(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channels = listOf(
            NotificationChannel(
                CHANNEL_PRAYER,
                "Prayer Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Salah reminders for Fajr, Dhuhr, Asr, Maghrib, Isha"
                enableVibration(true)
            },
            NotificationChannel(
                CHANNEL_STUDY,
                "Study Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "FAST prep study sprint reminders"
            },
            NotificationChannel(
                CHANNEL_HABIT,
                "Habit Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Quran, books, and golf practice reminders"
            },
            NotificationChannel(
                CHANNEL_HACKATHON,
                "Hackathon Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Project deadline and task reminders"
            },
            NotificationChannel(
                CHANNEL_PANIC,
                "Panic Reset",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Recovery and reset notifications"
            }
        )

        notificationManager.createNotificationChannels(channels)
    }
}
