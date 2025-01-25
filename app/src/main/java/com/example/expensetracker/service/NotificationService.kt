package com.example.expensetracker.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.expensetracker.MainActivity
import com.example.expensetracker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {
    
    companion object {
        private const val CHANNEL_ID = "expense_tracker_channel"
        private const val CHANNEL_NAME = "Expense Notifications"
        private const val NOTIFICATION_ID = 1
    }

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var tokenRepository: TokenRepository

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        // Handle FCM message
        message.notification?.let { notification ->
            showNotification(
                title = notification.title ?: "Expense Tracker",
                message = notification.body ?: ""
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        auth.currentUser?.uid?.let { userId ->
            tokenRepository.updateFcmToken(userId)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Expense tracking notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
} 