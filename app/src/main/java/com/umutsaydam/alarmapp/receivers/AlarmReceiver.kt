package com.umutsaydam.alarmapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.umutsaydam.alarmapp.db.AlarmDatabase
import com.umutsaydam.alarmapp.helpers.AlarmNotification
import com.umutsaydam.alarmapp.helpers.AlarmSchedule
import com.umutsaydam.alarmapp.helpers.Alarms
import com.umutsaydam.alarmapp.helpers.IAlarmManager
import com.umutsaydam.alarmapp.helpers.IAlarmNotification
import com.umutsaydam.alarmapp.models.AlarmModel
import com.umutsaydam.alarmapp.repository.AlarmRepository
import com.umutsaydam.alarmapp.ui.TimesUpActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var alarmManager: IAlarmManager
    private lateinit var alarmSchedule: AlarmSchedule
    private lateinit var alarmNotification: IAlarmNotification

    override fun onReceive(context: Context, intent: Intent) {
        initDependencies(context)
        val alarmId = intent.getIntExtra("alarmId", -1)
        if (alarmId != -1) {
            handleAlarm(context, alarmId)
        }
    }

    private fun initDependencies(context: Context) {
        if (!::alarmManager.isInitialized) {
            val alarms = Alarms(context)
            alarmManager = alarms
            alarmSchedule = AlarmSchedule()
            alarmNotification = AlarmNotification(context)
        }
    }

    private fun handleAlarm(context: Context, alarmId: Int) {
        val alarmRepository = AlarmRepository(AlarmDatabase(context))
        CoroutineScope(Dispatchers.IO).launch {
            val alarmModel = alarmRepository.getSingleAlarm(alarmId)
            if (alarmModel.alarmRepeat.size > 1) {
                alarmModel.alarmTime = alarmSchedule.alarmReschedule(alarmModel)
                alarmRepository.addAlarm(alarmModel)
            } else {
                AlarmNotification.decreaseCountOfEnabledAlarms()
                alarmNotification.checkAlarmNotificationState()
                alarmManager.deleteAlarm(alarmModel)
                alarmRepository.deleteAlarm(alarmModel)
            }

            startTimesUpActivity(context, alarmModel)
        }
    }

    private fun startTimesUpActivity(context: Context, alarmModel: AlarmModel) {
        val myIntent = Intent(context, TimesUpActivity::class.java).apply {
            putExtra("alarmModelBundle", Bundle().apply { putParcelable("alarmModel", alarmModel) })
            putExtra("testRingtone", alarmModel.alarmRingtoneUri)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(myIntent)
    }
}
