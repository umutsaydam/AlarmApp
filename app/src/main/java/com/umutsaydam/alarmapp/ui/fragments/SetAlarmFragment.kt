package com.umutsaydam.alarmapp.ui.fragments

import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
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
import com.umutsaydam.alarmapp.helpers.IRingtoneSelector
import com.umutsaydam.alarmapp.helpers.RingtoneSelector
import com.umutsaydam.alarmapp.models.AlarmModel
import com.umutsaydam.alarmapp.models.RepeatDaysItemModel
import com.umutsaydam.alarmapp.repository.AlarmRepository
import com.umutsaydam.alarmapp.ui.viewmodels.AlarmViewModel
import com.umutsaydam.alarmapp.ui.viewmodels.AlarmsViewModelFactory
import com.umutsaydam.alarmapp.utils.Constants.EDIT_MODE_OFF
import com.umutsaydam.alarmapp.utils.Constants.EDIT_MODE_ON
import com.umutsaydam.alarmapp.utils.SetCheckedListener
import java.util.Calendar
import java.util.Date

class SetAlarmFragment : Fragment(), IRingtoneSelector, SetCheckedListener {
    private var _binding: FragmentSetAlarmBinding? = null
    private val binding get() = _binding!!
    private var timeInMillis: Long = 0
    private lateinit var viewModel: AlarmViewModel
    private lateinit var selected: Calendar
    private val dayList = ArrayList<Int>()
    private var alarmVibrate = false
    private var alarmRingtoneUri: String? = null
    private lateinit var ringtoneSelector: IRingtoneSelector
    private var editState = EDIT_MODE_OFF
    private var editAlarmModel: AlarmModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSetAlarmBinding.inflate(inflater, container, false)

        val days = listOf(
            RepeatDaysItemModel("S"),
            RepeatDaysItemModel("M"),
            RepeatDaysItemModel("T"),
            RepeatDaysItemModel("W"),
            RepeatDaysItemModel("T"),
            RepeatDaysItemModel("F"),
            RepeatDaysItemModel("S"),
        )

        if (arguments != null) {
            editAlarmModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments!!.getParcelable("alarmModel", AlarmModel::class.java)
            } else {
                arguments!!.getParcelable("alarmModel")!!
            }
            if (editAlarmModel != null) {
                editState = EDIT_MODE_ON
                initEditAlarmUI(editAlarmModel!!)
                dayList.addAll(editAlarmModel!!.alarmRepeat)
            }
        }

        binding.rcDays.apply {
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
            adapter = if (editState) RepeatAdapter(
                days,
                editAlarmModel!!.alarmRepeat,
                this@SetAlarmFragment
            ) else RepeatAdapter(days, null, this@SetAlarmFragment)
        }

        ringtoneSelector = RingtoneSelector(this)
        initViewModel()
        initCalendar()
        initUI()

        return binding.root
    }

    private fun initEditAlarmUI(editAlarmModel: AlarmModel) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = editAlarmModel.alarmTime

        binding.tvClockTitle.setText(editAlarmModel.alarmTitle)
        binding.switchVibrating.isChecked = editAlarmModel.alarmVibrating
        alarmVibrate = editAlarmModel.alarmVibrating
        val ringtoneManager =
            RingtoneManager.getRingtone(context, Uri.parse(editAlarmModel.alarmRingtoneUri))
        binding.tvClockRingtoneDefault.text = ringtoneManager.getTitle(context)
    }

    private fun initUI() {
        binding.btAlarmSave.setOnClickListener {
            val title = binding.tvClockTitle.text.toString()

            alarmRingtoneUri = ringtoneSelector.currentRingtone()

            if (editState) {
                editAlarmModel!!.apply {
                    this.alarmTime = timeInMillis
                    this.alarmRepeat = dayList
                    this.alarmVibrating = alarmVibrate
                    this.alarmRingtoneUri = alarmRingtoneUri.toString()
                    this.alarmTitle = title
                }
                viewModel.updateAlarm(editAlarmModel!!)
                    .invokeOnCompletion {
                        findNavController().popBackStack()
                    }
            } else {
                viewModel.addAlarm(title, timeInMillis, dayList, alarmVibrate, alarmRingtoneUri)
                    .invokeOnCompletion {
                        findNavController().popBackStack()
                    }
            }
        }

        binding.switchVibrating.setOnCheckedChangeListener { p0, p1 ->
            alarmVibrate = p1
        }

        binding.linearLayoutVibrating.setOnClickListener {
            alarmVibrate = !alarmVibrate
            binding.switchVibrating.isChecked = alarmVibrate
        }

        binding.linearLayoutRingtone.setOnClickListener {
            ringtoneSelector.selectRingtone()
        }
    }

    private fun initCalendar() {
        selected = Calendar.getInstance()
        binding.timePicker.setIs24HourView(true)

        if (editState) {
           val date = Date(editAlarmModel!!.alarmTime)
            selected.time = date
            timeInMillis = editAlarmModel!!.alarmTime
            binding.timePicker.hour = selected[Calendar.HOUR_OF_DAY]
            binding.timePicker.minute = selected[Calendar.MINUTE]
        } else {
            timeInMillis = selected.timeInMillis
        }
        binding.timePicker.setOnTimeChangedListener { _, p1, p2 ->
            selected[Calendar.HOUR_OF_DAY] = p1
            selected[Calendar.MINUTE] = p2
            selected[Calendar.SECOND] = 0
            timeInMillis = selected.timeInMillis
        }
    }

    override fun setOnCheckedListener(indexOfDay: Int, isChecked: Boolean) {
        if (isChecked) {
            dayList.add(indexOfDay)
        } else {
            dayList.remove(indexOfDay)
        }
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

    override fun selectRingtone() {
        ringtoneSelector.selectRingtone()
    }

    override fun selectDefaultRingtone() {
        ringtoneSelector.selectDefaultRingtone()
    }

    override fun currentRingtone(): String {
        return ringtoneSelector.currentRingtone()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}