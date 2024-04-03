package com.umutsaydam.alarmapp.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umutsaydam.alarmapp.R
import com.umutsaydam.alarmapp.adapters.AlarmAdapter
import com.umutsaydam.alarmapp.databinding.FragmentAlarmBinding
import com.umutsaydam.alarmapp.db.AlarmDatabase
import com.umutsaydam.alarmapp.helpers.AlarmNotification
import com.umutsaydam.alarmapp.models.AlarmModel
import com.umutsaydam.alarmapp.repository.AlarmRepository
import com.umutsaydam.alarmapp.ui.viewmodels.AlarmViewModel
import com.umutsaydam.alarmapp.ui.viewmodels.AlarmsViewModelFactory
import com.umutsaydam.alarmapp.utils.SetCheckedListener
import com.umutsaydam.alarmapp.utils.SetClickListener

class AlarmFragment : Fragment(), SetCheckedListener, SetClickListener {
    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AlarmViewModel
    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.getAlarms().observe(viewLifecycleOwner) {
            alarmAdapter.differ.submitList(it)
        }
        initUI()
    }
    override fun setOnCheckedListener(position: Int) {
        val alarmModel = alarmAdapter.differ.currentList[position]
        alarmModel.alarmEnabled = !alarmModel.alarmEnabled
        if (alarmModel.alarmEnabled)
            AlarmNotification.increaseCountOfEnabledAlarms()
        else
            AlarmNotification.decreaseCountOfEnabledAlarms()
        viewModel.updateAlarm(alarmModel)
    }

    override fun setOnLongClickListener(alarmModel: AlarmModel) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Delete selected alarm")
        dialog.setMessage("Alarm will delete.")
        dialog.setNegativeButton("No") { _, _ ->
        }
        dialog.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteAlarm(alarmModel)
        }
        dialog.setCancelable(true)
        dialog.show()
    }

    override fun setOnClickListener(alarmModel: AlarmModel) {
        val bundle = Bundle()
        bundle.putParcelable("alarmModel", alarmModel)
        findNavController().navigate(R.id.action_alarmFragment_to_setAlarmFragment, bundle)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            AlarmsViewModelFactory(
                requireContext(),
                AlarmRepository(AlarmDatabase(requireContext()))
            )
        )[AlarmViewModel::class.java]
    }

    private fun initUI() {
        binding.fbSetAlarm.setOnClickListener {
            findNavController().navigate(R.id.action_alarmFragment_to_setAlarmFragment)
        }

        alarmAdapter = AlarmAdapter(this@AlarmFragment, this@AlarmFragment)
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