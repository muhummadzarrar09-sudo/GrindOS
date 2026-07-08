package com.grindos.app

import android.app.Application
import com.grindos.app.notifications.NotificationChannels
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GrindOSApp : Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationChannels.createAllChannels(this)
    }
}
