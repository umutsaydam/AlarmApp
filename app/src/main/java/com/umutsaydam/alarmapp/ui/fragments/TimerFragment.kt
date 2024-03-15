package com.umutsaydam.alarmapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.umutsaydam.alarmapp.databinding.FragmentTimerBinding
import com.umutsaydam.alarmapp.helpers.CustomCountdownTimer
import com.umutsaydam.alarmapp.helpers.IAlarmNotification
import com.umutsaydam.alarmapp.helpers.ICustomCountdownTimer
import com.umutsaydam.alarmapp.helpers.ITimerManager
import com.umutsaydam.alarmapp.services.AlarmNotificationService
import java.text.DecimalFormat
import kotlin.math.roundToInt

class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private lateinit var customCountdownTimer: ICustomCountdownTimer
    private lateinit var timerManager: ITimerManager
    private var currHour = 0
    private var currMinute = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)

        initTimePickerUI()
        initOnBackPressed()
        return binding.root
    }

    private fun initOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    customCountdownTimer.destroyTimer()
                    findNavController().navigateUp()
                }
            })
    }

    private fun initTimePickerUI() {
        binding.timePickerTimer.setIs24HourView(true)
        binding.timePickerTimer.hour = 0
        binding.timePickerTimer.minute = 0
        binding.timePickerTimer.setOnTimeChangedListener { _, hour, minute ->
            currHour = hour
            currMinute = minute
            binding.btnStart.isEnabled = hour > 0 || minute > 0
        }
        binding.btnStart.setOnClickListener {
            binding.timePickerTimer.visibility = View.GONE
            binding.tvTimer.visibility = View.VISIBLE
            initCustomCountdownTimer()
        }
    }

    private fun initCustomCountdownTimer() {
        binding.btnStart.visibility = View.GONE
        binding.llTimerController.visibility = View.VISIBLE

        val countdownTime = (currHour * 60 * 60) + (currMinute * 60)
        val clockTime = (countdownTime * 1000).toLong()
        val progressTime = (clockTime / 1000).toFloat()
        var secondsLeft = 0
        customCountdownTimer = CustomCountdownTimer(clockTime, 1000)

        customCountdownTimer.onTick = { millisUntilFinished ->
            val second = (millisUntilFinished / 1000.0f).roundToInt()
            if (second != secondsLeft) {
                secondsLeft = second

                timerFormat(secondsLeft, binding.tvTimer, binding.pbTimer)
            }
        }

        customCountdownTimer.onFinish = {
            timerFormat(0, binding.tvTimer, binding.pbTimer)
        }
        binding.pbTimer.max = progressTime.toInt()
        binding.pbTimer.progress = progressTime.toInt()

        customCountdownTimer.startTimer()

        binding.btnCancel.setOnClickListener {
            customCountdownTimer.destroyTimer()
            binding.timePickerTimer.visibility = View.VISIBLE
            binding.tvTimer.visibility = View.GONE
            binding.llTimerController.visibility = View.GONE
            binding.btnStart.visibility = View.VISIBLE
        }

        binding.btnPause.setOnClickListener {
            togglePauseResumeButtons(visibilityPause = View.GONE, visibilityResume = View.VISIBLE)
            customCountdownTimer.pauseTimer()
        }

        binding.btnResume.setOnClickListener {
            togglePauseResumeButtons(visibilityPause = View.VISIBLE, visibilityResume = View.GONE)
            customCountdownTimer.resumeTimer()
        }
    }

    private fun togglePauseResumeButtons(visibilityPause: Int, visibilityResume: Int) {
        binding.btnPause.visibility = visibilityPause
        binding.btnResume.visibility = visibilityResume
    }

    private fun timerFormat(secondsLeft: Int, tvTimer: TextView, pbTimer: ProgressBar) {
        pbTimer.progress = secondsLeft
        val decimalFormat = DecimalFormat("00")
        val hour = secondsLeft / 3600
        val min = (secondsLeft % 3600) / 60
        val seconds = secondsLeft % 60
        val formattedTime =
            "${decimalFormat.format(hour)}:${decimalFormat.format(min)}:${
                decimalFormat.format(
                    seconds
                )
            }"
        tvTimer.text = formattedTime
    }

    override fun onStart() {
        super.onStart()
        Log.d("R/D", "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d("R/D", "onPause")
    }

    override fun onResume() {
        super.onResume()
        val serviceIntent = Intent(context, AlarmNotificationService::class.java)
        context!!.stopService(serviceIntent)
        Log.d("R/D", "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("R/D", "onStop")
        val serviceIntent = Intent(context, AlarmNotificationService::class.java)
        ContextCompat.startForegroundService(context!!, serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d("R/D", "onDestroy")
    }

}