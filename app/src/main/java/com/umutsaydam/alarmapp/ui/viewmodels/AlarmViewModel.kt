package com.umutsaydam.alarmapp.ui.viewmodels

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.alarmapp.AlarmReceiver
import com.umutsaydam.alarmapp.models.AlarmModel
import com.umutsaydam.alarmapp.repository.AlarmRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmViewModel(private val alarmRepository: AlarmRepository) : ViewModel() {

    fun getAlarms() = alarmRepository.getAllAlarms()

    fun addAlarm(alarmTitle: String, timeInMillis: Long, context: Context) = viewModelScope.launch {
        var alarmID: Long = 0;
        val alarm = AlarmModel(0, alarmTitle, timeInMillis, true)
        alarmID = alarmRepository.addAlarm(alarm)
        Log.d("R/T", "$alarmID**")


        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmID.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC_WAKEUP, timeInMillis] = pendingIntent

    }

    fun getSingleAlarm(alarmId: Int) = alarmRepository.getSingleAlarm(alarmId)

}