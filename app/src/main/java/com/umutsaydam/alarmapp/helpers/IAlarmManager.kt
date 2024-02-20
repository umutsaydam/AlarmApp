package com.umutsaydam.alarmapp.helpers

import com.umutsaydam.alarmapp.models.AlarmModel

interface IAlarmManager {
    fun createAlarm(alarmModel: AlarmModel){}

    fun updateAlarm(alarmModel: AlarmModel){}

    fun deleteAlarm(alarmModel: AlarmModel){}
}