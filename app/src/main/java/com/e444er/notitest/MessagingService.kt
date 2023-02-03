package com.e444er.notitest

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val refreshToken = FirebaseMessaging.getInstance().token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        showNotification(
            message.notification?.title.toString(),
            message.notification?.body.toString()
        )
    }

    private fun showNotification(title: String, message: String) {

        val intent = Intent(this, MainActivity::class.java)
        val notificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val builder = NotificationCompat.Builder(
            this, "firebase"
        )
            .setContentTitle(title)
            .setContentTitle(message)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .build()

        notificationManager.notify(213, builder)

    }
}