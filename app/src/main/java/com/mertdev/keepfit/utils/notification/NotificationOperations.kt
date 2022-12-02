package com.mertdev.keepfit.utils.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mertdev.keepfit.utils.Constants.HOUR_OF_DAY
import com.mertdev.keepfit.utils.Constants.MINUTE
import com.mertdev.keepfit.utils.Constants.NOTIFICATION_ID
import com.mertdev.keepfit.utils.Constants.NOTIFICATION_NAME
import java.util.*

object NotificationOperations {

    fun createNotificationChannel(
        applicationContext: Context
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            applicationContext.getSystemService(
                NotificationManager::class.java
            ).createNotificationChannel(channel)
        }
    }


    private fun getReminderTime(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY)
        calendar.set(Calendar.MINUTE, MINUTE)
        calendar.set(Calendar.SECOND, 0)

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return calendar
    }

    fun setReminder(
        applicationContext: Context
    ) {
        val time = getReminderTime()

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            Intent(applicationContext, Notification::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            applicationContext.getSystemService(Application.ALARM_SERVICE) as AlarmManager

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, time.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, time.timeInMillis, pendingIntent
        )
    }

}