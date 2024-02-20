package com.umutsaydam.alarmapp.utils

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromString(value: String): List<Int> {
        return value.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun fromList(list: List<Int>): String {
        return list.joinToString(separator = ",")
    }
}