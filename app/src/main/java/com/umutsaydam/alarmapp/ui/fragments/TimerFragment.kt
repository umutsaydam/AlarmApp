package com.umutsaydam.alarmapp.ui.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.umutsaydam.alarmapp.databinding.FragmentTimerBinding
import com.umutsaydam.alarmapp.services.TimerService

class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private var currHour = 0
    private var currMinute = 0
    private var isTimerEnable = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)

        initTimePickerUI()
        initTimeControllerUI()
        initOnBackPressed()
        return binding.root
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "TIMER_TICK") {
                val timeLeftFormatted = intent.getStringExtra("timeLeftFormatted")
                val timeLeft = intent.getIntExtra("timeLeft", 0)
                Log.d("R/T", "Time left: $timeLeftFormatted seconds *************************")
                binding.tvTimer.text = timeLeftFormatted
                updateProgressBar(timeLeft)

                if (!isTimerEnable){
                    isTimerEnable = true
                    changeTimerController(true)
                }
                if (timeLeft == 0) {
                    changeTimerController(false)
                    isTimerEnable = false
                }
            }
        }
    }

    private fun changeTimerController(isTiming: Boolean) {
        if (isTiming) {
            binding.btnStart.visibility = View.GONE
            binding.timePickerTimer.visibility = View.GONE
            binding.llTimerController.visibility = View.VISIBLE
            binding.tvTimer.visibility = View.VISIBLE
        } else {
            binding.timePickerTimer.visibility = View.VISIBLE
            binding.llTimerController.visibility = View.GONE
            binding.tvTimer.visibility = View.GONE
            binding.btnStart.visibility = View.VISIBLE
        }
    }

    private fun initTimeControllerUI() {
        binding.btnStart.setOnClickListener {
            val serviceIntent = Intent(requireContext(), TimerService::class.java)
            serviceIntent.putExtra("hour", currHour)
            serviceIntent.putExtra("minute", currMinute)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().startForegroundService(serviceIntent)
            } else {
                requireContext().startService(serviceIntent)
            }
            initCustomCountdownTimer()
            changeTimerController(true)
            isTimerEnable = true
        }

        binding.btnCancel.setOnClickListener {
            binding.timePickerTimer.visibility = View.VISIBLE
            binding.tvTimer.visibility = View.GONE
            binding.llTimerController.visibility = View.GONE
            binding.btnStart.visibility = View.VISIBLE
        }

        binding.btnPause.setOnClickListener {
            togglePauseResumeButtons(visibilityPause = View.GONE, visibilityResume = View.VISIBLE)
        }

        binding.btnResume.setOnClickListener {
            togglePauseResumeButtons(visibilityPause = View.VISIBLE, visibilityResume = View.GONE)
        }
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
    }

    private fun initCustomCountdownTimer() {
        val countdownTime = (currHour * 60 * 60) + (currMinute * 60)
        val clockTime = (countdownTime * 1000).toLong()
        val progressTime = (clockTime / 1000).toFloat()
        binding.pbTimer.max = progressTime.toInt()
        binding.pbTimer.progress = progressTime.toInt()
    }

    private fun updateProgressBar(seconds: Int) {
        val clockTime = (seconds * 1000).toLong()
        val progressTime = (clockTime / 1000).toFloat()
        binding.pbTimer.progress = progressTime.toInt()
    }

    private fun togglePauseResumeButtons(visibilityPause: Int, visibilityResume: Int) {
        binding.btnPause.visibility = visibilityPause
        binding.btnResume.visibility = visibilityResume
    }

    private fun initOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
    }

    override fun onStart() {
        super.onStart()
        // BroadcastReceiver'ı kaydedin
        requireContext().registerReceiver(broadcastReceiver, IntentFilter("TIMER_TICK"))
        Log.d("R/T", "Broadcast basladi")
    }

    override fun onStop() {
        super.onStop()
        // BroadcastReceiver'ı kaldırın
        requireContext().unregisterReceiver(broadcastReceiver)
        Log.d("R/T", "Broadcast bitti")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d("R/D", "onDestroy")
    }

}