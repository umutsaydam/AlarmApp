package com.umutsaydam.alarmapp.helpers


interface IAlarmSchedule {

    fun alarmReschedule(alarmTime: Long, alarmRepeat: List<Int>): Long{
        return 0
    }
}