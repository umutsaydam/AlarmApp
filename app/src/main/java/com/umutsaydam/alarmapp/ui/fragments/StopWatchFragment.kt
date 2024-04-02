package com.umutsaydam.alarmapp.ui.fragments

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.umutsaydam.alarmapp.databinding.FragmentStopWatchBinding
import com.umutsaydam.alarmapp.helpers.ITimerManager
import com.umutsaydam.alarmapp.services.StopWatchService
import com.umutsaydam.alarmapp.services.TimerService

class StopWatchFragment : Fragment() {
    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!
    private var stopWatchService: ITimerManager? = null
    private var isBound = false
    private var isStopWatchEnable = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStopWatchBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("StopWatchFeature", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()


        if (sharedPreferences.getBoolean("isStopWatchEnabled", false)) {
            changeTimerController(true)
            binding.tvTimer.text = TimerService.remainTimeFormatted
            togglePauseResumeButtons(View.GONE, View.VISIBLE)
        }
        stopWatchService = StopWatchService()
        initTimeControllerUI()
        initOnBackPressed()

        return binding.root
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "STOPWATCH_TICK") {
                val timeLeftFormatted = intent.getStringExtra("timeFormatted")
                binding.tvTimer.text = timeLeftFormatted

                if (!isStopWatchEnable) {
                    isStopWatchEnable = true
                    changeTimerController(true)
                }
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as StopWatchService.TimerBinder
            stopWatchService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            stopWatchService = null
            isBound = false
        }
    }

    private fun changeTimerController(isTiming: Boolean) {
        if (isTiming) {
            binding.btnStartStopWatch.visibility = View.GONE
            binding.llTimerControllerStopWatch.visibility = View.VISIBLE
        } else {
            binding.llTimerControllerStopWatch.visibility = View.GONE
            binding.btnStartStopWatch.visibility = View.VISIBLE
        }
    }

    private fun initTimeControllerUI() {
        binding.btnStartStopWatch.setOnClickListener {
            val serviceIntent = Intent(requireContext(), StopWatchService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().startForegroundService(serviceIntent)
            } else {
                requireContext().startService(serviceIntent)
            }
            changeTimerController(true)
            editor.putBoolean("isTimerEnabled", true)
            editor.apply()
            isStopWatchEnable = true
        }

        binding.btnCancelStopWatch.setOnClickListener {
            changeTimerController(false)
            binding.btnStartStopWatch.visibility = View.VISIBLE
            stopWatchService!!.cancelTimer()
            binding.tvTimer.text = "00:00:00"
            editor.putBoolean("isTimerEnabled", false)
            editor.apply()
        }

        binding.btnPauseStopWatch.setOnClickListener {
            stopWatchService!!.pauseTimer()
            togglePauseResumeButtons(visibilityPause = View.GONE, visibilityResume = View.VISIBLE)
        }

        binding.btnResumeStopWatch.setOnClickListener {
            stopWatchService!!.resumeTimer()
            togglePauseResumeButtons(visibilityPause = View.VISIBLE, visibilityResume = View.GONE)
        }
    }

    private fun togglePauseResumeButtons(visibilityPause: Int, visibilityResume: Int) {
        binding.btnPauseStopWatch.visibility = visibilityPause
        binding.btnResumeStopWatch.visibility = visibilityResume
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
        requireContext().registerReceiver(broadcastReceiver, IntentFilter("STOPWATCH_TICK"))
        Intent(requireContext(), StopWatchService::class.java).also { intent ->
            requireContext().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(broadcastReceiver)
        Intent(requireContext(), StopWatchService::class.java).also {
            requireContext().unbindService(connection)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}