package com.umutsaydam.alarmapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.umutsaydam.alarmapp.databinding.FragmentTimerBinding
import com.umutsaydam.alarmapp.helpers.CustomCountdownTimer
import java.text.DecimalFormat
import kotlin.math.roundToInt

class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private lateinit var customCountdownTimer: CustomCountdownTimer

    private val countdownTime = 60 // 60 second, 1 min
    private val clockTime = (countdownTime * 1000).toLong()
    private val progressTime = (clockTime / 1000).toFloat()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)

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
        }

        binding.btnStart.setOnClickListener {
            customCountdownTimer.startTimer()
        }

        binding.btnResume.setOnClickListener {
            customCountdownTimer.pauseTimer()
        }

        binding.btnCancel.setOnClickListener {
            binding.pbTimer.progress = progressTime.toInt()
            customCountdownTimer.restartTimer()
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

    private fun timerFormat(secondsLeft: Int, tvTimer: TextView) {
        binding.pbTimer.progress = secondsLeft

        val decimalFormat = DecimalFormat("00")
        val hour = secondsLeft / 3600
        val min = (secondsLeft % 3600) / 60
        val seconds = secondsLeft % 60

        val timeFormat1 = decimalFormat.format(secondsLeft)
        val timeFormat2 = decimalFormat.format(min) + ":" + decimalFormat.format(seconds)
        val timerFormat3 =
            decimalFormat.format(hour) + ":" + decimalFormat.format(min) + ":" + decimalFormat.format(
                seconds
            )

        binding.tvTimer.text = timeFormat1 + "\n" + timeFormat2 + "\n" + timerFormat3
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}