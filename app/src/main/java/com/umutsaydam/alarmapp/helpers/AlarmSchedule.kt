package com.umutsaydam.alarmapp.helpers

import java.util.Calendar

class AlarmSchedule : IAlarmSchedule {
    private val calendar: Calendar = Calendar.getInstance()

    override fun alarmReschedule(alarmTime: Long, alarmRepeat: List<Int>): Long {
        val today = calendar[Calendar.DAY_OF_WEEK]

        return if (alarmRepeat.contains(today) && calendar.timeInMillis < alarmTime) {
            alarmTime
        } else {
            calendar.timeInMillis = alarmTime
            val firstDay = alarmRepeat.find { it > today }
            if (firstDay == null) {
                alarmTime + ((7 - today + alarmRepeat[0]) * 24 * 60 * 60 * 1000)
            } else {
                alarmTime + ((firstDay - today) * 24 * 60 * 60 * 1000)
            }
        }
    }
}