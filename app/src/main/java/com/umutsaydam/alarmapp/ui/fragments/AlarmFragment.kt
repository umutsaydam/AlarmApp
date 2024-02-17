package com.umutsaydam.alarmapp.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umutsaydam.alarmapp.AlarmReceiver
import com.umutsaydam.alarmapp.R
import com.umutsaydam.alarmapp.adapters.AlarmAdapter
import com.umutsaydam.alarmapp.databinding.FragmentAlarmBinding
import com.umutsaydam.alarmapp.db.AlarmDatabase
import com.umutsaydam.alarmapp.models.AlarmModel
import com.umutsaydam.alarmapp.repository.AlarmRepository
import com.umutsaydam.alarmapp.ui.viewmodels.AlarmViewModel
import com.umutsaydam.alarmapp.ui.viewmodels.AlarmsViewModelFactory
import com.umutsaydam.alarmapp.utils.SetCheckedListener

class AlarmFragment : Fragment(), SetCheckedListener {
    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AlarmViewModel
    private lateinit var alarmAdapter: AlarmAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)

        initViewModel()
        viewModel.getAlarms().observe(viewLifecycleOwner) {
            alarmAdapter.differ.submitList(it)
        }
        initUI()

        return binding.root
    }

    override fun setOnCheckedListener(alarmModel: AlarmModel) {
        alarmModel.alarmEnabled = !alarmModel.alarmEnabled
        context?.let { viewModel.updateAlarm(alarmModel, it) }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            AlarmsViewModelFactory(AlarmRepository(AlarmDatabase(activity!!.applicationContext)))
        )[AlarmViewModel::class.java]
    }

    private fun initUI() {
        binding.fbSetAlarm.setOnClickListener {
            findNavController().navigate(R.id.action_alarmFragment_to_setAlarmFragment)
        }

        alarmAdapter = AlarmAdapter(this@AlarmFragment)
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvAlarm.apply {
            layoutManager = linearLayoutManager
            adapter = alarmAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}