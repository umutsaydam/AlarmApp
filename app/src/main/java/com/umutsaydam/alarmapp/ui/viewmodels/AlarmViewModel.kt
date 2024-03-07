package com.umutsaydam.alarmapp.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.alarmapp.helpers.AlarmNotification
import com.umutsaydam.alarmapp.helpers.AlarmSchedule
import com.umutsaydam.alarmapp.helpers.Alarms
import com.umutsaydam.alarmapp.helpers.IAlarmNotification
import com.umutsaydam.alarmapp.models.AlarmModel
import com.umutsaydam.alarmapp.repository.AlarmRepository
import kotlinx.coroutines.launch

class AlarmViewModel(context: Context, private val alarmRepository: AlarmRepository) :
    ViewModel(), IAlarmNotification {
    private val alarmManager: Alarms = Alarms(context)
    private val alarmSchedule: AlarmSchedule = AlarmSchedule()
    private var alarmNotification: IAlarmNotification

    companion object {
        private var countOfEnabledAlarms = 0

        fun increaseCountOfEnabledAlarms() {
            countOfEnabledAlarms++
        }

        fun decreaseCountOfEnabledAlarms(){
            countOfEnabledAlarms--
        }
    }
    init {
        alarmNotification = AlarmNotification(context.applicationContext)
        updateCountOfEnabledAlarms()
    }

    fun getAlarms() = alarmRepository.getAllAlarms()

    fun addAlarm(
        alarmTitle: String,
        timeInMillis: Long,
        alarmRepeat: ArrayList<Int>,
        alarmVibrating: Boolean,
        alarmRingtoneUri: String?,
        alarmHourMinuteFormat: String,
    ) =
        viewModelScope.launch {
            val organizedAlarmRepeat = checkAlarmRepeat(alarmRepeat) as ArrayList<Int>
            val checkedAlarmTitle = checkAlarmTitle(alarmTitle)
            Log.d("R/T", "$alarmRepeat.toString() viewmodel")
            val alarm = AlarmModel(
                0,
                checkedAlarmTitle,
                timeInMillis,
                organizedAlarmRepeat,
                true,
                alarmVibrating,
                alarmRingtoneUri,
                alarmHourMinuteFormat
            )
            Log.d("R/T", "$countOfEnabledAlarms 51")
            if (countOfEnabledAlarms == 1) {
                startForegroundService()
                Log.d("R/T", "service was started")
            }
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
        alarmModel.alarmTitle = checkAlarmTitle(alarmModel.alarmTitle)
        alarmRepository.addAlarm(alarmModel)
        alarmManager.updateAlarm(alarmModel)
        updateCountOfEnabledAlarms()
    }

    fun getSingleAlarm(alarmId: Int) = viewModelScope.launch {
        alarmRepository.getSingleAlarm(alarmId)
    }

    fun deleteAlarm(alarmModel: AlarmModel) = viewModelScope.launch {
        Log.d("R/T", "$countOfEnabledAlarms")
        if (countOfEnabledAlarms <= 0) {
            stopService()
            Log.d("R/T", "service was stopped")
        }
        alarmRepository.deleteAlarm(alarmModel)
        alarmManager.deleteAlarm(alarmModel)
    }

    private fun checkAlarmTitle(alarmTitle: String?): String {
        return alarmTitle?.trim() ?: "None"
    }

    private fun checkAlarmRepeat(alarmRepeat: ArrayList<Int>): List<Int> {
        alarmRepeat.sort()
        if (alarmRepeat.isEmpty()) alarmRepeat.addAll((1..7).map { it })
        return alarmRepeat
    }

    fun updateCountOfEnabledAlarms() = viewModelScope.launch {
        countOfEnabledAlarms = alarmRepository.countOfEnabledAlarm()
        Log.d("R/T", "$countOfEnabledAlarms updated")
    }

    override fun startForegroundService() {
        alarmNotification.startForegroundService()
    }

    override fun stopService() {
        alarmNotification.stopService()
    }


}