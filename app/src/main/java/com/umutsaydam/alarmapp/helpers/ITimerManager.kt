package com.umutsaydam.alarmapp.helpers

interface ITimerManager {
    fun startTimer(hour: Int, minute: Int)
    fun pauseTimer()
    fun resumeTimer()
    fun cancelTimer()
}