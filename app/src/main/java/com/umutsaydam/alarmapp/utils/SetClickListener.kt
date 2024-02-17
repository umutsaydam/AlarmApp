package com.umutsaydam.alarmapp.utils

import com.umutsaydam.alarmapp.models.AlarmModel

interface SetClickListener {
    fun setOnLongClickListener(alarmModel: AlarmModel) {}
}