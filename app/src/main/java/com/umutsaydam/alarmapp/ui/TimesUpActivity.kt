package com.umutsaydam.alarmapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umutsaydam.alarmapp.databinding.ActivityTimesUpBinding
import com.umutsaydam.alarmapp.helpers.IRingtonePlayer
import com.umutsaydam.alarmapp.helpers.IVibrator
import com.umutsaydam.alarmapp.helpers.RingtonePlayer
import com.umutsaydam.alarmapp.helpers.Vibrator

class TimesUpActivity : AppCompatActivity(), IVibrator, IRingtonePlayer {
    private lateinit var vibrator: IVibrator
    private var _binding: ActivityTimesUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var ringtonePlayer: IRingtonePlayer
    private var alarmVibrating = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTimesUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmVibrating = intent.getBooleanExtra("alarmVibrating", false)

        val alarmRingtoneUri = intent.getStringExtra("alarmRingtoneUri")
        ringtonePlayer = RingtonePlayer(this, alarmRingtoneUri!!)
        playRingtone()

        if (alarmVibrating) {
            vibrator = Vibrator(this)
            startVibrator()
        }

        initUI()
    }

    private fun initUI() {
        binding.btnStopAlarm.setOnClickListener {
            if (alarmVibrating) {
                vibrator.stopVibrator()
            }
            stopRingtone()
        }
    }

    override fun startVibrator() {
        vibrator.startVibrator()
    }

    override fun stopVibrator() {
        vibrator.stopVibrator()
    }

    override fun playRingtone() {
        ringtonePlayer.playRingtone()
    }

    override fun stopRingtone() {
        ringtonePlayer.stopRingtone()
    }
}