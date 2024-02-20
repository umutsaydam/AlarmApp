package com.umutsaydam.alarmapp.utils

import com.umutsaydam.alarmapp.models.AlarmModel

interface SetCheckedListener {
    fun setOnCheckedListener(alarmModel: AlarmModel){}
    fun setOnCheckedListener(indexOfDay: Int, isChecked: Boolean){}
}