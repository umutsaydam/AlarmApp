package com.umutsaydam.alarmapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "alarms")
@TypeConverters
data class AlarmModel(
    @PrimaryKey(autoGenerate = true)
    var alarmId: Int,
    val alarmTitle: String = "",
    var alarmTime: Long = 0,
    var alarmRepeat: List<Int> = listOf(),
    var alarmEnabled: Boolean = true
)