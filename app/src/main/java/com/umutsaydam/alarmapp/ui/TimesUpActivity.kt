package com.umutsaydam.alarmapp.ui

import android.media.Ringtone
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umutsaydam.alarmapp.R
import com.umutsaydam.alarmapp.databinding.ActivityTimesUpBinding

class TimesUpActivity : AppCompatActivity() {
    private lateinit var ringtone: Ringtone
    private var _binding: ActivityTimesUpBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTimesUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }

        ringtone = RingtoneManager.getRingtone(this, alarmUri)
        ringtone.play()

        binding.btnStopAlarm.setOnClickListener {
            stopAlarm()
        }
    }

    private fun stopAlarm() {
        if (ringtone.isPlaying)
            ringtone.stop()
    }
}