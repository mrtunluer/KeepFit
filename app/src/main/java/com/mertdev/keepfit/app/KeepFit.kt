package com.mertdev.keepfit.app

import android.app.Application
import com.mertdev.keepfit.utils.notification.NotificationOperations
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KeepFit : Application() {

    override fun onCreate() {
        super.onCreate()
        with(NotificationOperations) {
            createNotificationChannel(applicationContext)
            setReminder(applicationContext)
        }
    }

}