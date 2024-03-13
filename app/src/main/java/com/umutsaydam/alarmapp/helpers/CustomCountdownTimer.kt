package com.umutsaydam.alarmapp.helpers

import android.os.CountDownTimer

class CustomCountdownTimer(
    private val millisInFuture: Long,
    private val countDownInterval: Long,
): ICustomCountdownTimer  {
    private val millisUntilFinished = millisInFuture
    private var timer = InternalTimer(this, millisInFuture, countDownInterval)
    private var isRunning = false
    override var onTick: ((millisUntilFinished: Long) -> Unit)? = null
    override var onFinish: (() -> Unit)? = null


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

   override fun pauseTimer() {
        timer.cancel()
        isRunning = false
    }

    override fun resumeTimer() {
        if (!isRunning && timer.millisUntilFinished > 0) {
            timer = InternalTimer(this, timer.millisUntilFinished, countDownInterval)
            startTimer()
        }
    }

    override fun startTimer() {
        timer.start()
        isRunning = true
    }

    override fun destroyTimer() {
        timer.cancel()
    }
}


