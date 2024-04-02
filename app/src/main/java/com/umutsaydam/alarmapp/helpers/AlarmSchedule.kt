package com.umutsaydam.alarmapp.helpers

import com.umutsaydam.alarmapp.models.AlarmModel
import java.util.Calendar

class AlarmSchedule : IAlarmSchedule {

    override fun alarmReschedule(alarmModel: AlarmModel): Long {
        val calendar: Calendar = Calendar.getInstance()
        val today = calendar[Calendar.DAY_OF_WEEK]
        val currentTime = calendar[Calendar.HOUR_OF_DAY] * 60 + calendar[Calendar.MINUTE]

        val targetAlarmTime =
            if (alarmModel.alarmRepeat.contains(today) && alarmModel.alarmTime > calendar.timeInMillis) {
                alarmModel.alarmTime
            } else if (alarmModel.alarmRepeat.isNotEmpty()) {
                var nextDay = alarmModel.alarmRepeat.firstOrNull { it > today }
                if (nextDay == null) nextDay = alarmModel.alarmRepeat.first()
                calculateNextAlarmTime(currentTime, nextDay, alarmModel.alarmTime)
            } else {
                calculateNextAlarmTime(currentTime, today, alarmModel.alarmTime)
            }

        return targetAlarmTime
    }

    private fun calculateNextAlarmTime(currentTime: Int, nextDay: Int, alarmTime: Long): Long {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = alarmTime
        val alarmHour = calendar[Calendar.HOUR_OF_DAY]
        val alarmMinute = calendar[Calendar.MINUTE]
        val alarmTimeMinutes = alarmHour * 60 + alarmMinute

        val todayTime = calendar[Calendar.DAY_OF_WEEK]
        val daysToAdd = if (todayTime == nextDay && alarmTimeMinutes < currentTime) {
            7
        } else {
            (nextDay + 7 - todayTime) % 7
        }
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = alarmHour
        calendar[Calendar.MINUTE] = alarmMinute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        calendar.add(Calendar.DAY_OF_WEEK, daysToAdd)

        return calendar.timeInMillis
    }
}