package com.e444er.notitest

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.lifecycleScope
import com.e444er.notitest.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSimple.setOnClickListener {
            showSimple()
        }
        binding.buttonMessage.setOnClickListener {
            createMessage()
        }
        binding.buttonNews.setOnClickListener {
            createNews()
        }
        binding.buttonGroup.setOnClickListener {
            createGroup()
        }

        binding.buttonProg.setOnClickListener {
            createProgress()
        }

        binding.buttonFirebase.setOnClickListener {
            getToken()
        }
    }

    private fun getToken() {
        lifecycleScope.launch {
            val token = getTokenSuspend()
            Log.d("Token", "Tokennn:$token")
        }
    }

    private suspend fun getTokenSuspend(): String? = suspendCoroutine { cont ->
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener {token ->
                cont.resume(token)
            }
            .addOnFailureListener {
                cont.resume(null)
            }
            .addOnFailureListener {
                cont.resume(null)
            }
    }

    private fun createProgress() {
        val notificationBuilder =
            NotificationCompat.Builder(this, NotificationChannels.MESSAGE_CHANNEL_NEWS_ID)
                .setContentTitle("Update downloading")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setPriority(NotificationCompat.PRIORITY_LOW)

        val maxProgress = 10
        lifecycleScope.launch {
            (0 until maxProgress).forEach { progress ->
                val notification =
                    notificationBuilder
                        .setProgress(maxProgress, progress, false)
                        .build()


                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@forEach
                }

                NotificationManagerCompat.from(this@MainActivity)
                    .notify(PROGRESS_NOTIFICATION, notification)
                delay(500)
            }
            val finalNotification =
                notificationBuilder
                    .setContentText("Download is completed")
                    .setProgress(0, 0, false)
                    .build()


            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@launch
            }

            NotificationManagerCompat.from(this@MainActivity)
                .notify(PROGRESS_NOTIFICATION, finalNotification)
            delay(500)

            NotificationManagerCompat.from(this@MainActivity)
                .cancel(PROGRESS_NOTIFICATION)

        }
    }

    private fun createGroup() {
        val messageCount = 3
        val groupId = "message group"
        (0 until messageCount).forEach { messageIndex ->
            val messageNumber = messageIndex + 1
            val notification =
                NotificationCompat.Builder(this, NotificationChannels.MESSAGE_CHANNEL_ID)
                    .setContentTitle("Message")
                    .setContentText("My content Message $messageNumber")
                    .setSmallIcon(R.drawable.baseline_message_24)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(longArrayOf(100, 300, 100, 300))
                    .setGroup(groupId)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "permission", Toast.LENGTH_SHORT).show()
                return
            }
            NotificationManagerCompat.from(this)
                .notify(messageNumber, notification)
        }

        val summaryNotification =
            NotificationCompat.Builder(this, NotificationChannels.MESSAGE_CHANNEL_ID)
                .setContentTitle("Summary")
                .setContentText("My content Summary ")
                .setSmallIcon(R.drawable.baseline_message_24)
                .setGroup(groupId)
                .setGroupSummary(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        NotificationManagerCompat.from(this)
            .notify(GROUP_NOTIFICATION, summaryNotification)
    }

    private fun createMessage() {

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.baseline_message_24)

        val notification = NotificationCompat.Builder(this, NotificationChannels.MESSAGE_CHANNEL_ID)
            .setContentTitle("Message")
            .setContentText("My content Message ${System.currentTimeMillis()}")
            .setSmallIcon(R.drawable.baseline_message_24)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(100, 300, 100, 300))
            .setLargeIcon(largeIcon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "permission", Toast.LENGTH_SHORT).show()
            return
        }
        NotificationManagerCompat.from(this)
            .notify(MESSAGE_NOTIFICATION, notification)
    }

    private fun createNews() {

        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification =
            NotificationCompat.Builder(this, NotificationChannels.MESSAGE_CHANNEL_NEWS_ID)
                .setContentTitle("NEWS")
                .setContentText("My content News ${System.currentTimeMillis()}")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "permission", Toast.LENGTH_SHORT).show()
            return
        }

        NotificationManagerCompat.from(this)
            .notify(NEWS_NOTIFICATION, notification)

    }

    private fun showSimple() {
        val notification = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle("MY Notification title")
            .setContentText("My content text")
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .build()



        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "permission", Toast.LENGTH_SHORT).show()

            return
        }

        NotificationManagerCompat.from(this)
            .notify(SIMPLE_NOTIFICATION, notification)
    }

    companion object {
        private const val SIMPLE_NOTIFICATION = 1234
        private const val MESSAGE_NOTIFICATION = 345
        private const val NEWS_NOTIFICATION = 324
        private const val GROUP_NOTIFICATION = 23
        private const val PROGRESS_NOTIFICATION = 1432
    }
}