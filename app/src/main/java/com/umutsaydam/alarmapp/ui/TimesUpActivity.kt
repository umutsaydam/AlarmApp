package com.umutsaydam.alarmapp.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.animation.AnimationUtils
import com.umutsaydam.alarmapp.R
import com.umutsaydam.alarmapp.databinding.ActivityTimesUpBinding
import com.umutsaydam.alarmapp.helpers.IRingtonePlayer
import com.umutsaydam.alarmapp.helpers.IVibrator
import com.umutsaydam.alarmapp.helpers.RingtonePlayer
import com.umutsaydam.alarmapp.helpers.Vibrator
import com.umutsaydam.alarmapp.models.AlarmModel

class TimesUpActivity : AppCompatActivity(), IVibrator, IRingtonePlayer {
    private lateinit var vibrator: IVibrator
    private var _binding: ActivityTimesUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var ringtonePlayer: IRingtonePlayer
    private var alarmVibrating = false
    private lateinit var alarmModel: AlarmModel
    private var alarmRingtoneUri: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTimesUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.getBundleExtra("alarmModelBundle")

        bundle?.let {
            alarmModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable("alarmModel", AlarmModel::class.java)!!
            } else {
                it.getParcelable("alarmModel")!!
            }
        }
        alarmVibrating = alarmModel.alarmVibrating

        alarmRingtoneUri = alarmModel.alarmRingtoneUri
        ringtonePlayer = RingtonePlayer(this, alarmRingtoneUri!!)
        playRingtone()

        if (alarmVibrating) {
            vibrator = Vibrator(this)
            startVibrator()
        }

        initUI()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUI() {
        binding.tvAlarmTime.text = alarmModel.alarmHourMinuteFormat
        binding.tvAlarmTitle.text = alarmModel.alarmTitle


        binding.fbStopAlarm.setOnTouchListener { view, motionEvent ->
            val data = ClipData.newPlainText("", "word")
            val showBuilder = View.DragShadowBuilder(binding.fbStopAlarm)
            binding.fbStopAlarm.startDragAndDrop(data, showBuilder, binding.fbStopAlarm, 0)
            true
        }

        binding.fbStopAlarm.setOnDragListener { p0, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    Log.d("R/T", "Started")
                    binding.fbStopAlarm.visibility = View.GONE
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    Log.d("R/T", "Entered")
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    Log.d("R/T", "EXITED")
                    // stop music etc.
                    if (alarmVibrating) {
                        vibrator.stopVibrator()
                    }
                    stopRingtone()
                    finish()
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    Log.d("R/T", "Stopped")
                    binding.fbStopAlarm.visibility = View.VISIBLE
                }

            }
            true
        }

        startAlphaAnim()
    }

    private fun startAlphaAnim() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.alpha_animation)
        binding.ivs.startAnimation(animation)
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