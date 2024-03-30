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

class TimerService : Service(), ITimerManager {
    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false
    private var service: NotificationManager? = null
    private var notificationBuilder: NotificationCompat.Builder? = null
    private val binder = TimerBinder()
    private var remainTime: Int = 0

    companion object {
        var remainTimeFormatted = ""
    }

    inner class TimerBinder : Binder() {
        fun getService(): TimerService {
            return this@TimerService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val hour = intent!!.getIntExtra("hour", 0)
        val minute = intent!!.getIntExtra("minute", 0)
        val countdownTime = (hour * 60 * 60) + (minute * 60)
        startForegroundService(countdownTime)
        return START_STICKY
    }

    private fun startForegroundService(countdownTime: Int) {
        if (!isTimerRunning) {
            var notification = createNotification()
            startForeground(1, notification)
            countDownTimer = object : CountDownTimer((countdownTime * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if (isTimerRunning) {
                        val secondsLeft = (millisUntilFinished / 1000).toInt()
                        Log.d("R/T", secondsLeft.toString())
                        remainTime = secondsLeft
                        val intent = Intent("TIMER_TICK")
                        remainTimeFormatted = formatTime(secondsLeft)
                        intent.putExtra("timeLeftFormatted", remainTimeFormatted)
                        intent.putExtra("timeLeft", secondsLeft)
                        sendBroadcast(intent)

                        notificationBuilder?.let {
                            it.setContentText(formatTime(secondsLeft))
                            notification = it.build()
                            service!!.notify(1, notification)
                        }
                    }
                    Log.d("R/T", "Tetikleniyor")
                }

                private fun formatTime(secondsLeft: Int): String {
                    val hours = secondsLeft / 3600
                    val minutes = (secondsLeft % 3600) / 60
                    val seconds = secondsLeft % 60
                    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
                }

                override fun onFinish() {
                    isTimerRunning = false
                    stopSelf()
                }

            }
            countDownTimer?.start()
            isTimerRunning = true
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
            .setSmallIcon(R.drawable.ic_timer)
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
        val countdownTime = (hour * 60 * 60) + (minute * 60)
        startForegroundService(countdownTime)
    }

    override fun pauseTimer() {
        if (isTimerRunning) {
            Log.d("R/T", "timer is paused")
            countDownTimer?.cancel()
            isTimerRunning = false
        }
    }

    override fun resumeTimer() {
        if (!isTimerRunning && countDownTimer != null) {
            Log.d("R/T", "timer is resumed")
            startForegroundService(remainTime)
            isTimerRunning = true
        }
    }

    override fun cancelTimer() {
        Log.d("R/T", "timer is canceled")
        service?.cancel(1)
        countDownTimer?.cancel()
        isTimerRunning = false
        stopSelf()
    }

}
