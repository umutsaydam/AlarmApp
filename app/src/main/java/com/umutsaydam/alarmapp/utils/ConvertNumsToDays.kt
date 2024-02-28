package com.umutsaydam.alarmapp.utils

object ConvertNumsToDays {
    private val days: List<String> = listOf("S", "M", "T", "W", "Th", "F", "St")

    fun convertNumsToDays(dayNumList: List<Int>): List<String> {
        if (dayNumList.size == 7) return listOf("Everyday")

        val selectedDays: ArrayList<String> = arrayListOf()
        dayNumList.forEach { num ->
            selectedDays.add(days[num])
        }
        return selectedDays
    }
}