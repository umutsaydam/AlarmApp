package com.umutsaydam.alarmapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.umutsaydam.alarmapp.R

class AlarmNotificationService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val icon = R.drawable.ic_alarm
        startForeground(startId, createNotification(icon))
        return START_NOT_STICKY
    }

    private fun createNotification(icon: Int): Notification {
        val builder = NotificationCompat.Builder(this, "alarm_channel")
            .setSmallIcon(icon)
            .setPriority(NotificationCompat.PRIORITY_MIN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            builder.setChannelId("alarm_channel")
        }
        return builder.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channelId = "alarm_channel"
        val channelName = "Alarm Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }
}