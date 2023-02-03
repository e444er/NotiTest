package com.e444er.notitest

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val  CHAN= "message"
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@RequiresApi(Build.VERSION_CODES.O)
class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        generateNotification(
            message.notification?.title.toString(),
            message.notification?.body.toString(),
        )
    }

    @SuppressLint("LaunchActivityFromNotification", "UnspecifiedImmutableFlag")
    fun generateNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT
        )
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHAN)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(100, 200, 200, 100))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "News"
        val newsDescription = "App News message"

        val channelNews =
            NotificationChannel(CHAN, name, NotificationManager.IMPORTANCE_HIGH).apply {
                description = newsDescription
            }
        notificationManager.createNotificationChannel(channelNews)
        notificationManager.notify(0, builder.build())

    }
}