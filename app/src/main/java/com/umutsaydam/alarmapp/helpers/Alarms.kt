package com.umutsaydam.alarmapp.helpers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.umutsaydam.alarmapp.receivers.AlarmReceiver
import com.umutsaydam.alarmapp.models.AlarmModel

class Alarms(private val context: Context) :
    IAlarmManager {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    // private val intent = Intent(context, AlarmReceiver::class.java)

    override fun createAlarm(alarmModel: AlarmModel) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("alarmId", alarmModel.alarmId)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmModel.alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager[AlarmManager.RTC_WAKEUP, alarmModel.alarmTime] = pendingIntent
    }

    override fun updateAlarm(alarmModel: AlarmModel) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("alarmId", alarmModel.alarmId)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmModel.alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (alarmModel.alarmEnabled) {
            alarmManager[AlarmManager.RTC_WAKEUP, alarmModel.alarmTime] = pendingIntent
        } else {
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