package com.umutsaydam.alarmapp.utils

interface SetCheckedListener {
    fun setOnCheckedListener(position: Int){}
    fun setOnCheckedListener(indexOfDay: Int, isChecked: Boolean){}
}