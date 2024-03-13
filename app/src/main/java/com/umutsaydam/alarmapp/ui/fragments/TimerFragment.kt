package com.umutsaydam.alarmapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.umutsaydam.alarmapp.databinding.FragmentTimerBinding
import com.umutsaydam.alarmapp.helpers.CustomCountdownTimer
import java.text.DecimalFormat
import kotlin.math.roundToInt

class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private lateinit var customCountdownTimer: CustomCountdownTimer
    private var currHour = 0
    private var currMinute = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)

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
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    customCountdownTimer.destroyTimer()
                    findNavController().navigateUp()
                }
            })

        return binding.root
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

                timerFormat(secondsLeft, binding.tvTimer)
            }
        }

        customCountdownTimer.onFinish = {
            timerFormat(0, binding.tvTimer)
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
            binding.btnPause.visibility = View.GONE
            binding.btnResume.visibility = View.VISIBLE
            customCountdownTimer.pauseTimer()
        }

        binding.btnResume.setOnClickListener {
            binding.btnPause.visibility = View.VISIBLE
            binding.btnResume.visibility = View.GONE
            customCountdownTimer.resumeTimer()
        }
    }

    private fun timerFormat(secondsLeft: Int, tvTimer: TextView) {
        binding.pbTimer.progress = secondsLeft

        val decimalFormat = DecimalFormat("00")
        val hour = secondsLeft / 3600
        val min = (secondsLeft % 3600) / 60
        val seconds = secondsLeft % 60

        val timerFormat3 =
            decimalFormat.format(hour) + ":" + decimalFormat.format(min) + ":" + decimalFormat.format(
                seconds
            )

        binding.tvTimer.text = timerFormat3
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}