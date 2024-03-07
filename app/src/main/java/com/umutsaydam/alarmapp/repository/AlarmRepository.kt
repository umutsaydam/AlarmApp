package com.umutsaydam.alarmapp.repository

import com.umutsaydam.alarmapp.db.AlarmDatabase
import com.umutsaydam.alarmapp.models.AlarmModel

class AlarmRepository(val db: AlarmDatabase) {

    suspend fun addAlarm(alarm: AlarmModel) = db.getAlarmDao().upsert(alarm)

    fun getAllAlarms() = db.getAlarmDao().getAllAlarms()

    suspend fun getSingleAlarm(alarmId: Int) = db.getAlarmDao().getSingleAlarm(alarmId)

    suspend fun deleteAlarm(alarmModel: AlarmModel) = db.getAlarmDao().deleteAlarm(alarmModel)

    suspend fun countOfEnabledAlarm() = db.getAlarmDao().countOfEnabledAlarms()
}