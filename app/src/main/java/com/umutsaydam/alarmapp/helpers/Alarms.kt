package com.umutsaydam.alarmapp.helpers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.umutsaydam.alarmapp.AlarmReceiver
import com.umutsaydam.alarmapp.models.AlarmModel

class Alarms(private val context: Context) : IAlarmManager {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun createAlarm(alarmId: Int, timeInMillis: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager[AlarmManager.RTC_WAKEUP, timeInMillis] = pendingIntent
    }

    override fun updateAlarm(alarmModel: AlarmModel) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmModel.alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (alarmModel.alarmEnabled) {
            Log.d("R/T", alarmModel.alarmId.toString() + " etkin")
            alarmManager[AlarmManager.RTC_WAKEUP, alarmModel.alarmTime] = pendingIntent
        } else {
            Log.d("R/T", alarmModel.alarmId.toString() + " devre disi")
            alarmManager[AlarmManager.RTC_WAKEUP, alarmModel.alarmTime] = pendingIntent
            alarmManager.cancel(pendingIntent)
        }
    }

    override fun deleteAlarm(alarmModel: AlarmModel) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmModel.alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager[AlarmManager.RTC_WAKEUP, alarmModel.alarmTime] = pendingIntent
        alarmManager.cancel(pendingIntent)
    }
}