package com.umutsaydam.alarmapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.alarmapp.helpers.AlarmSchedule
import com.umutsaydam.alarmapp.helpers.Alarms
import com.umutsaydam.alarmapp.models.AlarmModel
import com.umutsaydam.alarmapp.repository.AlarmRepository
import kotlinx.coroutines.launch

class AlarmViewModel(context: Context, private val alarmRepository: AlarmRepository) :
    ViewModel() {
    private val alarmManager: Alarms = Alarms(context)
    private val alarmSchedule: AlarmSchedule = AlarmSchedule()

    fun getAlarms() = alarmRepository.getAllAlarms()

    fun addAlarm(alarmTitle: String, timeInMillis: Long, alarmRepeat: ArrayList<Int>) =
        viewModelScope.launch {
            alarmRepeat.sort()
            if (alarmRepeat.isEmpty()) alarmRepeat.addAll((1..7).map { it })
            val rescheduledTime = alarmSchedule.alarmReschedule(timeInMillis, alarmRepeat)
            val alarm = AlarmModel(0, alarmTitle, rescheduledTime, alarmRepeat, true)
            val alarmId = alarmRepository.addAlarm(alarm).toInt()
            alarm.alarmId = alarmId
            alarmManager.createAlarm(alarm)
        }

    fun updateAlarm(alarmModel: AlarmModel) = viewModelScope.launch {
        alarmRepository.addAlarm(alarmModel)
        alarmManager.updateAlarm(alarmModel)
    }

    fun getSingleAlarm(alarmId: Int) = alarmRepository.getSingleAlarm(alarmId)

    fun deleteAlarm(alarmModel: AlarmModel) = viewModelScope.launch {
        alarmRepository.deleteAlarm(alarmModel)
        alarmManager.deleteAlarm(alarmModel)
    }
}