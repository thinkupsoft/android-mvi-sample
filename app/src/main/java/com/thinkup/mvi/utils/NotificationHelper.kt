package com.thinkup.mvi.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.thinkup.mvi.MainActivity
import com.thinkup.mvi.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.random.Random

class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun showNotification(title: String?, body: String?, data: Map<String, String>) {
        val intent = Intent(context, MainActivity::class.java)
        intent.also {
            data.forEach { entry ->
                it.putExtra(entry.key, entry.value)
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        val notificationBuilder = NotificationCompat.Builder(context, PushFirebaseMessagingService.CHANNEL_HIGH_IMPORTANCE)
            .setSmallIcon(R.drawable.tkup_push_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Random(System.currentTimeMillis()).nextInt(1000), notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(): String {
        val notificationManager = context.getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
        val callInviteChannel = NotificationChannel(
            PushFirebaseMessagingService.CHANNEL_HIGH_IMPORTANCE,
            PushFirebaseMessagingService.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(callInviteChannel)
        return PushFirebaseMessagingService.CHANNEL_HIGH_IMPORTANCE
    }
}