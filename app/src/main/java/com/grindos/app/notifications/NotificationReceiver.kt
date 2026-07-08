package com.grindos.app.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.grindos.app.MainActivity
import com.grindos.app.domain.model.NotificationTone

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "GrindOS"
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "Time to lock in!"
        val channelId = intent.getStringExtra(EXTRA_CHANNEL_ID) ?: NotificationChannels.CHANNEL_STUDY
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, System.currentTimeMillis().toInt())
        val toneName = intent.getStringExtra(EXTRA_TONE) ?: NotificationTone.GENTLE.name

        val styledMessage = applyToneStyle(message, NotificationTone.valueOf(toneName))

        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, notificationId, launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(styledMessage)
            .setStyle(NotificationCompat.BigTextStyle().bigText(styledMessage))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (e: SecurityException) {
            // Notification permission not granted
        }
    }

    private fun applyToneStyle(message: String, tone: NotificationTone): String {
        return when (tone) {
            NotificationTone.GENTLE -> message
            NotificationTone.HYPE -> "🔥 $message LET'S GO!"
            NotificationTone.ROAST -> "💀 $message Stop scrolling."
            NotificationTone.DEEN_FIRST -> "🤲 $message Barakah mode."
            NotificationTone.EXAM_WAR -> "🎯 $message FAST is watching."
        }
    }

    companion object {
        const val EXTRA_TITLE = "notification_title"
        const val EXTRA_MESSAGE = "notification_message"
        const val EXTRA_CHANNEL_ID = "channel_id"
        const val EXTRA_NOTIFICATION_ID = "notification_id"
        const val EXTRA_TONE = "notification_tone"
    }
}
