package com.umutsaydam.alarmapp.helpers

import com.umutsaydam.alarmapp.models.AlarmModel

interface IAlarmManager {
    fun createAlarm(alarmId: Int, timeInMillis: Long){}

    fun updateAlarm(alarmModel: AlarmModel){}

    fun deleteAlarm(alarmModel: AlarmModel){}
}