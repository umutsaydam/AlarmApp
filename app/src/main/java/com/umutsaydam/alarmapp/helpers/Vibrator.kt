package com.umutsaydam.alarmapp.helpers

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class Vibrator(context: Context) : IVibrator {
    private var isVibrating: Boolean = false
    private val vibratorManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    override fun startVibrator() {
        if (!isVibrating) {
            isVibrating = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibrationEffect = VibrationEffect.createWaveform(
                    longArrayOf(0, 400, 1000, 400, 1000, 400, 1000),
                    intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE),
                    0
                )
                (vibratorManager as VibratorManager).vibrate(
                    CombinedVibration.createParallel(
                        vibrationEffect
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                (vibratorManager as Vibrator).vibrate(
                    longArrayOf(
                        0,
                        400,
                        1000,
                        400,
                        1000,
                        400,
                        1000
                    ), 0,
                )
            }
        }
    }

    override fun stopVibrator() {
        if (isVibrating) {
            isVibrating = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                (vibratorManager as VibratorManager).cancel()
            } else {
                (vibratorManager as Vibrator).cancel()
            }
        }
    }
}