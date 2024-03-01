package com.umutsaydam.alarmapp.helpers

import android.util.Log
import com.umutsaydam.alarmapp.models.AlarmModel
import java.util.Calendar

class AlarmSchedule : IAlarmSchedule {
    private val calendar: Calendar = Calendar.getInstance()

   /* override fun alarmReschedule(alarmTime: Long, alarmRepeat: List<Int>): Long {
        val today = calendar[Calendar.DAY_OF_WEEK]

        return if (alarmRepeat.contains(today) && calendar.timeInMillis < alarmTime) {
            alarmTime
        } else {
            calendar.timeInMillis = alarmTime
            Log.d("R/T", alarmRepeat.toString())
            val firstDay = alarmRepeat.find { it > today }
            Log.d("R/T", firstDay.toString())
            if (firstDay == null) {
                alarmTime + ((7 - today + alarmRepeat[0]) * 24 * 60 * 60 * 1000)
            } else {
                alarmTime + ((firstDay - today) * 24 * 60 * 60 * 1000)
            }
        }
    }*/

    override fun alarmReschedule(alarmModel: AlarmModel): Long {
        val today = calendar[Calendar.DAY_OF_WEEK]
        val currentTime = calendar[Calendar.HOUR_OF_DAY] * 60 + calendar[Calendar.MINUTE]
        val nextAlarmTime = if (alarmModel.alarmRepeat.isNotEmpty()) {
            var nextDay = alarmModel.alarmRepeat.firstOrNull { it > today }
            if (nextDay == null) nextDay = alarmModel.alarmRepeat.first()
            calculateNextAlarmTime(currentTime, nextDay, alarmModel.alarmTime)
        } else {
            calculateNextAlarmTime(currentTime, today, alarmModel.alarmTime)
        }

        return nextAlarmTime
    }

    private fun calculateNextAlarmTime(currentTime: Int, nextDay: Int, alarmTime: Long): Long {
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
        Log.d("R/T", "$daysToAdd *******")
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = alarmHour
        calendar[Calendar.MINUTE] = alarmMinute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        calendar.add(Calendar.DAY_OF_WEEK, daysToAdd)

        return calendar.timeInMillis
    }
}