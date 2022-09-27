package com.mertdev.weighttracking.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.app.MainActivity
import com.mertdev.weighttracking.utils.Constants.NOTIFICATION_ID
import com.mertdev.weighttracking.utils.Constants.NOTIFY_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Notification : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent?) {
        val repeatingIntent = Intent(context, MainActivity::class.java)
        repeatingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            repeatingIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, NOTIFICATION_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_content))
                .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFY_ID, builder.build())
    }

}