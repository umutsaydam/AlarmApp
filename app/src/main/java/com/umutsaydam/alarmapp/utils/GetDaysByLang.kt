package com.umutsaydam.alarmapp.utils

import android.util.Log
import com.umutsaydam.alarmapp.models.RepeatDaysItemModel
import java.util.Locale

object GetDaysByLang {
    fun getDaysRepeatDays(): List<RepeatDaysItemModel> {
        Log.d("R/T", Locale.getDefault().language)
        return when (Locale.getDefault().language) {
            "tr" -> listOf(
                RepeatDaysItemModel("P"),
                RepeatDaysItemModel("Pzt"),
                RepeatDaysItemModel("S"),
                RepeatDaysItemModel("Ç"),
                RepeatDaysItemModel("P"),
                RepeatDaysItemModel("C"),
                RepeatDaysItemModel("Cmt"),
            )

            else -> listOf(
                RepeatDaysItemModel("S"),
                RepeatDaysItemModel("M"),
                RepeatDaysItemModel("T"),
                RepeatDaysItemModel("W"),
                RepeatDaysItemModel("T"),
                RepeatDaysItemModel("F"),
                RepeatDaysItemModel("S"),
            )
        }
    }

    fun getDays(): List<String> {
        Log.d("R/T", Locale.getDefault().language)
        return when (Locale.getDefault().language) {
            "tr" -> listOf(
                "P",
                "Pzt",
                "S",
                "Ç",
                "P",
                "C",
                "Cmt",
            )
            else -> listOf(
                "S",
                "M",
                "T",
                "W",
                "T",
                "F",
                "S",
            )
        }
    }
}