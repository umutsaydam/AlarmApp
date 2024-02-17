package com.umutsaydam.alarmapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmModel(
    @PrimaryKey(autoGenerate = true)
    val alarmId: Int,
    val alarmTitle: String = "",
    val alarmTime: Long = 0,
    var alarmEnabled: Boolean = true
)