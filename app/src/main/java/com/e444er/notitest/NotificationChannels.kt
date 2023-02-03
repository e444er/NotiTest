package com.e444er.notitest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

object NotificationChannels {

    val MESSAGE_CHANNEL_ID = "message"
    val MESSAGE_CHANNEL_NEWS_ID = "news"

    fun create(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createMessageChannel(context)
            createNewsChannel(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMessageChannel(context: Context) {
        val name = "Message"
        val priority = NotificationManager.IMPORTANCE_HIGH
        val channelDescription = "Urgent message"

        val channel = NotificationChannel(MESSAGE_CHANNEL_ID, name, priority).apply {
            description = channelDescription
            enableVibration(true)
            vibrationPattern = longArrayOf(
                100, 300, 100, 300
            )
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNewsChannel(context: Context) {
        val name = "News"
        val priority = NotificationManager.IMPORTANCE_LOW
        val newsDescription = "App News message"

        val channelNews = NotificationChannel(MESSAGE_CHANNEL_NEWS_ID, name, priority).apply {
            description = newsDescription
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channelNews)
    }
}