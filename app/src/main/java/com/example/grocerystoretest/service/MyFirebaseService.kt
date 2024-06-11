package com.example.grocerystoretest.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.grocerystoretest.R
import com.example.grocerystoretest.view.BillDetailActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseService : FirebaseMessagingService() {

    private val channelId = "CHANNEL_ID"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            showNotification(it.title ?: "", it.body ?: "")
        }
    }

    private fun showNotification(title: String, body: String) {
        val billId = body.substringAfter("[").substringBefore("]").toInt()
        val notifyIntent = Intent(this, BillDetailActivity::class.java).putExtra("billId", billId)

        val notifyPendingIntent = PendingIntent.getActivity(
            this,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val channelId = channelId
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentIntent(notifyPendingIntent)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setSmallIcon(R.drawable.grocery_store_icon)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MyFirebaseService,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, notificationBuilder.build())
        }

    }

    @SuppressLint("WrongConstant")
    private fun Context.createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Grocery App Notification"
            val descriptionText = "Bill Status notification"
            val importance = NotificationManager.IMPORTANCE_MAX
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}