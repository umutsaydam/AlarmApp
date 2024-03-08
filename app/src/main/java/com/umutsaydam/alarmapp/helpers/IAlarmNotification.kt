package com.umutsaydam.alarmapp.helpers

interface IAlarmNotification {
    fun startForegroundService()
    fun stopService()
    fun checkAlarmNotificationState()
}