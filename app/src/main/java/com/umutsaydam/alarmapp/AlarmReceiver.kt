package com.umutsaydam.alarmapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.umutsaydam.alarmapp.db.AlarmDatabase
import com.umutsaydam.alarmapp.helpers.AlarmNotification
import com.umutsaydam.alarmapp.helpers.AlarmSchedule
import com.umutsaydam.alarmapp.helpers.Alarms
import com.umutsaydam.alarmapp.helpers.IAlarmManager
import com.umutsaydam.alarmapp.helpers.IAlarmNotification
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
        if (!::alarmManager.isInitialized) {
            val alarms = Alarms(context)
            alarmManager = alarms
            val schedule = AlarmSchedule()
            alarmSchedule = schedule
            alarmNotification = AlarmNotification(context)
        }

        val alarmId = intent.getIntExtra("alarmId", -1)
        if (alarmId != -1) {
            val alarmRepository = AlarmRepository(AlarmDatabase(context))
            CoroutineScope(Dispatchers.IO).launch {
                val alarmModel = alarmRepository.getSingleAlarm(alarmId)
                if (alarmModel.alarmRepeat.size > 1) {
                    alarmModel.alarmTime =
                        alarmSchedule.alarmReschedule(alarmModel)
                    alarmRepository.addAlarm(alarmModel)
                } else {
                    AlarmNotification.decreaseCountOfEnabledAlarms()
                    alarmNotification.checkAlarmNotificationState()
                    alarmManager.deleteAlarm(alarmModel)
                    alarmRepository.deleteAlarm(alarmModel)
                }
                Log.d("R/T", alarmModel.alarmTime.toString())

                val myIntent = Intent(context, TimesUpActivity::class.java)

                val bundle = Bundle()
                bundle.putParcelable("alarmModel", alarmModel)
                myIntent.putExtra("alarmModelBundle", bundle)
                myIntent.putExtra("testRingtone", alarmModel.alarmRingtoneUri)

                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(myIntent)
            }
        }
    }
}