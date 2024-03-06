package com.umutsaydam.alarmapp.utils

object ConverterHoursMinutesFormat {
    fun convertToHourAndMinuteFormat(hour: Int, minute: Int): String {
        return "$hour:$minute"
    }

    fun splitHourAndMinute(hourAndMinuteFormat: String): List<Int> {
        return hourAndMinuteFormat.split(":").map { it.toInt() }
    }
}