package com.umutsaydam.alarmapp.helpers

interface IRingtoneSelector {
    fun selectRingtone()
    fun selectDefaultRingtone()
    fun currentRingtone(): String
}