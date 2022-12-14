package com.mertdev.keepfit.utils.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mertdev.keepfit.R
import com.mertdev.keepfit.app.MainActivity
import com.mertdev.keepfit.utils.Constants.NOTIFICATION_ID
import com.mertdev.keepfit.utils.Constants.NOTIFY_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Notification : BroadcastReceiver() {

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
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_content))
                .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFY_ID, builder.build())
    }

}