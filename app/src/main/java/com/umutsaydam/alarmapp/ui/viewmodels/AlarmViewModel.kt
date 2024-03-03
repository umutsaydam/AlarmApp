package com.umutsaydam.alarmapp.ui.viewmodels

import android.content.Context
import android.util.Log
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

    fun addAlarm(
        alarmTitle: String,
        timeInMillis: Long,
        alarmRepeat: ArrayList<Int>,
        alarmVibrating: Boolean,
        alarmRingtoneUri: String?,
    ) =
        viewModelScope.launch {
            val organizedAlarmRepeat = checkAlarmRepeat(alarmRepeat) as ArrayList<Int>
            Log.d("R/T", "$alarmRepeat.toString() viewmodel")
            val alarm = AlarmModel(
                0,
                alarmTitle,
                timeInMillis,
                organizedAlarmRepeat,
                true,
                alarmVibrating,
                alarmRingtoneUri
            )
            val rescheduledTime = alarmSchedule.alarmReschedule(alarm)
            alarm.alarmTime = rescheduledTime
            val alarmId = alarmRepository.addAlarm(alarm).toInt()
            alarm.alarmId = alarmId
            alarmManager.createAlarm(alarm)
        }

    fun updateAlarm(alarmModel: AlarmModel) = viewModelScope.launch {
        alarmModel.alarmRepeat = checkAlarmRepeat(alarmModel.alarmRepeat as ArrayList<Int>)
        val rescheduledTime = alarmSchedule.alarmReschedule(alarmModel)
        alarmModel.alarmTime = rescheduledTime
        alarmRepository.addAlarm(alarmModel)
        alarmManager.updateAlarm(alarmModel)
    }

    fun getSingleAlarm(alarmId: Int) = viewModelScope.launch {
        alarmRepository.getSingleAlarm(alarmId)
    }

    fun deleteAlarm(alarmModel: AlarmModel) = viewModelScope.launch {
        alarmRepository.deleteAlarm(alarmModel)
        alarmManager.deleteAlarm(alarmModel)
    }

    fun checkAlarmRepeat(alarmRepeat: ArrayList<Int>): List<Int>{
        alarmRepeat.sort()
        if (alarmRepeat.isEmpty()) alarmRepeat.addAll((1..7).map { it })
        return alarmRepeat
    }
}