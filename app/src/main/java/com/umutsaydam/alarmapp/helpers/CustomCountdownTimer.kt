package com.umutsaydam.alarmapp.helpers

import android.os.CountDownTimer

class CustomCountdownTimer(
    private val millisInFuture: Long,
    private val countDownInterval: Long,
) {
    private val millisUntilFinished = millisInFuture
    private var timer = InternalTimer(this, millisInFuture, countDownInterval)
    private val isRunning = false
    var onTick: ((millisUntilFinished: Long) -> Unit)? = null
    var onFinish: (() -> Unit)? = null


    private class InternalTimer(
        private val parent: CustomCountdownTimer,
        millisInFuture: Long,
        countDownInterval: Long,
    ) : CountDownTimer(millisInFuture, countDownInterval) {
        var millisUntilFinished = parent.millisUntilFinished
        override fun onTick(millisUntilFinished: Long) {
            this.millisUntilFinished = millisUntilFinished
            parent.onTick?.invoke(millisUntilFinished)
        }

        override fun onFinish() {
            millisUntilFinished = 0
            parent.onFinish?.invoke()
        }
    }

    fun pauseTimer() {

    }

    fun resumeTimer() {

    }

    fun startTimer() {

    }

    fun restartTimer() {

    }

    fun destroyTimer() {

    }
}


