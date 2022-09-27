package com.mertdev.weighttracking.app

import android.app.Application
import com.mertdev.weighttracking.notification.NotificationOperations
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeightTracking : Application(){

    override fun onCreate() {
        super.onCreate()
        with(NotificationOperations) {
            createNotificationChannel(applicationContext)
            setReminder(applicationContext)
        }
    }

}