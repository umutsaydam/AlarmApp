package com.umutsaydam.alarmapp.helpers

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.umutsaydam.alarmapp.services.AlarmNotificationService

class AlarmNotification(private val context: Context) : IAlarmNotification {

    companion object {
        private var countOfEnabledAlarms = 0
        private var isNotificationEnabled = false

        fun increaseCountOfEnabledAlarms() {
            countOfEnabledAlarms++
        }

        fun decreaseCountOfEnabledAlarms() {
            countOfEnabledAlarms--
        }

        fun updateCountOfEnabledAlarms(count: Int) {
            countOfEnabledAlarms = count
        }
    }

    override fun checkAlarmNotificationState() {
        when (countOfEnabledAlarms) {
            0 -> {
                if (isNotificationEnabled){
                    stopService()
                    isNotificationEnabled = false
                }
            }
            1 -> {
                if (!isNotificationEnabled){
                    startForegroundService()
                    isNotificationEnabled = true
                }
            }
        }
    }

    override fun startForegroundService() { 
        val serviceIntent = Intent(context, AlarmNotificationService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
        Log.d("R/T", "service was started function")
    }

    override fun stopService() {
        val serviceIntent = Intent(context, AlarmNotificationService::class.java)
        context.stopService(serviceIntent)
        Log.d("R/T", "service was started function")
    }
}