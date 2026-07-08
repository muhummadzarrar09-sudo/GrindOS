package com.grindos.app.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.grindos.app.domain.model.NotificationTone
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleNotification(
        title: String,
        message: String,
        date: LocalDate,
        time: LocalTime,
        channelId: String,
        tone: NotificationTone = NotificationTone.GENTLE,
        requestId: Int = System.currentTimeMillis().toInt()
    ) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.EXTRA_TITLE, title)
            putExtra(NotificationReceiver.EXTRA_MESSAGE, message)
            putExtra(NotificationReceiver.EXTRA_CHANNEL_ID, channelId)
            putExtra(NotificationReceiver.EXTRA_NOTIFICATION_ID, requestId)
            putExtra(NotificationReceiver.EXTRA_TONE, tone.name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, requestId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = date.atTime(time)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        // Only schedule if in the future
        if (triggerTime > System.currentTimeMillis()) {
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } catch (e: SecurityException) {
                // Fallback to inexact alarm if exact alarm permission not granted
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        }
    }

    fun cancelNotification(requestId: Int) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, requestId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun generateNotificationMessage(
        tone: NotificationTone,
        taskTitle: String,
        category: String
    ): String {
        return when (tone) {
            NotificationTone.GENTLE -> "Time for $taskTitle. Small win today."
            NotificationTone.HYPE -> "BRO LOCK IN. $taskTitle starts now 🔥"
            NotificationTone.ROAST -> "Scholarship won't spawn while you scroll reels 💀 $taskTitle"
            NotificationTone.DEEN_FIRST -> "Salah first. Everything else gets barakah after 🤲"
            NotificationTone.EXAM_WAR -> "40-sec MCQ mode. FAST is watching 🎯 $taskTitle"
        }
    }
}
