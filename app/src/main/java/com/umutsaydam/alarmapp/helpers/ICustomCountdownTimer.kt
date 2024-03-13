package com.umutsaydam.alarmapp.helpers

interface ICustomCountdownTimer {
    var onTick: ((millisUntilFinished: Long) -> Unit)?
    var onFinish: (() -> Unit)?
    fun pauseTimer()
    fun resumeTimer()
    fun startTimer()
    fun destroyTimer()
}