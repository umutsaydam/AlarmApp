package com.umutsaydam.alarmapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.umutsaydam.alarmapp.db.AlarmDatabase
import com.umutsaydam.alarmapp.helpers.AlarmSchedule
import com.umutsaydam.alarmapp.helpers.Alarms
import com.umutsaydam.alarmapp.helpers.IAlarmManager
import com.umutsaydam.alarmapp.helpers.IVibrator
import com.umutsaydam.alarmapp.helpers.Vibrator
import com.umutsaydam.alarmapp.repository.AlarmRepository
import com.umutsaydam.alarmapp.ui.TimesUpActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var alarmManager: IAlarmManager
    private lateinit var alarmSchedule: AlarmSchedule

    override fun onReceive(context: Context, intent: Intent) {
        if (!::alarmManager.isInitialized) {
            val alarms = Alarms(context)
            alarmManager = alarms
            val schedule = AlarmSchedule()
            alarmSchedule = schedule
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
                    alarmManager.deleteAlarm(alarmModel)
                    alarmRepository.deleteAlarm(alarmModel)
                }
                Log.d("R/T", alarmModel.alarmTime.toString())

                val myIntent = Intent(context, TimesUpActivity::class.java)
                myIntent.putExtra("alarmVibrating", alarmModel.alarmVibrating)
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(myIntent)
            }
        }
    }
}