package com.umutsaydam.alarmapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.umutsaydam.alarmapp.ui.TimesUpActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val myIntent = Intent(context, TimesUpActivity::class.java)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(myIntent)
    }
}