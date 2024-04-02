package com.umutsaydam.alarmapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.umutsaydam.alarmapp.R
import com.umutsaydam.alarmapp.helpers.ITimerManager

class StopWatchService : Service(), ITimerManager {
    private var isStopWatchRunning = false
    private var service: NotificationManager? = null
    private var notificationBuilder: NotificationCompat.Builder? = null
    private val binder = TimerBinder()
    private lateinit var countDownTimer: CountDownTimer
    private var startTime: Long = 0

    inner class TimerBinder : Binder() {
        fun getService(): StopWatchService {
            return this@StopWatchService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startTime = System.currentTimeMillis()
        startForegroundService()
        return START_STICKY
    }
    private fun startForegroundService() {
        if (!isStopWatchRunning) {
            var notification = createNotification()
            startForeground(2, notification)
            countDownTimer = object : CountDownTimer(3600000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if (isStopWatchRunning) {
                        val intent = Intent("STOPWATCH_TICK")
                        val formattedTime = formatTime()
                        TimerService.remainTimeFormatted = formattedTime
                        intent.putExtra("timeFormatted", formattedTime)
                        sendBroadcast(intent)

                        notificationBuilder?.let {
                            it.setContentText(formattedTime)
                            notification = it.build()
                            service!!.notify(2, notification)
                        }
                    }
                }

                private fun formatTime(): String {
                    val elapsedTime = System.currentTimeMillis() - startTime
                    val seconds = (elapsedTime / 1000 % 60).toInt()
                    val minutes = (elapsedTime / 1000 / 60 % 60).toInt()
                    val hours = (elapsedTime / 1000 / 60 / 60).toInt()
                    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
                }
                override fun onFinish() {
                    isStopWatchRunning = false
                    stopSelf()
                }

            }
            countDownTimer.start()
            isStopWatchRunning = true
        }
    }

    private fun createNotification(): Notification {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                ""
            }
        notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
        return notificationBuilder!!.setOngoing(true)
            .setContentTitle("Timer Service")
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.ic_stop_watch)
            .setContentText("test")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        service = getSystemService(NotificationManager::class.java) as NotificationManager
        service!!.createNotificationChannel(channel)
        return channelId
    }

    override fun startTimer(hour: Int, minute: Int) {
    }

    override fun pauseTimer() {
        if (isStopWatchRunning) {
            Log.d("R/T", "timer is paused")
            countDownTimer.cancel()
            isStopWatchRunning = false
        }
    }

    override fun resumeTimer() {
        if (!isStopWatchRunning) {
            Log.d("R/T", "timer is resumed")
            startForegroundService()
            isStopWatchRunning = true
        }
    }

    override fun cancelTimer() {
        Log.d("R/T", "timer is canceled")
        service?.cancel(2)
        countDownTimer.cancel()
        isStopWatchRunning = false
        stopSelf()
    }
}