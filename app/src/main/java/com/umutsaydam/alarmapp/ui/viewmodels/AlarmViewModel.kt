package com.umutsaydam.alarmapp.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.alarmapp.helpers.Alarms
import com.umutsaydam.alarmapp.models.AlarmModel
import com.umutsaydam.alarmapp.repository.AlarmRepository
import kotlinx.coroutines.launch

class AlarmViewModel(context: Context, private val alarmRepository: AlarmRepository) :
    ViewModel() {
    private var alarmManager: Alarms = Alarms(context)

    fun getAlarms() = alarmRepository.getAllAlarms()

    fun addAlarm(alarmTitle: String, timeInMillis: Long) = viewModelScope.launch {
        val alarm = AlarmModel(0, alarmTitle, timeInMillis, true)
        val alarmID = alarmRepository.addAlarm(alarm).toInt()
        Log.d("R/T", "$alarmID**")

        alarmManager.createAlarm(alarmID, timeInMillis)
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