package com.umutsaydam.alarmapp.helpers

import com.umutsaydam.alarmapp.models.AlarmModel


interface IAlarmSchedule {

    fun alarmReschedule(alarmModel: AlarmModel): Long{
        return 0
    }
}