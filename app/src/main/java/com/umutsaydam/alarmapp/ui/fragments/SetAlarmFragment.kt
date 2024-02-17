package com.umutsaydam.alarmapp.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umutsaydam.alarmapp.adapters.RepeatAdapter
import com.umutsaydam.alarmapp.databinding.FragmentSetAlarmBinding
import com.umutsaydam.alarmapp.db.AlarmDatabase
import com.umutsaydam.alarmapp.models.RepeatDaysItemModel
import com.umutsaydam.alarmapp.repository.AlarmRepository
import com.umutsaydam.alarmapp.ui.viewmodels.AlarmViewModel
import com.umutsaydam.alarmapp.ui.viewmodels.AlarmsViewModelFactory
import java.util.Calendar

class SetAlarmFragment : Fragment() {
    private var _binding: FragmentSetAlarmBinding? = null
    private val binding get() = _binding!!
    private var timeInMillis: Long = 0
    private lateinit var viewModel: AlarmViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSetAlarmBinding.inflate(inflater, container, false)

        initViewModel()

        binding.timePicker.setOnTimeChangedListener { _, p1, p2 ->
            val selected = Calendar.getInstance()
            selected[Calendar.HOUR_OF_DAY] = p1
            selected[Calendar.MINUTE] = p2
            selected[Calendar.SECOND] = 0

            timeInMillis = selected.timeInMillis
        }

        binding.btAlarmSave.setOnClickListener {
            val title = binding.tvClockTitle.text.toString()

            viewModel.addAlarm(title, timeInMillis, context!!).invokeOnCompletion {
                findNavController().popBackStack()
            }

        }

        val days = listOf(
            RepeatDaysItemModel("S"),
            RepeatDaysItemModel("M"),
            RepeatDaysItemModel("T"),
            RepeatDaysItemModel("W"),
            RepeatDaysItemModel("T"),
            RepeatDaysItemModel("F"),
            RepeatDaysItemModel("S"),
        )

        val adapter = RepeatAdapter(days)

        binding.rcDays.apply {
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
            setAdapter(adapter)
        }


        return binding.root
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}