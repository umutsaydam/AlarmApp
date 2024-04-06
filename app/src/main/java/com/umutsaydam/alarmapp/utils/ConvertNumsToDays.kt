package com.umutsaydam.alarmapp.utils


object ConvertNumsToDays {
    private val days: List<String> = GetDaysByLang.getDays()

    fun convertNumsToDays(everyday: String, dayNumList: List<Int>): List<String> {
        if (dayNumList.size == 7) return listOf(everyday)

        val selectedDays: ArrayList<String> = arrayListOf()
        dayNumList.forEach { num ->
            selectedDays.add(days[num-1])
        }
        return selectedDays
    }
}